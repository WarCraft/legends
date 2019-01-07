package gg.warcraft.legends.app.core.game.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import gg.warcraft.legends.api.core.game.Game;
import gg.warcraft.legends.api.core.game.GameType;
import gg.warcraft.legends.api.core.game.Skirmish;
import gg.warcraft.legends.api.core.game.War;
import gg.warcraft.legends.api.core.game.service.GameRepository;
import gg.warcraft.legends.app.core.game.SimpleSkirmish;
import gg.warcraft.legends.app.core.game.SimpleWar;
import gg.warcraft.legends.app.core.game.persistence.GameItem;
import gg.warcraft.legends.app.core.game.persistence.SkirmishItem;
import gg.warcraft.legends.app.core.game.persistence.WarItem;
import gg.warcraft.monolith.api.core.JsonMapper;
import gg.warcraft.monolith.api.core.PersistenceService;
import gg.warcraft.monolith.api.core.PluginLogger;
import gg.warcraft.monolith.api.entity.team.Team;
import gg.warcraft.monolith.api.entity.team.service.TeamQueryService;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.util.TimeUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

@Singleton
public class DefaultGameRepository implements GameRepository {
    private static final String GAME_KEY_PREFIX = "game:";
    private static final String ACTIVE_GAME_ID_KEY = "game:activeGameId";
    private static final String CURRENT_WAR_ID_KEY = "game:currentWarId";

    private final PersistenceService persistenceService;
    private final TeamQueryService teamQueryService;
    private final TimeUtils timeUtils;
    private final ObjectMapper jsonMapper;
    private final Logger pluginLogger;
    private final Map<String, Integer> capturePoints;

    @Inject
    public DefaultGameRepository(PersistenceService persistenceService, TeamQueryService teamQueryService,
                                 TimeUtils timeUtils, @JsonMapper ObjectMapper jsonMapper,
                                 @PluginLogger Logger pluginLogger) {
        this.persistenceService = persistenceService;
        this.teamQueryService = teamQueryService;
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
        Map<Team, Integer> points = new HashMap<>();
        item.getPoints().forEach((teamName, teamPoints) -> {
            Team team = teamQueryService.getTeam(teamName);
            points.put(team, teamPoints);
        });
        Map<String, Team> objectives = new HashMap<>();
        item.getObjectives().forEach((objectiveId, teamName) -> {
            Team team = teamQueryService.getTeam(teamName);
            objectives.put(objectiveId, team);
        });
        Duration timer = timeUtils.createDurationInSeconds(item.getTimer());
        String startTime = item.getStartTime();
        String endTime = item.getEndTime();
        Map<Team, Integer> warScore = new HashMap<>();
        item.getWarScore().forEach((teamName, teamScore) -> {
            Team team = teamQueryService.getTeam(teamName);
            warScore.put(team, teamScore);
        });
        SortedMap<String, Map<Team, Integer>> dailyPoints = new TreeMap<>();
        item.getDailyPoints().forEach((date, dailyPointsMap) -> {
            Map<Team, Integer> dateDailyPoints = new HashMap<>();
            dailyPointsMap.forEach((teamName, teamDailyPoints) -> {
                Team team = teamQueryService.getTeam(teamName);
                dateDailyPoints.put(team, teamDailyPoints);
            });
            dailyPoints.put(date, dateDailyPoints);
        });
        return new SimpleWar(id, points, warScore, dailyPoints, objectives, timer, startTime, endTime);
    }

    Game mapItemToSkirmish(SkirmishItem item) {
        int id = item.getId();
        Map<Team, Integer> points = new HashMap<>();
        item.getPoints().forEach((teamName, teamPoints) -> {
            Team team = teamQueryService.getTeam(teamName);
            points.put(team, teamPoints);
        });
        Map<String, Team> objectives = new HashMap<>();
        item.getObjectives().forEach((objectiveId, teamName) -> {
            Team team = teamQueryService.getTeam(teamName);
            objectives.put(objectiveId, team);
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
        war.getPoints().forEach((team, teamPoints) -> points.put(team.getName(), teamPoints));
        Map<String, String> objectives = new HashMap<>();
        war.getObjectives().forEach((objectiveId, team) ->
                objectives.put(objectiveId, team == null ? null : team.getName()));
        int timer = war.getTimer().inSeconds();
        String startTime = war.getStartTime();
        String endTime = war.getEndTime();
        Map<String, Integer> warScore = new HashMap<>();
        war.getWarScore().forEach((team, teamScore) -> warScore.put(team.getName(), teamScore));
        SortedMap<String, Map<String, Integer>> dailyPoints = new TreeMap<>();
        war.getDailyPoints().forEach((date, dailyPointsMap) -> {
            Map<String, Integer> dateDailyPoints = new HashMap<>();
            dailyPointsMap.forEach((team, teamDailyPoints) -> dateDailyPoints.put(team.getName(), teamDailyPoints));
            dailyPoints.put(date, dateDailyPoints);
        });
        return new WarItem(id, type, points, warScore, dailyPoints, objectives, timer, startTime, endTime);
    }

    GameItem mapSkirmishToItem(Skirmish skirmish) {
        int id = skirmish.getId();
        String type = skirmish.getType().name();
        Map<String, Integer> points = new HashMap<>();
        skirmish.getPoints().forEach((team, teamPoints) -> points.put(team.getName(), teamPoints));
        Map<String, String> objectives = new HashMap<>();
        skirmish.getObjectives().forEach((objectiveId, team) ->
                objectives.put(objectiveId, team != null ? team.getName() : null));
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
