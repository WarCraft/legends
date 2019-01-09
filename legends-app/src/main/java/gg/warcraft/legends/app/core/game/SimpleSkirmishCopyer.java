package gg.warcraft.legends.app.core.game;

import gg.warcraft.legends.api.core.game.Game;
import gg.warcraft.legends.api.core.game.GameCopyer;
import gg.warcraft.legends.api.core.game.Skirmish;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.monolith.api.util.Duration;

import java.util.Map;

public class SimpleSkirmishCopyer implements GameCopyer {
    private final Skirmish skirmish;

    private Map<LegendsTeam, Integer> points;
    private Map<String, LegendsTeam> objectives;
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
    public GameCopyer withPoints(Map<LegendsTeam, Integer> points) {
        this.points = points;
        return this;
    }

    @Override
    public GameCopyer withObjectives(Map<String, LegendsTeam> objectives) {
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
