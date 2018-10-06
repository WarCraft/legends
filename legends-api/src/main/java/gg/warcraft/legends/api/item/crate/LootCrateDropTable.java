package gg.warcraft.legends.api.item.crate;

import com.google.common.collect.ImmutableSortedMap;
import gg.warcraft.legends.api.item.ItemRarity;

public interface LootCrateDropTable {

    LootCrateDropType getType();

    ImmutableSortedMap<Integer, ItemRarity> viewDistribution();
}
