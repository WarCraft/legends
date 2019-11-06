package gg.warcraft.legends.app.item;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.legends.api.item.DecorationBuilder;
import gg.warcraft.legends.api.shop.Decoration;
import gg.warcraft.monolith.api.util.ColorCode;
import gg.warcraft.monolith.api.world.item.Item;
import gg.warcraft.monolith.api.world.item.ItemService;

public class SimpleDecorationBuilder implements DecorationBuilder {
    private final ItemService itemService;
    private final Decoration decoration;

    @Inject
    public SimpleDecorationBuilder(ItemService itemService,
                                   @Assisted Decoration decoration) {
        this.itemService = itemService;
        this.decoration = decoration;
    }

    @Override
    public Item build() {
        Item item = itemService.create(decoration.getItemType()).withName(
                decoration.getRarity().getColor() + decoration.getName());

        // set type at top of tooltip
        item = item.addTooltip(ColorCode.GRAY + "Decoration");

        // add tutorial
        item = item.addTooltip("");
        item = item.addTooltip("[RIGHT CLICK] to add this decoration to your house");

        return item;
    }
}
