package gg.warcraft.legends.api.item.crate.config;

import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.item.crate.LootCrateDropType;

import java.util.Map;

public interface LootCrateDropTableConfiguration {

    LootCrateDropType getType();

    float getChance();

    Map<ItemRarity, Float> getDistribution();
}
