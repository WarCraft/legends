/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.game

import gg.warcraft.legends.api.LegendsConfig
import gg.warcraft.monolith.api.core.Duration._
import gg.warcraft.monolith.api.entity.team.Team

class GameService {
  private final val tallyTimerDuration = 900.seconds
  private final val dailyWinnerWarScore = 5
  private final val skirmishWinnerWarScore = 1

  private var _activeGame: Game = _
  private var _currentWar: War = _
  private var _defaultCaptures: Map[String, Team] = _

  def activeGame: Game = _activeGame
  def currentWar: War = _currentWar

  def readConfig(config: LegendsConfig): Unit = {
    // TODO _defaultCaptures = config.defaultCaptures
  }

  def initActiveGame(): Unit = ???

  def tickActiveGame(): Unit = ???

  def startWar(): Unit = ???

  def runTally(): Unit = ???

  def runScore(): Unit = ??? // TODO what is this?

  def endWar(): Unit = ???

  def startSkirmish(): Unit = ???

  def endSkirmish(): Unit = ???
}

/*
public interface GameQueryService {
    Game getGame(int id);
}

    void initializeActiveGame();


public interface GameRepository {
    int getCapturePoints(String objectiveType);

    void save(Game game);

    void saveActiveGame(int gameId);

    void saveCurrentWar(int gameId);

    void saveCapturePoints(String objectiveType, int points);
}
 */













/*
public class DefaultGameCommandService implements GameCommandService {


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
        War newWar = new SimpleWar(newWarId, new HashMap<>(), new HashMap<>(), new TreeMap<>(),
                DEFAULT_CAPTURES, timer, startTime, null);
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

                int currentPoints = newPoints.getOrDefault(occupiers, 0);
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

        Map<LegendsTeam, Integer> newPoints = new HashMap<>();
        Arrays.stream(LegendsTeam.values()).forEach(team -> newPoints.put(team, 0));

        War updatedWar = war.getCopyer()
                .withWarScore(newScore)
                .withDailyPoints(newDailyPoints)
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
        Skirmish newSkirmish = new SimpleSkirmish(newSkirmishId, new HashMap<>(), DEFAULT_CAPTURES,
                timer, startTime, null);
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
        if (winners.size() > 1 && skirmish.getPoints().get(winners.iterator().next()) != 0) {
            // wait with ending skirmish until 1 team gets ahead in points
            return;
        }

        String endTime = LocalDateTime.now().toString();
        Game updatedSkirmish = skirmish.getCopyer()
                .withEndTime(endTime)
                .copy();
        gameRepository.save(updatedSkirmish);
        War currentWar = (War) gameRepository.getCurrentWar();

        LegendsTeam winner = null;
        if (winners.isEmpty() || skirmish.getPoints().get(winners.iterator().next()) == 0) {
            // don't delay skirmish end if no one is playing
        } else {
            winner = winners.iterator().next();
            Map<LegendsTeam, Integer> newScore = currentWar.getWarScore();
            int currentScore = newScore.getOrDefault(winner, 0);
            newScore.put(winner, currentScore + SKIRMISH_WINNER_SCORE);
            gameRepository.save(currentWar);

            // TODO
            Team winner = event.getWinner();
            Item warCrate = lootCrateBuilderFactory.createLootCrateBuilder(LootCrateType.WAR_CRATE).build();
            playerQueryService.getOnlinePlayers().forEach(player -> {
                int rewardCount = player.getTeam() == winner ? 5 : 2;
                Item stackedItem = ((StackableItem) warCrate).withCount(rewardCount);
                playerCommandService.giveItem(player.getId(), stackedItem, false);
            });
        }

        gameRepository.saveActiveGame(currentWar.getId());

        SkirmishFinishedEvent skirmishFinishedEvent = new SimpleSkirmishFinishedEvent(skirmish.getId(), winner);
        eventService.publish(skirmishFinishedEvent);

        initializeActiveGame();

        WarResumedEvent warResumedEvent = new SimpleWarResumedEvent(currentWar.getId());
        eventService.publish(warResumedEvent);
    }
}

 */














/*
@Singleton
public class DefaultGameRepository implements GameRepository {
    private static final String GAME_KEY_PREFIX = "game:";
    private static final String ACTIVE_GAME_ID_KEY = "game:activeGameId";
    private static final String CURRENT_WAR_ID_KEY = "game:currentWarId";

    private final PersistenceService persistenceService;
    private final TimeUtils timeUtils;
    private final ObjectMapper jsonMapper;
    private final Logger pluginLogger;
    private final Map<String, Integer> capturePoints;

    @Inject
    public DefaultGameRepository(PersistenceService persistenceService, TimeUtils timeUtils,
                                 @JsonMapper ObjectMapper jsonMapper, @PluginLogger Logger pluginLogger) {
        this.persistenceService = persistenceService;
        this.timeUtils = timeUtils;
        this.jsonMapper = jsonMapper;
        this.pluginLogger = pluginLogger;
        this.capturePoints = new HashMap<>();
    }

    String createGameKey(int id) {
        return GAME_KEY_PREFIX + id;
    }

    Game mapItemToWar(WarItem item) {
        int id = item.getId();
        Map<LegendsTeam, Integer> points = new HashMap<>();
        item.getPoints().forEach((teamName, teamPoints) -> {
            LegendsTeam team = LegendsTeam.valueOf(teamName);
            points.put(team, teamPoints);
        });
        Map<String, LegendsTeam> objectives = new HashMap<>();
        item.getObjectives().forEach((objectiveId, teamName) -> {
            LegendsTeam team = teamName == null ? null : LegendsTeam.valueOf(teamName);
            objectives.put(objectiveId, team);
        });
        Duration timer = timeUtils.createDurationInSeconds(item.getTimer());
        String startTime = item.getStartTime();
        String endTime = item.getEndTime();
        Map<LegendsTeam, Integer> warScore = new HashMap<>();
        item.getWarScore().forEach((teamName, teamScore) -> {
            LegendsTeam team = LegendsTeam.valueOf(teamName);
            warScore.put(team, teamScore);
        });
        SortedMap<String, Map<LegendsTeam, Integer>> dailyPoints = new TreeMap<>();
        item.getDailyPoints().forEach((date, dailyPointsMap) -> {
            Map<LegendsTeam, Integer> dateDailyPoints = new HashMap<>();
            dailyPointsMap.forEach((teamName, teamDailyPoints) -> {
                LegendsTeam team = LegendsTeam.valueOf(teamName);
                dateDailyPoints.put(team, teamDailyPoints);
            });
            dailyPoints.put(date, dateDailyPoints);
        });
        return new SimpleWar(id, points, warScore, dailyPoints, objectives, timer, startTime, endTime);
    }

    Game mapItemToSkirmish(SkirmishItem item) {
        int id = item.getId();
        Map<LegendsTeam, Integer> points = new HashMap<>();
        item.getPoints().forEach((teamName, teamPoints) -> {
            LegendsTeam team = LegendsTeam.valueOf(teamName);
            points.put(team, teamPoints);
        });
        Map<String, LegendsTeam> objectives = new HashMap<>();
        item.getObjectives().forEach((objectiveId, teamName) -> {
            if (teamName != null) {
                LegendsTeam team = LegendsTeam.valueOf(teamName);
                objectives.put(objectiveId, team);
            } else {
                objectives.put(objectiveId, null);
            }
        });
        Duration timer = timeUtils.createDurationInSeconds(item.getTimer());
        String startTime = item.getStartTime();
        String endTime = item.getEndTime();
        return new SimpleSkirmish(id, points, objectives, timer, startTime, endTime);
    }

    @Override
    public Game getGame(int gameId) {
        String gameKey = createGameKey(gameId);
        String gameJson = persistenceService.get(gameKey);
        if (gameJson == null) {
            return null;
        }

        try {
            if (gameJson.contains(GameType.WAR.name())) {
                WarItem warItem = jsonMapper.readValue(gameJson, WarItem.class);
                return mapItemToWar(warItem);
            } else if (gameJson.contains(GameType.SKIRMISH.name())) {
                SkirmishItem skirmishItem = jsonMapper.readValue(gameJson, SkirmishItem.class);
                return mapItemToSkirmish(skirmishItem);
            } else {
                throw new IllegalArgumentException("Failed to map game item to game, could not find game type.");
            }
        } catch (IOException ex) {
            pluginLogger.severe("Failed to get game with id " + gameId + ": " + ex.getMessage());
            return null;
        }
    }

    private Game getActiveCurrentGame(String key) {
        try {
            String gameIdString = persistenceService.get(key);
            int gameId = Integer.parseInt(gameIdString);
            return getGame(gameId);
        } catch (NumberFormatException ex) {
            pluginLogger.severe("Failed to get game for id " + key + ": " + ex.getMessage());
            return null;
        }
    }

    @Override
    public Game getActiveGame() {
        return getActiveCurrentGame(ACTIVE_GAME_ID_KEY);
    }

    @Override
    public Game getCurrentWar() {
        return getActiveCurrentGame(CURRENT_WAR_ID_KEY);
    }

    @Override
    public int getCapturePoints(String objectiveType) {
        return capturePoints.getOrDefault(objectiveType, 0);
    }

    GameItem mapWarToItem(War war) {
        int id = war.getId();
        String type = war.getType().name();
        Map<String, Integer> points = new HashMap<>();
        war.getPoints().forEach((team, teamPoints) -> points.put(team.name(), teamPoints));
        Map<String, String> objectives = new HashMap<>();
        war.getObjectives().forEach((objectiveId, team) ->
                objectives.put(objectiveId, team == null ? null : team.name()));
        int timer = war.getTimer().inSeconds();
        String startTime = war.getStartTime();
        String endTime = war.getEndTime();
        Map<String, Integer> warScore = new HashMap<>();
        war.getWarScore().forEach((team, teamScore) -> warScore.put(team.name(), teamScore));
        SortedMap<String, Map<String, Integer>> dailyPoints = new TreeMap<>();
        war.getDailyPoints().forEach((date, dailyPointsMap) -> {
            Map<String, Integer> dateDailyPoints = new HashMap<>();
            dailyPointsMap.forEach((team, teamDailyPoints) -> dateDailyPoints.put(team.name(), teamDailyPoints));
            dailyPoints.put(date, dateDailyPoints);
        });
        return new WarItem(id, type, points, warScore, dailyPoints, objectives, timer, startTime, endTime);
    }

    GameItem mapSkirmishToItem(Skirmish skirmish) {
        int id = skirmish.getId();
        String type = skirmish.getType().name();
        Map<String, Integer> points = new HashMap<>();
        skirmish.getPoints().forEach((team, teamPoints) -> points.put(team.name(), teamPoints));
        Map<String, String> objectives = new HashMap<>();
        skirmish.getObjectives().forEach((objectiveId, team) ->
                objectives.put(objectiveId, team != null ? team.name() : null));
        int timer = skirmish.getTimer().inSeconds();
        String startTime = skirmish.getStartTime();
        String endTime = skirmish.getEndTime();
        return new SkirmishItem(id, type, points, objectives, timer, startTime, endTime);
    }

    @Override
    public void save(Game game) {
        String gameKey = createGameKey(game.getId());
        GameItem gameItem;
        if (game.getType() == GameType.WAR) {
            gameItem = mapWarToItem((War) game);
        } else if (game.getType() == GameType.SKIRMISH) {
            gameItem = mapSkirmishToItem((Skirmish) game);
        } else {
            throw new IllegalArgumentException("Failed to save game with id " + game.getId() +
                    ", could recognize type " + game.getType());
        }

        try {
            String gameJson = jsonMapper.writeValueAsString(gameItem);
            persistenceService.set(gameKey, gameJson);
        } catch (IOException ex) {
            pluginLogger.severe("Failed to save game with id " + game.getId() + ": " + ex.getMessage());
        }
    }

    @Override
    public void saveActiveGame(int gameId) {
        persistenceService.set(ACTIVE_GAME_ID_KEY, Integer.toString(gameId));
    }

    @Override
    public void saveCurrentWar(int gameId) {
        persistenceService.set(CURRENT_WAR_ID_KEY, Integer.toString(gameId));
    }

    @Override
    public void saveCapturePoints(String objectiveType, int points) {
        capturePoints.put(objectiveType, points);
    }
}

 */