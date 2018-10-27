package gg.warcraft.legends.app.item.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.legends.api.item.config.AttributeRollConfiguration;

public class JacksonAttributeRollConfiguration implements AttributeRollConfiguration {
    private final LegendsAttribute attribute;
    private final int lowRoll;
    private final int highRoll;

    @JsonCreator
    public JacksonAttributeRollConfiguration(@JsonProperty("attribute") LegendsAttribute attribute,
                                             @JsonProperty("low") int lowRoll,
                                             @JsonProperty("high") int highRoll) {
        this.attribute = attribute;
        this.lowRoll = lowRoll;
        this.highRoll = highRoll;
    }

    @Override
    public LegendsAttribute getAttribute() {
        return attribute;
    }

    @Override
    public int getLowRoll() {
        return lowRoll;
    }

    @Override
    public int getHighRoll() {
        return highRoll;
    }
}
