package gg.warcraft.legends.app.core.persistence;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.SortedMap;

public class WarItem extends GameItem {
    private final Map<String, Integer> warScore;
    private final SortedMap<String, Map<String, Integer>> dailyPoints;

    @JsonCreator
    public WarItem(@JsonProperty("id") int id,
                   @JsonProperty("type") String type,
                   @JsonProperty("points") Map<String, Integer> points,
                   @JsonProperty("warScore") Map<String, Integer> warScore,
                   @JsonProperty("dailyPoints") SortedMap<String, Map<String, Integer>> dailyPoints,
                   @JsonProperty("objectives") Map<String, String> objectives,
                   @JsonProperty("timer") int timer,
                   @JsonProperty("startTime") String startTime,
                   @JsonProperty("endTime") String endTime) {
        super(id, type, points, objectives, timer, startTime, endTime);
        this.warScore = warScore;
        this.dailyPoints = dailyPoints;
    }

    public Map<String, Integer> getWarScore() {
        return warScore;
    }

    public SortedMap<String, Map<String, Integer>> getDailyPoints() {
        return dailyPoints;
    }
}
