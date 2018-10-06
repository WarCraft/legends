package gg.warcraft.legends.app.core.game;

import gg.warcraft.legends.api.core.game.War;
import gg.warcraft.legends.api.core.game.WarCopyer;
import gg.warcraft.monolith.api.entity.team.Team;
import gg.warcraft.monolith.api.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class SimpleWarCopyer implements WarCopyer {
    private final War war;

    private Map<Team, Integer> points;
    private Map<Team, Integer> warScore;
    private SortedMap<String, Map<Team, Integer>> dailyPoints;
    private Map<String, Team> objectives;
    private Duration timer;
    private String endTime;

    public SimpleWarCopyer(War war) {
        this.war = war;
        this.points = war.getPoints();
        this.warScore = war.getWarScore();
        this.dailyPoints = war.getDailyPoints();
        this.objectives = war.getObjectives();
        this.timer = war.getTimer();
        this.endTime = war.getEndTime();
    }

    @Override
    public WarCopyer withWarScore(Map<Team, Integer> score) {
        this.warScore = new HashMap<>(score);
        return this;
    }

    @Override
    public WarCopyer withDailyPoints(SortedMap<String, Map<Team, Integer>> dailyPoints) {
        this.dailyPoints = new TreeMap<>(dailyPoints);
        return this;
    }

    @Override
    public WarCopyer withPoints(Map<Team, Integer> points) {
        this.points = points;
        return this;
    }

    @Override
    public WarCopyer withObjectives(Map<String, Team> objectives) {
        this.objectives = objectives;
        return this;
    }

    @Override
    public WarCopyer withTimer(Duration timer) {
        this.timer = timer;
        return this;
    }

    @Override
    public WarCopyer withEndTime(String endTime) {
        this.endTime = endTime;
        return this;
    }

    @Override
    public War copy() {
        return new SimpleWar(war.getId(), points, warScore, dailyPoints, objectives, timer, war.getStartTime(), endTime);
    }
}
