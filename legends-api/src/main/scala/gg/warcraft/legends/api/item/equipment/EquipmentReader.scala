/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.equipment

import gg.warcraft.legends.api.entity.attribute.LegendsAttribute
import gg.warcraft.legends.api.entity.attribute.LegendsAttribute._
import gg.warcraft.legends.api.item.ItemRarity
import gg.warcraft.legends.api.item.ItemRarity.{toString => _, _}
import gg.warcraft.monolith.api.item.Item
import gg.warcraft.monolith.api.util.string._

import scala.jdk.CollectionConverters._

class EquipmentReader(item: Item) {
  private lazy val tooltip = item.getTooltip.asScala

  lazy val name: String = item.name.stripChatCodes
  lazy val rarity: ItemRarity = item.name.replace(name, "") match {
    case it if Rare.color.toString == it      => Rare
    case it if Epic.color.toString == it      => Epic
    case it if Legendary.color.toString == it => Legendary
    case _                                    => Common
  }

  lazy val typ: String = tooltip.headOption
    .map { _.stripChatCodes }.getOrElse("Unknown")
  lazy val isEquipment: Boolean =
    tooltip.nonEmpty && typ == "Equipment"
  lazy val isEquipmentUpgrade: Boolean =
    tooltip.nonEmpty && typ == "Equipment Upgrade"

  lazy val strength: Int = findAttributeValue(Strength)
  lazy val dexterity: Int = findAttributeValue(Dexterity)
  lazy val intellect: Int = findAttributeValue(Intellect)
  lazy val vitality: Int = findAttributeValue(Vitality)
  lazy val crit: Int = findAttributeValue(Crit)

  lazy val upgradeCount: Int = findUpgradeCount()
  lazy val availableUpgrades: Int = findAvailableUpgrades()

  private def findAttributeValue(attribute: LegendsAttribute): Int =
    tooltip.find { _.contains(attribute) } match {
      case Some(line) =>
        val plusIndex = line.indexOf('+')
        val spaceIndex = line.indexOf(' ', plusIndex)
        val statValue = line.substring(plusIndex + 1, spaceIndex)
        statValue.toInt
      case None => 0
    }

  private def findAvailableUpgrades(): Int =
    tooltip.find { _.contains("Available Upgrades") } match {
      case Some(line) =>
        val cleanedLine = line.stripChatCodes
        val spaceIndex = cleanedLine.indexOf(' ')
        val upgradeValue = cleanedLine.substring(0, spaceIndex)
        upgradeValue.toInt
      case None => 0
    }

  private def findUpgradeCount(): Int = {
    val plusIndex = item.name.indexOf("(+")
    if (plusIndex >= 0) {
      val upgradeCount = item.name.substring(0, plusIndex)
      upgradeCount.toInt
    } else 0
  }
}
