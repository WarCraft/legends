package gg.warcraft.legends.app.item.equipment;

import com.google.inject.Inject;
import gg.warcraft.legends.api.combat.LegendsSpecializationType;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.item.AttributeDistribution;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.item.config.AttributeRollConfiguration;
import gg.warcraft.legends.api.item.config.EquipmentConfiguration;
import gg.warcraft.legends.api.item.config.EquipmentLoreConfiguration;
import gg.warcraft.legends.api.item.config.service.ItemConfigurationRepository;
import gg.warcraft.legends.api.item.equipment.EquipmentBuilder;
import gg.warcraft.legends.api.item.equipment.EquipmentBuilderFactory;
import gg.warcraft.legends.api.item.equipment.EquipmentGenerator;
import gg.warcraft.legends.api.item.equipment.EquipmentType;
import gg.warcraft.monolith.api.item.Item;
import gg.warcraft.monolith.api.item.ItemType;

import java.util.List;
import java.util.Random;

public class SimpleEquipmentGenerator implements EquipmentGenerator {
    private final EquipmentConfiguration equipmentConfiguration;
    private final EquipmentBuilderFactory equipmentBuilderFactory;
    private final Random random;

    @Inject
    public SimpleEquipmentGenerator(ItemConfigurationRepository itemConfigurationRepository,
                                    EquipmentBuilderFactory equipmentBuilderFactory, Random random) {
        this.equipmentConfiguration = itemConfigurationRepository.getConfiguration();
        this.equipmentBuilderFactory = equipmentBuilderFactory;
        this.random = random;
    }

    private AttributeDistribution getRandomAttributeDistribution() {
        AttributeDistribution[] values = AttributeDistribution.values();
        return values[random.nextInt(values.length)];
    }

    @Override
    public Item generate(LegendsSpecializationType specialization, EquipmentType type, LegendsTeam team) {
        float rarityRoll = random.nextFloat() * 100;
        ItemRarity itemRarity = equipmentConfiguration.getItemRarities().floorEntry(rarityRoll).getValue();
        return generate(specialization, type, itemRarity, team);
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
        EquipmentLoreConfiguration loreConfiguration = equipmentConfiguration.getItemLores().get(specialization).get(team).get(attributeDistribution)
                .get(type).get(rarity);
        String itemName = loreConfiguration.getName();
        List<String> itemLore = loreConfiguration.getFlavorText();
        EquipmentBuilder builder = equipmentBuilderFactory.createEquipmentBuilder(itemType, rarity, itemName);
        if (itemLore != null && !itemLore.isEmpty()) {
            builder.withFlavorText(itemLore);
        }

        List<List<AttributeRollConfiguration>> attributeRolls =
                equipmentConfiguration.getLegendaryRolls().get(team).get(attributeDistribution);
        List<AttributeRollConfiguration> attributeRoll = attributeRolls.get(random.nextInt(attributeRolls.size()));
        attributeRoll.forEach(roll -> {
            int lowRoll = roll.getLowRoll();
            int highRoll = roll.getHighRoll();
            int rollDelta = highRoll - lowRoll + 1;
            int rollRoll = random.nextInt(rollDelta);
            builder.withAttribute(roll.getAttribute(), lowRoll + rollRoll);
        });

        return builder.build();
    }
}
