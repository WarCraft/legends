package gg.warcraft.legends.app.core;

import gg.warcraft.legends.api.core.Game;
import gg.warcraft.legends.api.core.GameCopyer;
import gg.warcraft.legends.api.core.Skirmish;
import gg.warcraft.monolith.api.entity.team.Team;
import gg.warcraft.monolith.api.util.Duration;

import java.util.Map;

public class SimpleSkirmishCopyer implements GameCopyer {
    private final Skirmish skirmish;

    private Map<Team, Integer> points;
    private Map<String, Team> objectives;
    private Duration timer;
    private String endTime;

    public SimpleSkirmishCopyer(Skirmish skirmish) {
        this.skirmish = skirmish;
        this.points = skirmish.getPoints();
        this.objectives = skirmish.getObjectives();
        this.timer = skirmish.getTimer();
        this.endTime = skirmish.getEndTime();
    }

    @Override
    public GameCopyer withPoints(Map<Team, Integer> points) {
        this.points = points;
        return this;
    }

    @Override
    public GameCopyer withObjectives(Map<String, Team> objectives) {
        this.objectives = objectives;
        return this;
    }

    @Override
    public GameCopyer withTimer(Duration timer) {
        this.timer = timer;
        return this;
    }

    @Override
    public GameCopyer withEndTime(String endTime) {
        this.endTime = endTime;
        return this;
    }

    @Override
    public Game copy() {
        return new SimpleSkirmish(skirmish.getId(), points, objectives, timer, skirmish.getStartTime(), endTime);
    }
}
