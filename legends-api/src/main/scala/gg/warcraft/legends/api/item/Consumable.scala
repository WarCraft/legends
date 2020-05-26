/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item

import gg.warcraft.monolith.api.item.{Item, ItemService, ItemTypeOrVariant}

import scala.collection.mutable

case class Consumable()

object Consumable {
  private val items: mutable.Map[String, Item] = mutable.Map.empty

  def apply(name: String, rarity: ItemRarity, typed: ItemTypeOrVariant)(implicit
      itemService: ItemService
  ): Item = items.getOrElseUpdate(
    name,
    itemService.create(typed)
      .withName(s"${rarity.color}$name")
      .withTooltip("Consumable")
  )
}
