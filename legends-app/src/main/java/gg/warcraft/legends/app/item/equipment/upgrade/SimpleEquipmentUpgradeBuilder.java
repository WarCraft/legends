package gg.warcraft.legends.app.item.equipment.upgrade;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.item.equipment.upgrade.EquipmentUpgradeBuilder;
import gg.warcraft.monolith.api.util.FormatCode;
import gg.warcraft.monolith.api.world.item.Item;
import gg.warcraft.monolith.api.world.item.ItemFactory;
import gg.warcraft.monolith.api.world.item.ItemType;

import java.util.HashMap;
import java.util.Map;

public class SimpleEquipmentUpgradeBuilder implements EquipmentUpgradeBuilder {
    private final ItemFactory itemFactory;
    private final ItemRarity rarity;
    private final String name;

    private Map<LegendsAttribute, Integer> attributes;

    @Inject
    public SimpleEquipmentUpgradeBuilder(ItemFactory itemFactory,
                                         @Assisted ItemRarity rarity, @Assisted String name) {
        this.itemFactory = itemFactory;
        this.rarity = rarity;
        this.name = name;
        this.attributes = new HashMap<>();
    }

    @Override
    public EquipmentUpgradeBuilder withAttribute(LegendsAttribute attribute, int amount) {
        this.attributes.put(attribute, amount);
        return this;
    }

    @Override
    public EquipmentUpgradeBuilder withAttributes(Map<LegendsAttribute, Integer> attributes) {
        this.attributes = attributes;
        return this;
    }

    @Override
    public Item build() {
        Item item = itemFactory.create(ItemType.PAPER).withName(rarity.getColor() + name);

        // set type at top of tooltip
        item = item.addTooltip("Equipment Upgrade");

        // add stats
        if (!attributes.keySet().isEmpty()) {
            item = item.addTooltip("");
            for (Map.Entry<LegendsAttribute, Integer> entry : attributes.entrySet()) {
                item = item.addTooltip("+" + entry.getValue() + " " + entry.getKey().getName());
            }
        }

        // add flavor text
        item = item.addTooltip("");
        item = item.addTooltip(FormatCode.ITALIC + "[RIGHT CLICK] While holding a piece of equipment");
        item = item.addTooltip(FormatCode.ITALIC + "in your main hand and this equipment upgrade in");
        item = item.addTooltip(FormatCode.ITALIC + "your offhand to use.");

        return item;
    }
}
