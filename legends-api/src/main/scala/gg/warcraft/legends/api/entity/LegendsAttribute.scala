/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.entity

import gg.warcraft.monolith.api.entity.attribute.Attribute

sealed abstract class LegendsAttribute(
    override val name: String
) extends Attribute {
  override def toString: String = name
}

object LegendsAttribute {
  case object Strength extends LegendsAttribute("Strength")
  case object Dexterity extends LegendsAttribute("Dexterity")
  case object Intellect extends LegendsAttribute("Intellect")
  case object Vitality extends LegendsAttribute("Vitality")
  case object Crit extends LegendsAttribute("Crit")
}
