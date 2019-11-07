package gg.warcraft.legends.app.item.crate;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.item.crate.LootCrate;
import gg.warcraft.legends.api.item.crate.LootCrateBuilder;
import gg.warcraft.legends.api.item.crate.LootCrateType;
import gg.warcraft.legends.api.item.crate.service.LootCrateRepository;
import gg.warcraft.monolith.api.world.item.Item;
import gg.warcraft.monolith.api.world.item.ItemService;
import gg.warcraft.monolith.api.world.item.ItemType;
import gg.warcraft.monolith.api.world.item.ItemTypeOrVariant;

public class SimpleLootCrateBuilder implements LootCrateBuilder {
    private final ItemService itemService;
    private final String name;
    private final ItemRarity rarity;
    private final ItemTypeOrVariant type;

    @Inject
    public SimpleLootCrateBuilder(LootCrateRepository lootCrateRepository,
                                  ItemService itemService,
                                  @Assisted LootCrateType type) {
        this.itemService = itemService;

        LootCrate crate = lootCrateRepository.get(type);
        if (crate == null) {
            throw new IllegalArgumentException("Failed to create loot crate builder for type " + type +
                    ", loot crate doesn't exist.");
        }
        this.name = crate.getName();
        this.rarity = crate.getRarity();
        this.type = crate.getItemType();
    }

    @Override
    public Item build() {
        Item item = itemService.create(type).withName(rarity.getColor() + name);

        // set type at top of tooltip
        item = item.addTooltip("Loot Crate");

        // add flavor text
        item = item.addTooltip("");
        item = item.addTooltip("[RIGHT CLICK] To open for a chance");
        item = item.addTooltip("at unlocking collectables or receiving");
        item = item.addTooltip("random combat items.");

        return item;
    }
}
