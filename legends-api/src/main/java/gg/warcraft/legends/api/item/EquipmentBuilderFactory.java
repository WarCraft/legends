package gg.warcraft.legends.api.item;

import gg.warcraft.monolith.api.item.ItemType;

public interface EquipmentBuilderFactory {

    EquipmentBuilder createEquipmentBuilder(ItemType type, ItemRarity rarity, String name);
}
