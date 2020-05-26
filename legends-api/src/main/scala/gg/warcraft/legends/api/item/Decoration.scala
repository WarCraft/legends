/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item

import gg.warcraft.monolith.api.item.{Item, ItemService, ItemTypeOrVariant}

case class Decoration(
    val item: Item // FIXME store item on decoration so it doesnt have to be rebuild all the time
)

/* TODO
    DecorationBuilder createDecorationBuilder(Decoration decoration);
 */

object Decoration { // TODO remove
  def apply(typed: ItemTypeOrVariant)(implicit
      itemService: ItemService
  ): Item = itemService.create(typed).withTooltip(
    "Decoration",
    "",
    "[Right Click] to place this decoration in your house"
  )
}


/*
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
 */