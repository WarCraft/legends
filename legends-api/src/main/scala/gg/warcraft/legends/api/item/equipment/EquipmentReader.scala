/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.equipment

import gg.warcraft.legends.api.entity.attribute.LegendsAttribute
import gg.warcraft.legends.api.entity.attribute.LegendsAttribute._
import gg.warcraft.monolith.api.item.Item
import gg.warcraft.monolith.api.util.string._

import scala.jdk.CollectionConverters._

class EquipmentReader(item: Item) {
  private lazy val tooltip = item.getTooltip.asScala

  lazy val isEquipment: Boolean = // TODO replace replaceAll call with stripChatCodes when Monolith updated
    tooltip.nonEmpty && tooltip.head.replaceAll("§[0-9a-z]", "") == "Equipment"

  lazy val strength: Int = findAttributeValue(Strength)
  lazy val dexterity: Int = findAttributeValue(Dexterity)
  lazy val intellect: Int = findAttributeValue(Intellect)
  lazy val vitality: Int = findAttributeValue(Vitality)
  lazy val crit: Int = findAttributeValue(Crit)

  private def findAttributeValue(attribute: LegendsAttribute): Int =
    tooltip.find { _.contains(attribute) } match {
      case Some(line) =>
        val plusIndex = line.indexOf('+')
        val spaceIndex = line.indexOf(' ', plusIndex)
        val statValue = line.substring(plusIndex + 1, spaceIndex)
        statValue.toInt
      case None => 0
    }
}
