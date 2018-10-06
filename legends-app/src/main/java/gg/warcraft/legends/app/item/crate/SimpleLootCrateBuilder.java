package gg.warcraft.legends.app.item.crate;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.item.crate.LootCrate;
import gg.warcraft.legends.api.item.crate.LootCrateBuilder;
import gg.warcraft.legends.api.item.crate.LootCrateType;
import gg.warcraft.legends.api.item.crate.service.LootCrateRepository;
import gg.warcraft.monolith.api.item.Item;
import gg.warcraft.monolith.api.item.ItemBuilder;
import gg.warcraft.monolith.api.item.ItemBuilderFactory;
import gg.warcraft.monolith.api.item.ItemType;

public class SimpleLootCrateBuilder implements LootCrateBuilder {
    private final ItemBuilderFactory itemBuilderFactory;
    private final String name;
    private final ItemRarity rarity;
    private final ItemType type;

    @Inject
    public SimpleLootCrateBuilder(LootCrateRepository lootCrateRepository,
                                  ItemBuilderFactory itemBuilderFactory,
                                  @Assisted LootCrateType type) {
        this.itemBuilderFactory = itemBuilderFactory;

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
        ItemBuilder itemBuilder = itemBuilderFactory.createItemBuilder(type, rarity.getColor() + name);

        // set type at top of tooltip
        itemBuilder.addLore("Loot Crate");

        // add flavor text
        itemBuilder.addLore("");
        itemBuilder.addLore("[RIGHT CLICK] To open for a chance");
        itemBuilder.addLore("at unlocking collectables or receiving");
        itemBuilder.addLore("random combat items.");

        return itemBuilder.build();
    }
}
