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
  private val tooltip = item.getTooltip.asScala

  def isEquipment: Boolean =
    tooltip.nonEmpty && tooltip.head.stripChatCodes == "Equipment"

  def strength: Int = findAttributeValue(Strength)
  def dexterity: Int = findAttributeValue(Dexterity)
  def intellect: Int = findAttributeValue(Intellect)
  def vitality: Int = findAttributeValue(Vitality)
  def crit: Int = findAttributeValue(Crit)

  private def findAttributeValue(attribute: LegendsAttribute): Int =
    tooltip.find { _.contains(attribute) } match {
      case Some(line) =>
        val spaceIndex = line.indexOf(' ')
        val statValue = line.substring(1, spaceIndex)
        statValue.toInt
      case None => 0
    }
}
