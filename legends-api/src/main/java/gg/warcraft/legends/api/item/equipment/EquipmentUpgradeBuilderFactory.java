package gg.warcraft.legends.api.item.equipment;

import gg.warcraft.legends.api.item.ItemRarity;

public interface EquipmentUpgradeBuilderFactory {

    EquipmentUpgradeBuilder createEquipmentUpgradeBuilder(ItemRarity rarity, String name);
}
