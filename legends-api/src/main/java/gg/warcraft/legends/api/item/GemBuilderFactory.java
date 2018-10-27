package gg.warcraft.legends.api.item;

import gg.warcraft.legends.api.item.equipment.EquipmentBuilder;

public interface GemBuilderFactory {

    EquipmentBuilder createScrollBuilder(String name, ItemRarity rarity);
}
