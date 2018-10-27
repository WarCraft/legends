package gg.warcraft.legends.api.item.equipment;

import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.monolith.api.item.ItemType;

public interface EquipmentBuilderFactory {

    EquipmentBuilder createEquipmentBuilder(ItemType type, ItemRarity rarity, String name);
}
