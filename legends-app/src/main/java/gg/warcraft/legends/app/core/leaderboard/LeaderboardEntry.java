package gg.warcraft.legends.app.core.leaderboard;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LeaderboardEntry {
    private final String name;
    private final int score;
    private final String avatarUrl;

    @JsonCreator
    public LeaderboardEntry(@JsonProperty("name") String name,
                            @JsonProperty("score") int score,
                            @JsonProperty("avatarUrl") String avatarUrl) {
        this.name = name;
        this.score = score;
        this.avatarUrl = avatarUrl;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
