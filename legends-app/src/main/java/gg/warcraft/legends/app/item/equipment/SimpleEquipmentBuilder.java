package gg.warcraft.legends.app.item.equipment;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.item.equipment.EquipmentBuilder;
import gg.warcraft.monolith.api.util.FormatCode;
import gg.warcraft.monolith.api.world.item.Item;
import gg.warcraft.monolith.api.world.item.ItemFactory;
import gg.warcraft.monolith.api.world.item.ItemType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleEquipmentBuilder implements EquipmentBuilder {
    private final ItemFactory itemFactory;
    private final ItemType type;
    private final ItemRarity rarity;
    private final String name;

    private Map<LegendsAttribute, Integer> attributes;
    private int availableUpgrades;
    private int upgradeCount;
    private List<String> flavorText;

    @Inject
    public SimpleEquipmentBuilder(ItemFactory itemFactory, @Assisted ItemType type,
                                  @Assisted ItemRarity rarity, @Assisted String name) {
        this.itemFactory = itemFactory;
        this.type = type;
        this.rarity = rarity;
        this.name = name;
        this.attributes = new HashMap<>();
        this.availableUpgrades = 0;
        this.upgradeCount = 0;
        this.flavorText = new ArrayList<>();
    }

    @Override
    public EquipmentBuilder addFlavorText(String flavorText) {
        this.flavorText.add(flavorText);
        return this;
    }

    @Override
    public EquipmentBuilder withAttribute(LegendsAttribute attribute, int amount) {
        this.attributes.put(attribute, amount);
        return this;
    }

    @Override
    public EquipmentBuilder withAttributes(Map<LegendsAttribute, Integer> attributes) {
        this.attributes = attributes;
        return this;
    }

    @Override
    public EquipmentBuilder withAvailableUpgrades(int count) {
        this.availableUpgrades = count;
        return this;
    }

    @Override
    public EquipmentBuilder withUpgradeCount(int count) {
        this.upgradeCount = count;
        return this;
    }

    @Override
    public EquipmentBuilder withFlavorText(List<String> flavorText) {
        this.flavorText = flavorText;
        return this;
    }

    @Override
    public Item build() {
        String upgradedName = upgradeCount > 0
                ? name + " (+" + upgradeCount + ")"
                : name;
        Item item = itemFactory.create(type).withName(rarity.getColor() + upgradedName);

        // set type at top of tooltip
        item = item.addTooltip("Equipment");

        // add stats
        if (!attributes.keySet().isEmpty()) {
            item = item.addTooltip("");
            for (Map.Entry<LegendsAttribute, Integer> entry : attributes.entrySet()) {
                if (entry.getValue() != 0) {
                    item = item.addTooltip("+" + entry.getValue() + " " + entry.getKey().getName());
                }
            }
        }

        // add upgrades
        if (availableUpgrades > 0) {
            item = item.addTooltip("");
            item = item.addTooltip(availableUpgrades + " Available Upgrades");
        }

        // add flavor text
        if (!flavorText.isEmpty()) {
            item = item.addTooltip("");
            for (String text : flavorText) {
                item = item.addTooltip(FormatCode.ITALIC + text);
            }
        }

        return item;
    }
}
