package gg.warcraft.legends.app.item.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gg.warcraft.legends.api.item.config.EquipmentLoreConfiguration;

import java.util.List;

public class JacksonEquipmentLoreConfiguration implements EquipmentLoreConfiguration {
    private final String name;
    private final List<String> flavorText;

    @JsonCreator
    public JacksonEquipmentLoreConfiguration(@JsonProperty("name") String name,
                                             @JsonProperty("flavorText") List<String> flavorText) {
        this.name = name;
        this.flavorText = flavorText;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getFlavorText() {
        return flavorText;
    }
}
