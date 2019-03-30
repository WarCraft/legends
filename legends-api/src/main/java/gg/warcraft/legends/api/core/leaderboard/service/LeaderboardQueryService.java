package gg.warcraft.legends.api.core.leaderboard.service;

import gg.warcraft.legends.api.core.leaderboard.LeaderboardEntry;
import gg.warcraft.legends.api.entity.player.LegendsStatistic;

import java.util.List;

public interface LeaderboardQueryService {

    List<LeaderboardEntry> getLeaders(LegendsStatistic statistic, int count);
}
