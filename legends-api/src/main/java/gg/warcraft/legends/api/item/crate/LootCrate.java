package gg.warcraft.legends.api.item.crate;

import com.google.common.collect.ImmutableSortedMap;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.monolith.api.world.item.ItemType;

public interface LootCrate {

    String getName();

    LootCrateType getType();

    ItemRarity getRarity();

    ItemType getItemType();

    ImmutableSortedMap<Float, LootCrateDropTable> viewDropTable();
}
