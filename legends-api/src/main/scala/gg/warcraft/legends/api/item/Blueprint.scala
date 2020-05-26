/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item

import gg.warcraft.monolith.api.item.{Item, ItemService, ItemType}

import scala.collection.mutable

object Blueprint {
  private final val tooltip: List[String] = List(
    "Blueprint",
    "",
    "[Right Click] a blueprint to start placing a",
    "siege plot. [Left Click] to confirm, or [Right Click]",
    "again to cancel."
  )

  private val items: mutable.Map[String, Item] = mutable.Map.empty

  def apply(name: String, rarity: ItemRarity)(implicit
      itemService: ItemService
  ): Item = items.getOrElseUpdate(
    name,
    itemService.create(ItemType.PAPER)
      .withName(s"${rarity.color}$name")
      .withTooltip(tooltip: _*)
  )
}
