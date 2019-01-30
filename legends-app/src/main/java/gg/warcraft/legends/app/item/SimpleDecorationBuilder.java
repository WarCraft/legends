package gg.warcraft.legends.app.item;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.legends.api.item.DecorationBuilder;
import gg.warcraft.legends.api.shop.Decoration;
import gg.warcraft.monolith.api.item.Item;
import gg.warcraft.monolith.api.item.ItemBuilder;
import gg.warcraft.monolith.api.item.ItemBuilderFactory;
import gg.warcraft.monolith.api.util.ColorCode;

public class SimpleDecorationBuilder implements DecorationBuilder {
    private final ItemBuilderFactory itemBuilderFactory;
    private final Decoration decoration;

    @Inject
    public SimpleDecorationBuilder(ItemBuilderFactory itemBuilderFactory,
                                   @Assisted Decoration decoration) {
        this.itemBuilderFactory = itemBuilderFactory;
        this.decoration = decoration;
    }

    @Override
    public Item build() {
        ItemBuilder itemBuilder = itemBuilderFactory.createItemBuilder(decoration.getItemType(),
                decoration.getRarity().getColor() + decoration.getName());

        // set type at top of tooltip
        itemBuilder.addLore(ColorCode.GRAY + "Decoration");

        // add tutorial
        itemBuilder.addLore("");
        itemBuilder.addLore("[RIGHT CLICK] to add this decoration to your house");

        return itemBuilder.build();
    }
}
