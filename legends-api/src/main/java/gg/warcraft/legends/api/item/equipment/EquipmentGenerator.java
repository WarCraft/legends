package gg.warcraft.legends.api.item.equipment;

import gg.warcraft.legends.api.combat.LegendsSpecializationType;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.monolith.api.item.Item;

public interface EquipmentGenerator {

    Item generate(LegendsSpecializationType specialization, EquipmentType type, LegendsTeam team);

    Item generate(LegendsSpecializationType specialization, EquipmentType type, ItemRarity rarity, LegendsTeam team);
}
