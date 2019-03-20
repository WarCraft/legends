package gg.warcraft.legends.api.core.leaderboard.service;

import gg.warcraft.legends.api.entity.player.LegendsStatistic;

import java.util.UUID;

public interface LeaderboardCommandService {

    void updateEntry(LegendsStatistic statistic, UUID playerId, int value);

    void publishLeaderboard(LegendsStatistic statistic);
}
