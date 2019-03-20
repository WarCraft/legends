package gg.warcraft.legends.api.core.leaderboard.service;

import gg.warcraft.legends.api.entity.player.LegendsStatistic;

import java.util.List;
import java.util.UUID;

public interface LeaderboardQueryService {

    List<UUID> getLeaders(LegendsStatistic statistic, int count);
}
