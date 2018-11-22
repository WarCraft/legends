package gg.warcraft.legends.api.item.equipment.upgrade;

import gg.warcraft.legends.api.item.ItemRarity;

public interface EquipmentUpgradeBuilderFactory {

    EquipmentUpgradeBuilder createEquipmentUpgradeBuilder(ItemRarity rarity, String name);
}
