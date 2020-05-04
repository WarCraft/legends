package gg.warcraft.legends.api.entity

import gg.warcraft.monolith.api.entity.attribute.Attribute

sealed abstract class LegendsAttribute(
    override val name: String
) extends Attribute.Type

object LegendsAttribute {
  case object STRENGTH extends LegendsAttribute("Strength")
  case object DEXTERITY extends LegendsAttribute("Dexterity")
  case object INTELLECT extends LegendsAttribute("Intellect")
  case object VITALITY extends LegendsAttribute("Vitality")
  case object CRIT extends LegendsAttribute("Crit")
}
