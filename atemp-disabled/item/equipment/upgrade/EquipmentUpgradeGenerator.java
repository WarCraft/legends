package gg.warcraft.legends.api.item.equipment.upgrade;

import gg.warcraft.legends.api.combat.LegendsSpecializationType;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.monolith.api.world.item.Item;

public interface EquipmentUpgradeGenerator {

    Item generate(LegendsSpecializationType specialization, ItemRarity rarity);
}
