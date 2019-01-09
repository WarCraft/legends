package gg.warcraft.legends.api.core.game;

import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.monolith.api.util.Duration;

import java.util.Map;
import java.util.SortedMap;

public interface WarCopyer extends GameCopyer {

    WarCopyer withWarScore(Map<LegendsTeam, Integer> score);

    WarCopyer withDailyPoints(SortedMap<String, Map<LegendsTeam, Integer>> dailyPoints);

    @Override
    WarCopyer withPoints(Map<LegendsTeam, Integer> points);

    @Override
    WarCopyer withObjectives(Map<String, LegendsTeam> objectives);

    @Override
    WarCopyer withTimer(Duration timer);

    @Override
    WarCopyer withEndTime(String endTime);

    @Override
    War copy();
}
