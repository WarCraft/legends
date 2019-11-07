package gg.warcraft.legends.api.item.crate.config;

import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.item.crate.LootCrateType;
import gg.warcraft.monolith.api.world.item.ItemType;
import gg.warcraft.monolith.api.world.item.ItemTypeOrVariant;

import java.util.List;

public interface LootCrateConfiguration {

    String getName();

    LootCrateType getType();

    ItemRarity getRarity();

    String getItemType();

    List<LootCrateDropTableConfiguration> getDropTable();
}
