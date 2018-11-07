package gg.warcraft.legends.app.item;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.legends.api.item.BlueprintBuilder;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.monolith.api.item.Item;
import gg.warcraft.monolith.api.item.ItemBuilder;
import gg.warcraft.monolith.api.item.ItemBuilderFactory;
import gg.warcraft.monolith.api.item.ItemType;
import gg.warcraft.monolith.api.util.ColorCode;

public class SimpleBlueprintBuilder implements BlueprintBuilder {
    private final ItemBuilderFactory itemBuilderFactory;
    private final ItemRarity rarity;
    private final String name;

    @Inject
    public SimpleBlueprintBuilder(ItemBuilderFactory itemBuilderFactory,
                                  @Assisted ItemRarity rarity,
                                  @Assisted String name) {
        this.itemBuilderFactory = itemBuilderFactory;
        this.rarity = rarity;
        this.name = name;
    }

    @Override
    public Item build() {
        ItemBuilder itemBuilder = itemBuilderFactory.createItemBuilder(ItemType.PAPER, rarity.getColor() + name);

        // set type at top of tooltip
        itemBuilder.addLore(ColorCode.GRAY + "Blueprint");

        // add tutorial
        itemBuilder.addLore("")
                .addLore("[RIGHT CLICK] a blueprint to start placing a")
                .addLore("siege plot. Left click to confirm, or Right click")
                .addLore("again to cancel.");

        return itemBuilder.build();
    }
}
