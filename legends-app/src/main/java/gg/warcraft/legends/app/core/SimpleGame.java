package gg.warcraft.legends.app.core;

import gg.warcraft.legends.api.core.Game;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.monolith.api.util.Duration;

import java.util.Map;

public class SimpleGame implements Game {
    private final int id;
    private final String type;
    private final Map<LegendsTeam, Integer> score;
    private final Map<String, LegendsTeam> objectives;
    private final Duration timer;
    private final String startTime;
    private final String endTime;

    public SimpleGame(int id, String type, Map<LegendsTeam, Integer> score, Map<String, LegendsTeam> objectives,
                      Duration timer, String startTime, String endTime) {
        this.id = id;
        this.type = type;
        this.score = score;
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
    public String getType() {
        return type;
    }

    @Override
    public Map<LegendsTeam, Integer> getScore() {
        return score;
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
