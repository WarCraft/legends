package gg.warcraft.legends.api.core.game;

import gg.warcraft.monolith.api.entity.team.Team;
import gg.warcraft.monolith.api.util.Duration;

import java.util.Map;
import java.util.SortedMap;

public interface WarCopyer extends GameCopyer {

    WarCopyer withWarScore(Map<Team, Integer> score);

    WarCopyer withDailyPoints(SortedMap<String, Map<Team, Integer>> dailyPoints);

    @Override
    WarCopyer withPoints(Map<Team, Integer> points);

    @Override
    WarCopyer withObjectives(Map<String, Team> objectives);

    @Override
    WarCopyer withTimer(Duration timer);

    @Override
    WarCopyer withEndTime(String endTime);

    @Override
    War copy();
}
