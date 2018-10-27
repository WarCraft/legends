package gg.warcraft.legends.app.item;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.legends.api.item.ConsumableBuilder;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.monolith.api.item.Item;
import gg.warcraft.monolith.api.item.ItemBuilder;
import gg.warcraft.monolith.api.item.ItemBuilderFactory;
import gg.warcraft.monolith.api.item.ItemType;

public class SimpleConsumableBuilder implements ConsumableBuilder {
    private static final String CONSUMABLE_TYPE = "Consumable";

    private final ItemBuilderFactory itemBuilderFactory;
    private final ItemType itemType;
    private final String name;
    private final ItemRarity rarity;

    @Inject
    public SimpleConsumableBuilder(ItemBuilderFactory itemBuilderFactory, @Assisted ItemType itemType,
                                   @Assisted String name, @Assisted ItemRarity rarity) {
        this.itemBuilderFactory = itemBuilderFactory;
        this.itemType = itemType;
        this.name = name;
        this.rarity = rarity;
    }

    @Override
    public Item build() {
        ItemBuilder builder = itemBuilderFactory.createItemBuilder(itemType, rarity.getColor() + name);
        builder.addLore(CONSUMABLE_TYPE);
        return builder.build();
    }
}
