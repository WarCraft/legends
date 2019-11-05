package gg.warcraft.legends.app.item;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.legends.api.item.ConsumableBuilder;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.monolith.api.world.item.Item;
import gg.warcraft.monolith.api.world.item.ItemFactory;
import gg.warcraft.monolith.api.world.item.ItemType;

public class SimpleConsumableBuilder implements ConsumableBuilder {
    private static final String CONSUMABLE_TYPE = "Consumable";

    private final ItemFactory itemFactory;
    private final ItemType itemType;
    private final String name;
    private final ItemRarity rarity;

    @Inject
    public SimpleConsumableBuilder(ItemFactory itemFactory, @Assisted ItemType itemType,
                                   @Assisted String name, @Assisted ItemRarity rarity) {
        this.itemFactory = itemFactory;
        this.itemType = itemType;
        this.name = name;
        this.rarity = rarity;
    }

    @Override
    public Item build() {
        Item item = itemFactory.create(itemType).withName(rarity.getColor() + name);
        item = item.addTooltip(CONSUMABLE_TYPE);
        return item;
    }
}
