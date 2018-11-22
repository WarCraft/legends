package gg.warcraft.legends.app.item.equipment.upgrade;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.item.equipment.upgrade.EquipmentUpgradeBuilder;
import gg.warcraft.monolith.api.item.Item;
import gg.warcraft.monolith.api.item.ItemBuilder;
import gg.warcraft.monolith.api.item.ItemBuilderFactory;
import gg.warcraft.monolith.api.item.ItemType;
import gg.warcraft.monolith.api.util.FormatCode;

import java.util.HashMap;
import java.util.Map;

public class SimpleEquipmentUpgradeBuilder implements EquipmentUpgradeBuilder {
    private final ItemBuilderFactory itemBuilderFactory;
    private final ItemRarity rarity;
    private final String name;

    private Map<LegendsAttribute, Integer> attributes;

    @Inject
    public SimpleEquipmentUpgradeBuilder(ItemBuilderFactory itemBuilderFactory,
                                         @Assisted ItemRarity rarity, @Assisted String name) {
        this.itemBuilderFactory = itemBuilderFactory;
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
        ItemBuilder itemBuilder = itemBuilderFactory.createItemBuilder(ItemType.PAPER, rarity.getColor() + name);

        // set type at top of tooltip
        itemBuilder.addLore("Equipment Upgrade");

        // add stats
        if (!attributes.keySet().isEmpty()) {
            itemBuilder.addLore("");
            for (Map.Entry<LegendsAttribute, Integer> entry : attributes.entrySet()) {
                itemBuilder.addLore("+" + entry.getValue() + " " + entry.getKey().getName());
            }
        }

        // add flavor text
        itemBuilder.addLore("");
        itemBuilder.addLore(FormatCode.ITALIC + "[RIGHT CLICK] While holding a piece of equipment");
        itemBuilder.addLore(FormatCode.ITALIC + "in your main hand and this equipment upgrade in");
        itemBuilder.addLore(FormatCode.ITALIC + "your offhand to use.");

        return itemBuilder.build();
    }
}
