package gg.warcraft.legends.app.core.game;

import gg.warcraft.legends.api.core.game.Game;
import gg.warcraft.legends.api.core.game.GameType;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.monolith.api.util.Duration;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractGame implements Game {
    private final int id;
    private final GameType type;
    private final Map<LegendsTeam, Integer> points;
    private final Map<String, LegendsTeam> objectives;
    private final Duration timer;
    private final String startTime;
    private final String endTime;

    public AbstractGame(int id, GameType type, Map<LegendsTeam, Integer> points, Map<String, LegendsTeam> objectives,
                        Duration timer, String startTime, String endTime) {
        this.id = id;
        this.type = type;
        this.points = points;
        this.objectives = objectives;
        this.timer = timer;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public GameType getType() {
        return type;
    }

    @Override
    public Map<LegendsTeam, Integer> getPoints() {
        if(points != null) {
            return points;
        }
        return new HashMap<>();
    }

    @Override
    public Map<String, LegendsTeam> getObjectives() {
        return objectives;
    }

    @Override
    public Duration getTimer() {
        return timer;
    }

    @Override
    public String getStartTime() {
        return startTime;
    }

    @Override
    public String getEndTime() {
        return endTime;
    }
}
