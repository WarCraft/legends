package gg.warcraft.legends.api.item.crate;

import com.google.common.collect.ImmutableSortedMap;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.monolith.api.world.item.ItemType;
import gg.warcraft.monolith.api.world.item.ItemTypeOrVariant;

public interface LootCrate {

    String getName();

    LootCrateType getType();

    ItemRarity getRarity();

    ItemTypeOrVariant getItemType();

    ImmutableSortedMap<Float, LootCrateDropTable> viewDropTable();
}
