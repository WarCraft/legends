package gg.warcraft.legends.app.core.leaderboard;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MojangProperty {
    private final String name;
    private final String value;
    private final String signature;

    @JsonCreator
    public MojangProperty(@JsonProperty("name") String name,
                          @JsonProperty("value") String value,
                          @JsonProperty("signature") String signature) {
        this.name = name;
        this.value = value;
        this.signature = signature;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getSignature() {
        return signature;
    }
}
