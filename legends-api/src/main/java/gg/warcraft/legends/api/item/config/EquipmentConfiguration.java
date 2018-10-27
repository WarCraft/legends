package gg.warcraft.legends.api.item.config;

import gg.warcraft.legends.api.combat.LegendsSpecializationType;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.item.AttributeDistribution;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.item.equipment.EquipmentType;
import gg.warcraft.monolith.api.item.ItemType;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

public interface EquipmentConfiguration {

    NavigableMap<Float, ItemRarity> getItemRarities();

    Map<EquipmentType, Map<ItemRarity, ItemType>> getItemTypes();

    Map<LegendsTeam, Map<AttributeDistribution, List<List<AttributeRollConfiguration>>>> getAttributeRolls();

    Map<LegendsTeam, Map<AttributeDistribution, List<List<AttributeRollConfiguration>>>> getLegendaryRolls();

    Map<LegendsTeam, Map<AttributeDistribution, List<List<AttributeRollConfiguration>>>> getAncientRolls();

    Map<LegendsSpecializationType, Map<LegendsTeam, Map<AttributeDistribution, Map<EquipmentType, Map<ItemRarity, EquipmentLoreConfiguration>>>>> getItemLores();
}
