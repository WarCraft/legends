package gg.warcraft.legends.api.item.equipment;

import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.monolith.api.world.item.ItemType;
import gg.warcraft.monolith.api.world.item.ItemTypeOrVariant;

public interface EquipmentBuilderFactory {

    EquipmentBuilder createEquipmentBuilder(ItemTypeOrVariant type, ItemRarity rarity, String name);
}
