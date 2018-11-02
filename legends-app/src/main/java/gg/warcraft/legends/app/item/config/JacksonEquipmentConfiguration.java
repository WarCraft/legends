package gg.warcraft.legends.app.item.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gg.warcraft.legends.api.combat.LegendsSpecializationType;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.item.AttributeDistribution;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.item.config.AttributeRollConfiguration;
import gg.warcraft.legends.api.item.config.EquipmentConfiguration;
import gg.warcraft.legends.api.item.config.EquipmentLoreConfiguration;
import gg.warcraft.legends.api.item.equipment.EquipmentType;
import gg.warcraft.monolith.api.item.ItemType;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class JacksonEquipmentConfiguration implements EquipmentConfiguration {
    private final NavigableMap<Float, ItemRarity> itemRarities;
    private final Map<LegendsSpecializationType, List<EquipmentType>> validTypes;
    private final List<EquipmentType> offhandTypes;
    private final Map<EquipmentType, Map<ItemRarity, ItemType>> itemTypes;
    private final Map<LegendsSpecializationType, Map<AttributeDistribution, List<List<AttributeRollConfiguration>>>> attributeRolls;
    private final Map<LegendsSpecializationType, Map<AttributeDistribution, List<List<AttributeRollConfiguration>>>> legendaryRolls;
    private final Map<LegendsSpecializationType, Map<AttributeDistribution, List<List<AttributeRollConfiguration>>>> ancientRolls;
    private final Map<LegendsSpecializationType, Map<AttributeDistribution, List<List<AttributeRollConfiguration>>>> offhandAttributeRolls;
    private final Map<LegendsSpecializationType, Map<AttributeDistribution, List<List<AttributeRollConfiguration>>>> offhandLegendaryRolls;
    private final Map<LegendsSpecializationType, Map<AttributeDistribution, List<List<AttributeRollConfiguration>>>> offhandAncientRolls;
    private final Map<LegendsSpecializationType, Map<LegendsTeam, Map<AttributeDistribution, Map<EquipmentType, Map<ItemRarity, List<EquipmentLoreConfiguration>>>>>> itemLores;

    @JsonCreator
    public JacksonEquipmentConfiguration(@JsonProperty("itemRarities") TreeMap<Float, ItemRarity> itemRarities,
                                         @JsonProperty("validTypes") Map<LegendsSpecializationType, List<EquipmentType>> validTypes,
                                         @JsonProperty("offhandTypes") List<EquipmentType> offhandTypes,
                                         @JsonProperty("itemTypes") Map<EquipmentType, Map<ItemRarity, ItemType>> itemTypes,
                                         @JsonProperty("attributeRolls") Map<LegendsSpecializationType, Map<AttributeDistribution, List<List<AttributeRollConfiguration>>>> attributeRolls,
                                         @JsonProperty("legendaryRolls") Map<LegendsSpecializationType, Map<AttributeDistribution, List<List<AttributeRollConfiguration>>>> legendaryRolls,
                                         @JsonProperty("ancientRolls") Map<LegendsSpecializationType, Map<AttributeDistribution, List<List<AttributeRollConfiguration>>>> ancientRolls,
                                         @JsonProperty("offhandAttributeRolls") Map<LegendsSpecializationType, Map<AttributeDistribution, List<List<AttributeRollConfiguration>>>> offhandAttributeRolls,
                                         @JsonProperty("offhandLegendaryRolls") Map<LegendsSpecializationType, Map<AttributeDistribution, List<List<AttributeRollConfiguration>>>> offhandLegendaryRolls,
                                         @JsonProperty("offhandAncientRolls") Map<LegendsSpecializationType, Map<AttributeDistribution, List<List<AttributeRollConfiguration>>>> offhandAncientRolls,
                                         @JsonProperty("itemLores") Map<LegendsSpecializationType, Map<LegendsTeam, Map<AttributeDistribution, Map<EquipmentType, Map<ItemRarity, List<EquipmentLoreConfiguration>>>>>> itemLores) {
        this.itemRarities = itemRarities;
        this.validTypes = validTypes;
        this.offhandTypes = offhandTypes;
        this.itemTypes = itemTypes;
        this.attributeRolls = attributeRolls;
        this.legendaryRolls = legendaryRolls;
        this.ancientRolls = ancientRolls;
        this.offhandAttributeRolls = offhandAttributeRolls;
        this.offhandLegendaryRolls = offhandLegendaryRolls;
        this.offhandAncientRolls = offhandAncientRolls;
        this.itemLores = itemLores;
    }

    @Override
    public NavigableMap<Float, ItemRarity> getItemRarities() {
        return itemRarities;
    }

    @Override
    public Map<LegendsSpecializationType, List<EquipmentType>> getValidTypes() {
        return validTypes;
    }

    @Override
    public List<EquipmentType> getOffhandTypes() {
        return offhandTypes;
    }

    @Override
    public Map<EquipmentType, Map<ItemRarity, ItemType>> getItemTypes() {
        return itemTypes;
    }

    @Override
    public Map<LegendsSpecializationType, Map<AttributeDistribution, List<List<AttributeRollConfiguration>>>> getAttributeRolls() {
        return attributeRolls;
    }

    @Override
    public Map<LegendsSpecializationType, Map<AttributeDistribution, List<List<AttributeRollConfiguration>>>> getLegendaryRolls() {
        return legendaryRolls;
    }

    @Override
    public Map<LegendsSpecializationType, Map<AttributeDistribution, List<List<AttributeRollConfiguration>>>> getAncientRolls() {
        return ancientRolls;
    }

    @Override
    public Map<LegendsSpecializationType, Map<AttributeDistribution, List<List<AttributeRollConfiguration>>>> getOffhandAttributeRolls() {
        return offhandAttributeRolls;
    }

    @Override
    public Map<LegendsSpecializationType, Map<AttributeDistribution, List<List<AttributeRollConfiguration>>>> getOffhandLegendaryRolls() {
        return offhandLegendaryRolls;
    }

    @Override
    public Map<LegendsSpecializationType, Map<AttributeDistribution, List<List<AttributeRollConfiguration>>>> getOffhandAncientRolls() {
        return offhandAncientRolls;
    }

    @Override
    public Map<LegendsSpecializationType, Map<LegendsTeam, Map<AttributeDistribution, Map<EquipmentType, Map<ItemRarity, List<EquipmentLoreConfiguration>>>>>> getItemLores() {
        return itemLores;
    }
}
