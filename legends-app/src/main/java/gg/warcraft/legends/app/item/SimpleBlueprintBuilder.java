package gg.warcraft.legends.app.item;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.legends.api.item.BlueprintBuilder;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.monolith.api.util.ColorCode;
import gg.warcraft.monolith.api.world.item.Item;
import gg.warcraft.monolith.api.world.item.ItemService;
import gg.warcraft.monolith.api.world.item.ItemType;

public class SimpleBlueprintBuilder implements BlueprintBuilder {
    private final ItemService itemService;
    private final ItemRarity rarity;
    private final String name;

    @Inject
    public SimpleBlueprintBuilder(ItemService itemService,
                                  @Assisted ItemRarity rarity,
                                  @Assisted String name) {
        this.itemService = itemService;
        this.rarity = rarity;
        this.name = name;
    }

    @Override
    public Item build() {
        Item item = itemService.create(ItemType.PAPER).withName(rarity.getColor() + name);

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
