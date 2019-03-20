package gg.warcraft.legends.app.core.leaderboard;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class MojangProfile {
    private final String id;
    private final String name;
    private final List<MojangProperty> properties;

    @JsonCreator
    public MojangProfile(@JsonProperty("id") String id,
                         @JsonProperty("name") String name,
                         @JsonProperty("properties") List<MojangProperty> properties) {
        this.id = id;
        this.name = name;
        this.properties = properties;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<MojangProperty> getProperties() {
        return new ArrayList<>(properties);
    }
}
