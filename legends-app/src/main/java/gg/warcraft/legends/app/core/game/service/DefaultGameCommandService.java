package gg.warcraft.legends.app.core.game.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.config.ServerConfiguration;
import gg.warcraft.legends.api.core.game.Game;
import gg.warcraft.legends.api.core.game.GameType;
import gg.warcraft.legends.api.core.game.Skirmish;
import gg.warcraft.legends.api.core.game.War;
import gg.warcraft.legends.api.core.game.event.SkirmishFinishedEvent;
import gg.warcraft.legends.api.core.game.event.SkirmishStartedEvent;
import gg.warcraft.legends.api.core.game.event.WarFinishedEvent;
import gg.warcraft.legends.api.core.game.event.WarResumedEvent;
import gg.warcraft.legends.api.core.game.event.WarStartedEvent;
import gg.warcraft.legends.api.core.game.event.WarTallyEvent;
import gg.warcraft.legends.api.core.game.service.GameCommandService;
import gg.warcraft.legends.api.core.game.service.GameRepository;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.app.core.game.SimpleSkirmish;
import gg.warcraft.legends.app.core.game.SimpleWar;
import gg.warcraft.legends.app.core.game.event.SimpleSkirmishFinishedEvent;
import gg.warcraft.legends.app.core.game.event.SimpleSkirmishStartedEvent;
import gg.warcraft.legends.app.core.game.event.SimpleWarFinishedEvent;
import gg.warcraft.legends.app.core.game.event.SimpleWarResumedEvent;
import gg.warcraft.legends.app.core.game.event.SimpleWarStartedEvent;
import gg.warcraft.legends.app.core.game.event.SimpleWarTallyEvent;
import gg.warcraft.monolith.api.core.EventService;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.util.TimeUtils;
import gg.warcraft.siege.api.objective.service.ObjectiveCommandService;
import gg.warcraft.siege.api.objective.service.ObjectiveQueryService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class DefaultGameCommandService implements GameCommandService {
    private static final int TALLY_TIMER_IN_SECONDS = 900;
    private static final int DAILY_WINNER_SCORE = 5;
    private static final int SKIRMISH_WINNER_SCORE = 1;

    private final GameRepository gameRepository;
    private final ObjectiveQueryService objectiveQueryService;
    private final ObjectiveCommandService objectiveCommandService;
    private final EventService eventService;
    private final TimeUtils timeUtils;

    @Inject
    public DefaultGameCommandService(GameRepository gameRepository,
                                     ObjectiveQueryService objectiveQueryService,
                                     ObjectiveCommandService objectiveCommandService,
                                     EventService eventService, TimeUtils timeUtils) {
        this.gameRepository = gameRepository;
        this.objectiveQueryService = objectiveQueryService;
        this.objectiveCommandService = objectiveCommandService;
        this.eventService = eventService;
        this.timeUtils = timeUtils;
    }

    @Override
    public void readConfiguration(ServerConfiguration configuration) {
        configuration.getObjectivePoints().forEach(gameRepository::saveCapturePoints);
    }

    private int getNextGameId() {
        Game currentWar = gameRepository.getCurrentWar();
        if (currentWar == null) {
            return 1;
        }

        int newGameId = currentWar.getId() + 1;
        while (gameRepository.getGame(newGameId) != null) {
            newGameId += 1;
        }
        return newGameId;
    }

    @Override
    public void initializeActiveGame() {
        Game activeGame = gameRepository.getActiveGame();
        if (activeGame == null) {
            throw new IllegalStateException("Failed to initialize active game, none seems to be active.");
        }

        activeGame.getObjectives().forEach(objectiveCommandService::setOccupiers);
    }

    @Override
    public void tickActiveGame() {
        Game activeGame = gameRepository.getActiveGame();
        if (activeGame == null) {
            return;
        }

        Duration timer = activeGame.getTimer();
        int secondsRemaining = timer.inSeconds();
        if (secondsRemaining <= 1) {
            GameType gameType = activeGame.getType();
            switch (gameType) {
                case WAR:
                    runTally();

                    War updatedWar = (War) gameRepository.getActiveGame();
                    SortedMap<String, Map<LegendsTeam, Integer>> dailyPoints = updatedWar.getDailyPoints();
                    String lastScoreDate = dailyPoints.isEmpty()
                            ? updatedWar.getStartTime()
                            : updatedWar.getDailyPoints().lastKey();
                    int lastScoreDay = LocalDateTime.parse(lastScoreDate).getDayOfMonth();
                    int currentDay = LocalDateTime.now().getDayOfMonth();
                    if (lastScoreDay != currentDay) {
                        runScore();

                        if (currentDay == 1) {
                            endWar();
                            startWar();
                        } else {
                            War mostUpToDateWar = (War) gameRepository.getActiveGame();
                            Duration newTimer = timeUtils.createDurationInSeconds(TALLY_TIMER_IN_SECONDS);
                            War newMostUpToDateWar = mostUpToDateWar.getCopyer()
                                    .withTimer(newTimer)
                                    .copy();
                            gameRepository.save(newMostUpToDateWar);
                        }
                    } else {
                        War mostUpToDateWar = (War) gameRepository.getActiveGame();
                        Duration newTimer = timeUtils.createDurationInSeconds(TALLY_TIMER_IN_SECONDS);
                        War newMostUpToDateWar = mostUpToDateWar.getCopyer()
                                .withTimer(newTimer)
                                .copy();
                        gameRepository.save(newMostUpToDateWar);
                    }
                    break;
                case SKIRMISH:
                    endSkirmish();
                    break;
                default:
                    throw new IllegalStateException("Failed to tick game with illegal type " + gameType);
            }
        } else {
            Duration newTimer = timeUtils.createDurationInSeconds(secondsRemaining - 1);
            Game updatedGame = activeGame.getCopyer()
                    .withTimer(newTimer)
                    .copy();
            gameRepository.save(updatedGame);
        }
    }

    @Override
    public void startWar() {
        int newWarId = getNextGameId();
        Duration timer = timeUtils.createDurationInSeconds(TALLY_TIMER_IN_SECONDS);
        String startTime = LocalDateTime.now().toString();
        War newWar = new SimpleWar(newWarId, new HashMap<>(), new HashMap<>(), new TreeMap<>(), new HashMap<>(), timer,
                startTime, null);
        gameRepository.save(newWar);
        gameRepository.saveActiveGame(newWarId);
        gameRepository.saveCurrentWar(newWarId);

        WarStartedEvent warStartedEvent = new SimpleWarStartedEvent(newWarId);
        eventService.publish(warStartedEvent);
    }

    @Override
    public void runTally() {
        War war = (War) gameRepository.getCurrentWar();
        Map<String, LegendsTeam> newObjectives = war.getObjectives();
        Map<LegendsTeam, Integer> newPoints = war.getPoints();
        objectiveQueryService.getObjectives().forEach(objective -> {
            LegendsTeam occupiers = (LegendsTeam) objective.getOccupiers();
            if (occupiers != null) {
                newObjectives.put(objective.getId(), occupiers);

                int currentPoints = newPoints.get(occupiers);
                String objectiveType = objective.getType();
                int objectiveTypePoints = gameRepository.getCapturePoints(objectiveType);
                newPoints.put(occupiers, currentPoints + objectiveTypePoints);
            }
        });

        Game newWar = war.getCopyer()
                .withObjectives(newObjectives)
                .withPoints(newPoints)
                .copy();
        gameRepository.save(newWar);

        // TODO should this event expose the points that were just gained, or current war total points
        WarTallyEvent tallyEvent = new SimpleWarTallyEvent(war.getId(), newPoints);
        eventService.publish(tallyEvent);
    }

    private Set<LegendsTeam> computeWinners(Map<LegendsTeam, Integer> points) {
        int mostPoints = 0;
        Set<LegendsTeam> winners = new HashSet<>();
        for (Map.Entry<LegendsTeam, Integer> contestant : points.entrySet()) {
            int contestantPoints = contestant.getValue();
            if (contestantPoints > mostPoints) {
                mostPoints = contestantPoints;
                winners.clear();
                winners.add(contestant.getKey());
            } else if (contestantPoints == mostPoints) {
                winners.add(contestant.getKey());
            }
        }
        return winners;
    }

    @Override
    public void runScore() {
        War war = (War) gameRepository.getCurrentWar();
        Map<LegendsTeam, Integer> points = war.getPoints();

        Set<LegendsTeam> winners = computeWinners(points);
        if (winners.size() != 1) {
            // wait with running score until 1 team gets ahead in points
            return;
        }

        LegendsTeam winner = winners.iterator().next();
        SortedMap<String, Map<LegendsTeam, Integer>> newDailyPoints = war.getDailyPoints();
        String scoreTime = LocalDateTime.now().toString();
        newDailyPoints.put(scoreTime, points);

        Map<LegendsTeam, Integer> newScore = war.getWarScore();
        int currentScore = newScore.getOrDefault(winner, 0);
        newScore.put(winner, currentScore + DAILY_WINNER_SCORE);

        Map<String, LegendsTeam> newObjectives = new HashMap<>();
        objectiveQueryService.getObjectives().forEach(objective -> {
            newObjectives.put(objective.getId(), null);
            objectiveCommandService.setOccupiers(objective.getId(), null);
        });
        Map<LegendsTeam, Integer> newPoints = new HashMap<>();
        Arrays.stream(LegendsTeam.values()).forEach(team -> newPoints.put(team, 0));

        War updatedWar = war.getCopyer()
                .withWarScore(newScore)
                .withDailyPoints(newDailyPoints)
                .withObjectives(newObjectives)
                .withPoints(newPoints)
                .copy();
        gameRepository.save(updatedWar);
    }

    @Override
    public void endWar() {
        War war = (War) gameRepository.getActiveGame();
        String endTime = LocalDateTime.now().toString();
        War updatedWar = war.getCopyer()
                .withEndTime(endTime)
                .copy();
        gameRepository.save(updatedWar);

        WarFinishedEvent warFinishedEvent = new SimpleWarFinishedEvent(war.getId());
        eventService.publish(warFinishedEvent);
    }

    @Override
    public void startSkirmish() {
        int newSkirmishId = getNextGameId();
        Duration timer = timeUtils.createDurationInSeconds(1800);
        String startTime = LocalDateTime.now().toString();
        Skirmish newSkirmish = new SimpleSkirmish(newSkirmishId, new HashMap<>(), new HashMap<>(), timer, startTime,
                null);
        gameRepository.save(newSkirmish);
        gameRepository.saveActiveGame(newSkirmishId);

        // WarPausedEvent warPausedEvent = new SimpleWarPausedEvent(?);
        // eventService.publish(warPausedEvent);

        initializeActiveGame();

        SkirmishStartedEvent skirmishStartedEvent = new SimpleSkirmishStartedEvent(newSkirmishId);
        eventService.publish(skirmishStartedEvent);
    }

    @Override
    public void endSkirmish() {
        Skirmish skirmish = (Skirmish) gameRepository.getActiveGame();

        Set<LegendsTeam> winners = computeWinners(skirmish.getPoints());
        if (winners.size() != 1) {
            // wait with ending skirmish until 1 team gets ahead in points
            return;
        }

        String endTime = LocalDateTime.now().toString();
        Game updatedSkirmish = skirmish.getCopyer()
                .withEndTime(endTime)
                .copy();
        gameRepository.save(updatedSkirmish);
        War currentWar = (War) gameRepository.getCurrentWar();

        LegendsTeam winner = winners.iterator().next();
        Map<LegendsTeam, Integer> newScore = currentWar.getWarScore();
        int currentScore = newScore.getOrDefault(winner, 0);
        newScore.put(winner, currentScore + SKIRMISH_WINNER_SCORE);
        gameRepository.save(currentWar);

        gameRepository.saveActiveGame(currentWar.getId());

        SkirmishFinishedEvent skirmishFinishedEvent = new SimpleSkirmishFinishedEvent(skirmish.getId(), winner);
        eventService.publish(skirmishFinishedEvent);

        initializeActiveGame();

        WarResumedEvent warResumedEvent = new SimpleWarResumedEvent(currentWar.getId());
        eventService.publish(warResumedEvent);
    }
}
