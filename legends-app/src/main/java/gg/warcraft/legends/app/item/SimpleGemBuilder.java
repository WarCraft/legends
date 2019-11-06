package gg.warcraft.legends.app.item;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.legends.api.item.GemBuilder;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.monolith.api.util.ColorCode;
import gg.warcraft.monolith.api.world.item.Item;
import gg.warcraft.monolith.api.world.item.ItemService;
import gg.warcraft.monolith.api.world.item.ItemType;

import java.util.HashMap;
import java.util.Map;

public class SimpleGemBuilder implements GemBuilder {
    private final ItemService itemService;
    private final ItemType type;
    private final ItemRarity rarity;
    private final String name;

    private Map<LegendsAttribute, Integer> attributes;
    private int successChance;
    private int breakChance;

    @Inject
    public SimpleGemBuilder(ItemService itemService, @Assisted ItemRarity rarity,
                            @Assisted String name) {
        this.itemService = itemService;
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
                return ItemType.ENDER_EYE;
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
        Item item = itemService.create(type).withName(rarity.getColor() + name);

        // set type at top of tooltip
        item = item.addTooltip(ColorCode.GRAY + "Upgrade Gem");

        // add success stats
        if (!attributes.keySet().isEmpty()) {
            item = item.addTooltip("").addTooltip(successChance + "% chance to upgrade equipment with:");
            for (Map.Entry<LegendsAttribute, Integer> entry : attributes.entrySet()) {
                item = item.addTooltip("+" + entry.getValue() + " " + entry.getKey().getName());
            }
        }

        // add break chance
        if (breakChance > 0) {
            item = item.addTooltip("")
                    .addTooltip(breakChance + "% chance to destroy the equipment")
                    .addTooltip("when the upgrade fails.");
        }

        // add tutorial
        item = item.addTooltip("")
                .addTooltip("[SHIFT + RIGHT CLICK] an equipment to open")
                .addTooltip("its socket window and add this gem to it.");

        return item;
    }
}
