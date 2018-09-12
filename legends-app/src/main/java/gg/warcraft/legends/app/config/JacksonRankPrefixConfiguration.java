package gg.warcraft.legends.app.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gg.warcraft.legends.api.config.RankPrefixConfiguration;
import gg.warcraft.monolith.api.util.ColorCode;

public class JacksonRankPrefixConfiguration implements RankPrefixConfiguration {
    private final String name;
    private final ColorCode color;

    @JsonCreator
    public JacksonRankPrefixConfiguration(@JsonProperty("name") String name,
                                          @JsonProperty("color") ColorCode color) {
        this.name = name;
        this.color = color;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ColorCode getColor() {
        return color;
    }
}
