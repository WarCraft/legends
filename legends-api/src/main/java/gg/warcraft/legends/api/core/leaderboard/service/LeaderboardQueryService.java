package gg.warcraft.legends.api.core.leaderboard.service;

import gg.warcraft.legends.api.entity.player.LegendsStatistic;

import java.util.Map;
import java.util.UUID;

public interface LeaderboardQueryService {

    Map.Entry<UUID, Integer> getLeader(LegendsStatistic statistic);
}
