package gg.warcraft.legends.app.core.game;

import gg.warcraft.legends.api.core.game.GameCopyer;
import gg.warcraft.legends.api.core.game.GameType;
import gg.warcraft.legends.api.core.game.War;
import gg.warcraft.monolith.api.entity.team.Team;
import gg.warcraft.monolith.api.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

public class SimpleWar extends AbstractGame implements War {
    private final Map<Team, Integer> warScore;
    private final SortedMap<String, Map<Team, Integer>> dailyPoints;

    public SimpleWar(int id, Map<Team, Integer> points, Map<Team, Integer> warScore,
                     SortedMap<String, Map<Team, Integer>> dailyPoints, Map<String, Team> objectives, Duration timer,
                     String startTime, String endTime) {
        super(id, GameType.WAR, points, objectives, timer, startTime, endTime);
        this.warScore = warScore;
        this.dailyPoints = dailyPoints;
    }

    @Override
    public Map<Team, Integer> getWarScore() {
        return new HashMap<>(warScore);
    }

    @Override
    public SortedMap<String, Map<Team, Integer>> getDailyPoints() {
        return dailyPoints;
    }

    @Override
    public GameCopyer getCopyer() {
        return new SimpleWarCopyer(this);
    }
}
