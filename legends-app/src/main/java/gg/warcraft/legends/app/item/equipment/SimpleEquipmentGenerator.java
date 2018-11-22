package gg.warcraft.legends.app.item.equipment;

import com.google.inject.Inject;
import gg.warcraft.legends.api.combat.LegendsSpecializationType;
import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.item.AttributeDistribution;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.item.config.AttributeRollConfiguration;
import gg.warcraft.legends.api.item.config.EquipmentConfiguration;
import gg.warcraft.legends.api.item.config.EquipmentLoreConfiguration;
import gg.warcraft.legends.api.item.config.service.EquipmentConfigurationRepository;
import gg.warcraft.legends.api.item.equipment.EquipmentBuilder;
import gg.warcraft.legends.api.item.equipment.EquipmentBuilderFactory;
import gg.warcraft.legends.api.item.equipment.EquipmentGenerator;
import gg.warcraft.legends.api.item.equipment.EquipmentType;
import gg.warcraft.monolith.api.item.Item;
import gg.warcraft.monolith.api.item.ItemType;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SimpleEquipmentGenerator implements EquipmentGenerator {
    private final EquipmentConfiguration equipmentConfiguration;
    private final EquipmentBuilderFactory equipmentBuilderFactory;
    private final Random random;

    @Inject
    public SimpleEquipmentGenerator(EquipmentConfigurationRepository equipmentConfigurationRepository,
                                    EquipmentBuilderFactory equipmentBuilderFactory, Random random) {
        this.equipmentConfiguration = equipmentConfigurationRepository.getConfiguration();
        this.equipmentBuilderFactory = equipmentBuilderFactory;
        this.random = random;
    }

    private AttributeDistribution getRandomAttributeDistribution() {
        AttributeDistribution[] values = AttributeDistribution.values();
        return values[random.nextInt(values.length)];
    }

    private EquipmentType getRandomEquipmentTypeFor(LegendsSpecializationType specialization) {
        List<EquipmentType> validTypes = equipmentConfiguration.getValidTypes().get(specialization);
        int typeRoll = random.nextInt(validTypes.size());
        return validTypes.get(typeRoll);
    }

    private Map<LegendsSpecializationType, Map<AttributeDistribution, List<List<AttributeRollConfiguration>>>>
    getAttributeConfigurationsFor(ItemRarity rarity) {
        switch (rarity) {
            case LEGENDARY:
                return equipmentConfiguration.getLegendaryRolls();
            case ANCIENT:
                return equipmentConfiguration.getAncientRolls();
            case COMMON:
            case RARE:
            case EPIC:
            default:
                return equipmentConfiguration.getAttributeRolls();
        }
    }

    private Map<LegendsSpecializationType, Map<AttributeDistribution, List<List<AttributeRollConfiguration>>>>
    getOffhandAttributeConfigurationsFor(ItemRarity rarity) {
        switch (rarity) {
            case LEGENDARY:
                return equipmentConfiguration.getOffhandLegendaryRolls();
            case ANCIENT:
                return equipmentConfiguration.getOffhandAncientRolls();
            case COMMON:
            case RARE:
            case EPIC:
            default:
                return equipmentConfiguration.getOffhandAttributeRolls();
        }
    }

    private int getRandomRollFor(List<AttributeRollConfiguration> attributeRolls, ItemRarity rarity) {
        int totalLowRoll = 0;
        int totalHighRoll = 0;
        for (AttributeRollConfiguration roll : attributeRolls) {
            totalLowRoll += roll.getLowRoll();
            totalHighRoll += roll.getHighRoll();
        }

        int deltaRoll = totalHighRoll - totalLowRoll;
        int halfDeltaRoll = (int) (0.5f * deltaRoll);
        switch (rarity) {
            case COMMON:
                return totalLowRoll + random.nextInt(halfDeltaRoll + 1);
            case RARE:
                int randomHalfDeltaRoll = halfDeltaRoll > 1
                        ? random.nextInt(halfDeltaRoll - 1)
                        : 0;
                return totalHighRoll - 1 - randomHalfDeltaRoll;
            default:
                return totalHighRoll;
        }
    }

    private Map<LegendsAttribute, Integer> getRandomAttributesFor(List<AttributeRollConfiguration> attributeRolls,
                                                                  int roll) {
        Map<LegendsAttribute, Integer> randomAttributeRolls = new LinkedHashMap<>();
        Map<LegendsAttribute, Integer> highRolls = new HashMap<>();
        int remainingRoll = roll;
        for (AttributeRollConfiguration attr : attributeRolls) {
            randomAttributeRolls.put(attr.getAttribute(), attr.getLowRoll());
            highRolls.put(attr.getAttribute(), attr.getHighRoll());
            remainingRoll -= attr.getLowRoll();
        }
        while (remainingRoll > 0) {
            LegendsAttribute randomAttribute = attributeRolls.get(random.nextInt(attributeRolls.size())).getAttribute();
            int currentRoll = randomAttributeRolls.get(randomAttribute);
            int maxRoll = highRolls.get(randomAttribute);
            if (currentRoll < maxRoll) {
                int newRoll = currentRoll + 1;
                randomAttributeRolls.put(randomAttribute, newRoll);
                remainingRoll -= 1;
            }
        }
        return randomAttributeRolls;
    }

    @Override
    public Item generate(LegendsSpecializationType specialization, LegendsTeam team) {
        EquipmentType type = getRandomEquipmentTypeFor(specialization);
        return generate(specialization, type, team);
    }

    @Override
    public Item generate(LegendsSpecializationType specialization, EquipmentType type, LegendsTeam team) {
        float rarityRoll = random.nextFloat() * 100;
        ItemRarity itemRarity = equipmentConfiguration.getItemRarities().floorEntry(rarityRoll).getValue();
        return generate(specialization, type, itemRarity, team);
    }

    @Override
    public Item generate(LegendsSpecializationType specialization, ItemRarity rarity, LegendsTeam team) {
        EquipmentType type = getRandomEquipmentTypeFor(specialization);
        return generate(specialization, type, rarity, team);
    }

    @Override
    public Item generate(LegendsSpecializationType specialization, EquipmentType type, ItemRarity rarity,
                         LegendsTeam team) {
        // TODO remove when Ancient is ready
        if (rarity == ItemRarity.ANCIENT) {
            rarity = ItemRarity.LEGENDARY;
        }

        AttributeDistribution attributeDistribution = getRandomAttributeDistribution();
        ItemType itemType = equipmentConfiguration.getItemTypes().get(type).get(rarity);

        List<EquipmentLoreConfiguration> loreConfigurations = equipmentConfiguration.getItemLores().get(specialization)
                .get(team).get(attributeDistribution).get(type).get(rarity);
        EquipmentLoreConfiguration loreConfiguration =
                loreConfigurations.get(random.nextInt(loreConfigurations.size()));

        String itemName = loreConfiguration.getName();
        List<String> itemLore = loreConfiguration.getFlavorText();
        EquipmentBuilder builder = equipmentBuilderFactory.createEquipmentBuilder(itemType, rarity, itemName);
        if (itemLore != null && !itemLore.isEmpty()) {
            builder.withFlavorText(itemLore);
        }

        List<List<AttributeRollConfiguration>> attributeRolls;
        if (equipmentConfiguration.getOffhandTypes().contains(type)) {
            attributeRolls = getOffhandAttributeConfigurationsFor(rarity).get(specialization).get(attributeDistribution);
        } else {
            attributeRolls = getAttributeConfigurationsFor(rarity).get(specialization).get(attributeDistribution);
        }
        List<AttributeRollConfiguration> attributeRoll = attributeRolls.get(random.nextInt(attributeRolls.size()));
        int randomRoll = getRandomRollFor(attributeRoll, rarity);
        Map<LegendsAttribute, Integer> randomAttributeRoll = getRandomAttributesFor(attributeRoll, randomRoll);
        builder.withAttributes(randomAttributeRoll);

        builder.withAvailableUpgrades(random.nextInt(4));
        return builder.build();
    }
}
