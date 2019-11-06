package gg.warcraft.legends.app.item;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.legends.api.item.ConsumableBuilder;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.monolith.api.world.item.Item;
import gg.warcraft.monolith.api.world.item.ItemService;
import gg.warcraft.monolith.api.world.item.ItemType;
import gg.warcraft.monolith.api.world.item.ItemTypeOrVariant;

public class SimpleConsumableBuilder implements ConsumableBuilder {
    private static final String CONSUMABLE_TYPE = "Consumable";

    private final ItemService itemService;
    private final ItemTypeOrVariant itemType;
    private final String name;
    private final ItemRarity rarity;

    @Inject
    public SimpleConsumableBuilder(ItemService itemService, @Assisted ItemTypeOrVariant itemType,
                                   @Assisted String name, @Assisted ItemRarity rarity) {
        this.itemService = itemService;
        this.itemType = itemType;
        this.name = name;
        this.rarity = rarity;
    }

    @Override
    public Item build() {
        Item item = itemService.create(itemType).withName(rarity.getColor() + name);
        item = item.addTooltip(CONSUMABLE_TYPE);
        return item;
    }
}
