package gg.warcraft.legends.app.core;

import gg.warcraft.legends.api.core.Game;
import gg.warcraft.legends.api.core.GameType;
import gg.warcraft.monolith.api.entity.team.Team;
import gg.warcraft.monolith.api.util.Duration;

import java.util.Map;

public abstract class AbstractGame implements Game {
    private final int id;
    private final GameType type;
    private final Map<Team, Integer> points;
    private final Map<String, Team> objectives;
    private final Duration timer;
    private final String startTime;
    private final String endTime;

    public AbstractGame(int id, GameType type, Map<Team, Integer> points, Map<String, Team> objectives, Duration timer,
                        String startTime, String endTime) {
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
    public Map<Team, Integer> getPoints() {
        return points;
    }

    @Override
    public Map<String, Team> getObjectives() {
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
