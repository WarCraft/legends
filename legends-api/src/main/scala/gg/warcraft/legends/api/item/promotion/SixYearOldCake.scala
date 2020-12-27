/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.promotion

import gg.warcraft.legends.api.entity.attribute.LegendsAttribute
import gg.warcraft.legends.api.item.ItemRarity
import gg.warcraft.legends.api.item.equipment.EquipmentBuilder
import gg.warcraft.monolith.api.item.{Item, ItemService, ItemType}

object SixYearOldCake {
  private var cake: Item = _

  def apply(implicit
      itemService: ItemService
  ): Item =
    if (cake == null) {
      cake = new EquipmentBuilder(
        ItemType.CAKE,
        ItemRarity.Rare,
        "6 Year Old Cake"
      ).addAttribute(LegendsAttribute.Strength, 6)
        .addAttribute(LegendsAttribute.Dexterity, 6)
        .addAttribute(LegendsAttribute.Intellect, 6)
        .addFlavorText("After all this time its")
        .addFlavorText("still delicious!")
        .build()
      cake
    } else cake
}
