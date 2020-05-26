/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item

import gg.warcraft.monolith.api.core.ColorCode

sealed abstract class ItemRarity(val name: String, val color: ColorCode)

object ItemRarity {
  case object Common extends ItemRarity("Common", ColorCode.WHITE)
  case object Rare extends ItemRarity("Rare", ColorCode.DARK_BLUE)
  case object Epic extends ItemRarity("Epic", ColorCode.DARK_PURPLE)
  case object Legendary extends ItemRarity("Legendary", ColorCode.GOLD)
}
