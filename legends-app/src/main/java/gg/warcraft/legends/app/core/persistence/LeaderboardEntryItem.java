package gg.warcraft.legends.app.core.persistence;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LeaderboardEntryItem {
    private final String name;
    private final int score;
    private final String avatar;

    @JsonCreator
    public LeaderboardEntryItem(@JsonProperty("name") String name,
                                @JsonProperty("score") int score,
                                @JsonProperty("avatar") String avatar) {
        this.name = name;
        this.score = score;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String getAvatar() {
        return avatar;
    }
}
