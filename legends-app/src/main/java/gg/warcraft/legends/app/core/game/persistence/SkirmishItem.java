package gg.warcraft.legends.app.core.game.persistence;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class SkirmishItem extends GameItem {

    @JsonCreator
    public SkirmishItem(@JsonProperty("id") int id,
                        @JsonProperty("type") String type,
                        @JsonProperty("points") Map<String, Integer> points,
                        @JsonProperty("objectives") Map<String, String> objectives,
                        @JsonProperty("timer") int timer,
                        @JsonProperty("startTime") String startTime,
                        @JsonProperty("endTime") String endTime) {
        super(id, type, points, objectives, timer, startTime, endTime);
    }
}
