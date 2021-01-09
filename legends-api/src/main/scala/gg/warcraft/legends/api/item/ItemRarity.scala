/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item

import gg.warcraft.monolith.api.core.ColorCode

sealed case class ItemRarity(
    id: String,
    name: String,
    color: ColorCode
)

object ItemRarity {
  object Common extends ItemRarity("COMMON", "Common", ColorCode.WHITE)
  object Rare extends ItemRarity("RARE", "Rare", ColorCode.BLUE)
  object Epic extends ItemRarity("EPIC", "Epic", ColorCode.DARK_PURPLE)
  object Legendary extends ItemRarity("LEGENDARY", "Legendary", ColorCode.GOLD)

  def valueOf(rarity: String): ItemRarity = rarity match {
    case "COMMON"    => Common
    case "RARE"      => Rare
    case "EPIC"      => Epic
    case "LEGENDARY" => Legendary
  }

  implicit def toString(rarity: ItemRarity): String = rarity.id
}
