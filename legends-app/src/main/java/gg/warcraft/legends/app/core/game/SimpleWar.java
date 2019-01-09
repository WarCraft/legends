package gg.warcraft.legends.app.core.game;

import gg.warcraft.legends.api.core.game.GameType;
import gg.warcraft.legends.api.core.game.War;
import gg.warcraft.legends.api.core.game.WarCopyer;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.monolith.api.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

public class SimpleWar extends AbstractGame implements War {
    private final Map<LegendsTeam, Integer> warScore;
    private final SortedMap<String, Map<LegendsTeam, Integer>> dailyPoints;

    public SimpleWar(int id, Map<LegendsTeam, Integer> points, Map<LegendsTeam, Integer> warScore,
                     SortedMap<String, Map<LegendsTeam, Integer>> dailyPoints, Map<String, LegendsTeam> objectives,
                     Duration timer, String startTime, String endTime) {
        super(id, GameType.WAR, points, objectives, timer, startTime, endTime);
        this.warScore = warScore;
        this.dailyPoints = dailyPoints;
    }

    @Override
    public Map<LegendsTeam, Integer> getWarScore() {
        return new HashMap<>(warScore);
    }

    @Override
    public SortedMap<String, Map<LegendsTeam, Integer>> getDailyPoints() {
        return dailyPoints;
    }

    @Override
    public WarCopyer getCopyer() {
        return new SimpleWarCopyer(this);
    }
}
