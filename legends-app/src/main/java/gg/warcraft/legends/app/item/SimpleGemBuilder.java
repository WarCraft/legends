package gg.warcraft.legends.app.item;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.legends.api.item.GemBuilder;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.monolith.api.item.Item;
import gg.warcraft.monolith.api.item.ItemBuilder;
import gg.warcraft.monolith.api.item.ItemBuilderFactory;
import gg.warcraft.monolith.api.item.ItemType;
import gg.warcraft.monolith.api.util.ColorCode;

import java.util.HashMap;
import java.util.Map;

public class SimpleGemBuilder implements GemBuilder {
    private final ItemBuilderFactory itemBuilderFactory;
    private final ItemType type;
    private final ItemRarity rarity;
    private final String name;

    private Map<LegendsAttribute, Integer> attributes;
    private int successChance;
    private int breakChance;

    @Inject
    public SimpleGemBuilder(ItemBuilderFactory itemBuilderFactory, @Assisted ItemRarity rarity,
                            @Assisted String name) {
        this.itemBuilderFactory = itemBuilderFactory;
        this.type = getItemType(rarity);
        this.rarity = rarity;
        this.name = name;
        this.attributes = new HashMap<>();
        this.successChance = 0;
        this.breakChance = 0;
    }

    public ItemType getItemType(ItemRarity rarity) {
        switch (rarity) {
            case COMMON:
                return ItemType.MAGMA_CREAM;
            case RARE:
                return ItemType.ENDER_PEARL;
            case EPIC:
                return ItemType.EYE_OF_ENDER;
            case LEGENDARY:
            case ANCIENT:
                return ItemType.NETHER_STAR;
            default:
                throw new IllegalArgumentException("Failed to get gem item type for unknown rarity " + rarity);
        }
    }

    @Override
    public GemBuilder withAttribute(LegendsAttribute attribute, int amount) {
        this.attributes.put(attribute, amount);
        return this;
    }

    @Override
    public GemBuilder withAttributes(Map<LegendsAttribute, Integer> attributes) {
        this.attributes = attributes;
        return this;
    }

    @Override
    public GemBuilder withSuccessChance(int chance) {
        this.successChance = chance;
        return this;
    }

    @Override
    public GemBuilder withBreakChance(int chance) {
        this.breakChance = chance;
        return this;
    }

    @Override
    public Item build() {
        ItemBuilder itemBuilder = itemBuilderFactory.createItemBuilder(type, rarity.getColor() + name);

        // set type at top of tooltip
        itemBuilder.addLore(ColorCode.GRAY + "Upgrade Gem");

        // add success stats
        if (!attributes.keySet().isEmpty()) {
            itemBuilder.addLore("").addLore(successChance + "% chance to upgrade equipment with:");
            for (Map.Entry<LegendsAttribute, Integer> entry : attributes.entrySet()) {
                itemBuilder.addLore("+" + entry.getValue() + " " + entry.getKey().getName());
            }
        }

        // add break chance
        if (breakChance > 0) {
            itemBuilder.addLore("")
                    .addLore(breakChance + "% chance to destroy the equipment")
                    .addLore("when the upgrade fails.");
        }

        // add tutorial
        itemBuilder.addLore("")
                .addLore("[SHIFT + RIGHT CLICK] an equipment to open")
                .addLore("its socket window and add this gem to it.");

        return itemBuilder.build();
    }
}
