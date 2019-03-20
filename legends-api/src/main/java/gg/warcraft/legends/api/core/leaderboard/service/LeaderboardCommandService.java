package gg.warcraft.legends.api.core.leaderboard.service;

import gg.warcraft.legends.api.core.leaderboard.Leaderboard;

import java.util.UUID;

public interface LeaderboardCommandService {

    void updateEntry(Leaderboard leaderboard, UUID playerId, int value);
}
