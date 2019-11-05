package gg.warcraft.legends.app.item;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.legends.api.item.BlueprintBuilder;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.monolith.api.util.ColorCode;
import gg.warcraft.monolith.api.world.item.Item;
import gg.warcraft.monolith.api.world.item.ItemFactory;
import gg.warcraft.monolith.api.world.item.ItemType;

public class SimpleBlueprintBuilder implements BlueprintBuilder {
    private final ItemFactory itemFactory;
    private final ItemRarity rarity;
    private final String name;

    @Inject
    public SimpleBlueprintBuilder(ItemFactory itemFactory,
                                  @Assisted ItemRarity rarity,
                                  @Assisted String name) {
        this.itemFactory = itemFactory;
        this.rarity = rarity;
        this.name = name;
    }

    @Override
    public Item build() {
        Item item = itemFactory.create(ItemType.PAPER).withName(rarity.getColor() + name);

        // set type at top of tooltip
        item = item.addTooltip(ColorCode.GRAY + "Blueprint");

        // add tutorial
        item = item.addTooltip("",
                "[RIGHT CLICK] a blueprint to start placing a",
                "siege plot. Left click to confirm, or Right click",
                "again to cancel.");

        return item;
    }
}
