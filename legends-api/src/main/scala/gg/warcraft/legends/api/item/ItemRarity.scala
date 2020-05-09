package gg.warcraft.legends.api.item

import gg.warcraft.monolith.api.core.ColorCode

sealed abstract class ItemRarity(val name: String, val color: ColorCode.Type)

object ItemRarity {
  case object COMMON extends ItemRarity("Common", ColorCode.WHITE)
  case object RARE extends ItemRarity("Rare", ColorCode.DARK_BLUE)
  case object EPIC extends ItemRarity("Epic", ColorCode.DARK_PURPLE)
  case object LEGENDARY extends ItemRarity("Legendary", ColorCode.GOLD)
}
