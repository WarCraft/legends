package gg.warcraft.legends.app.core.leaderboard;

import gg.warcraft.legends.api.core.leaderboard.LeaderboardEntry;

import java.util.UUID;

public class SimpleLeaderboardEntry implements LeaderboardEntry {
    private final UUID playerId;
    private final int rank;
    private final int score;

    public SimpleLeaderboardEntry(UUID playerId, int rank, int score) {
        this.playerId = playerId;
        this.rank = rank;
        this.score = score;
    }

    @Override
    public UUID getPlayerId() {
        return playerId;
    }

    @Override
    public int getRank() {
        return rank;
    }

    @Override
    public int getScore() {
        return score;
    }
}
