package gg.warcraft.legends.app.item.crate.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.item.crate.LootCrateDropType;
import gg.warcraft.legends.api.item.crate.config.LootCrateDropTableConfiguration;

import java.util.HashMap;
import java.util.Map;

public class JacksonLootCrateDropTableConfiguration implements LootCrateDropTableConfiguration {
    private final LootCrateDropType type;
    private final float chance;
    private final Map<ItemRarity, Float> distribution;

    @JsonCreator
    public JacksonLootCrateDropTableConfiguration(@JsonProperty("type") LootCrateDropType type,
                                                  @JsonProperty("chance") float chance,
                                                  @JsonProperty("distribution") Map<ItemRarity, Float> distribution) {
        this.type = type;
        this.chance = chance;
        this.distribution = distribution;
    }

    @Override
    public LootCrateDropType getType() {
        return type;
    }

    @Override
    public float getChance() {
        return chance;
    }

    @Override
    public Map<ItemRarity, Float> getDistribution() {
        return new HashMap<>(distribution);
    }
}
