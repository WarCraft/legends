package gg.warcraft.legends.app.item.equipment;

import com.google.common.base.CharMatcher;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.item.equipment.EquipmentBuilder;
import gg.warcraft.monolith.api.item.Item;
import gg.warcraft.monolith.api.item.ItemBuilder;
import gg.warcraft.monolith.api.item.ItemBuilderFactory;
import gg.warcraft.monolith.api.item.ItemType;
import gg.warcraft.monolith.api.util.FormatCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleEquipmentBuilder implements EquipmentBuilder {
    private static final char GEM_CHAR = '❂';

    private final ItemBuilderFactory itemBuilderFactory;
    private final ItemType type;
    private final ItemRarity rarity;
    private final String name;

    private Map<LegendsAttribute, Integer> attributes;
    private int availableSockets;
    private int brokenSockets;
    private List<String> flavorText;

    @Inject
    public SimpleEquipmentBuilder(ItemBuilderFactory itemBuilderFactory, @Assisted ItemType type,
                                  @Assisted ItemRarity rarity, @Assisted String name) {
        this.itemBuilderFactory = itemBuilderFactory;
        this.type = type;
        this.rarity = rarity;
        this.name = name;
        this.attributes = new HashMap<>();
        this.availableSockets = 0;
        this.brokenSockets = 0;
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
    public EquipmentBuilder withAvailableSockets(int sockets) {
        this.availableSockets = sockets;
        return this;
    }

    @Override
    public EquipmentBuilder withBrokenSockets(int sockets) {
        this.brokenSockets = sockets;
        return this;
    }

    @Override
    public EquipmentBuilder withFlavorText(List<String> flavorText) {
        this.flavorText = flavorText;
        return this;
    }

    @Override
    public Item build() {
        ItemBuilder itemBuilder = itemBuilderFactory.createItemBuilder(type, rarity.getColor() + name);

        // set type at top of tooltip
        itemBuilder.addLore("Equipment");

        // add stats
        if (!attributes.keySet().isEmpty()) {
            itemBuilder.addLore("");
            for (Map.Entry<LegendsAttribute, Integer> entry : attributes.entrySet()) {
                if (entry.getValue() != 0) {
                    itemBuilder.addLore("+" + entry.getValue() + " " + entry.getKey().getName());
                }
            }
        }

        // add sockets
        int usedSockets = CharMatcher.is(GEM_CHAR).countIn(name);
        if ((usedSockets + availableSockets + brokenSockets) > 0) {
            itemBuilder.addLore("");
            String availableSocketText = availableSockets == 1
                    ? "Available Socket"
                    : "Available Sockets";
            itemBuilder.addLore(availableSockets + " " + availableSocketText);
            String brokenSocketText = brokenSockets == 1
                    ? "Broken Socket"
                    : "Broken Sockets";
            itemBuilder.addLore(brokenSockets + " " + brokenSocketText);
        }

        // add flavor text
        if (!flavorText.isEmpty()) {
            itemBuilder.addLore("");
            for (String text : flavorText) {
                itemBuilder.addLore(FormatCode.ITALIC + text);
            }
        }

        return itemBuilder.build();
    }
}
