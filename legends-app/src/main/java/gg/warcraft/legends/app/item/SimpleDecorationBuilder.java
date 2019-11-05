package gg.warcraft.legends.app.item;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.legends.api.item.DecorationBuilder;
import gg.warcraft.legends.api.shop.Decoration;
import gg.warcraft.monolith.api.util.ColorCode;
import gg.warcraft.monolith.api.world.item.Item;
import gg.warcraft.monolith.api.world.item.ItemFactory;

public class SimpleDecorationBuilder implements DecorationBuilder {
    private final ItemFactory itemFactory;
    private final Decoration decoration;

    @Inject
    public SimpleDecorationBuilder(ItemFactory itemFactory,
                                   @Assisted Decoration decoration) {
        this.itemFactory = itemFactory;
        this.decoration = decoration;
    }

    @Override
    public Item build() {
        Item item = itemFactory.create(decoration.getItemType()).withName(
                decoration.getRarity().getColor() + decoration.getName());

        // set type at top of tooltip
        item = item.addTooltip(ColorCode.GRAY + "Decoration");

        // add tutorial
        item = item.addTooltip("");
        item = item.addTooltip("[RIGHT CLICK] to add this decoration to your house");

        return item;
    }
}
