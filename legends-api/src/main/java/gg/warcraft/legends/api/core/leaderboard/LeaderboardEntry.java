package gg.warcraft.legends.api.core.leaderboard;

import java.util.UUID;

public interface LeaderboardEntry {

    UUID getPlayerId();

    int getRank();

    int getScore();
}
