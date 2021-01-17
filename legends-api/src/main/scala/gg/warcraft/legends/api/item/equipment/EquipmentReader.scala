/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.equipment

import gg.warcraft.legends.api.entity.attribute.LegendsAttribute._
import gg.warcraft.legends.api.item.ItemRarity
import gg.warcraft.legends.api.item.ItemRarity.{toString => _, _}
import gg.warcraft.monolith.api.item.Item
import gg.warcraft.monolith.api.util.string._

import scala.jdk.CollectionConverters._

class EquipmentReader(val item: Item) {
  private lazy val tooltip = item.getTooltip.asScala
  private lazy val indexedTooltip = tooltip.zipWithIndex

  lazy val name: String = item.name.stripChatCodes
  lazy val rarity: ItemRarity = item.name.replace(name, "") match {
    case it if it == Rare.color.toString      => Rare
    case it if it == Epic.color.toString      => Epic
    case it if it == Legendary.color.toString => Legendary
    case _                                    => Common
  }

  lazy val typ: String = tooltip.headOption
    .map { _.stripChatCodes }.getOrElse("Unknown")
  lazy val isEquipment: Boolean =
    tooltip.nonEmpty && typ == "Equipment"
  lazy val isEquipmentUpgrade: Boolean =
    tooltip.nonEmpty && typ == "Equipment Upgrade"

  lazy val upgradeCount: Int = findUpgradeCount()

  lazy val strengthLine: Option[Int] = findLine(Strength)
  lazy val strength: Int = findAttributeValue(strengthLine)
  lazy val dexterityLine: Option[Int] = findLine(Dexterity)
  lazy val dexterity: Int = findAttributeValue(dexterityLine)
  lazy val intellectLine: Option[Int] = findLine(Intellect)
  lazy val intellect: Int = findAttributeValue(intellectLine)
  lazy val vitalityLine: Option[Int] = findLine(Vitality)
  lazy val vitality: Int = findAttributeValue(vitalityLine)
  lazy val critLine: Option[Int] = findLine(Crit)
  lazy val crit: Int = findAttributeValue(critLine)
  lazy val lastAttributeLine: Option[Int] =
    List(strengthLine, dexterityLine, intellectLine, vitalityLine, critLine).max

  lazy val availableUpgradesLine: Option[Int] = findLine("Available Upgrades")
  lazy val availableUpgrades: Int = findAvailableUpgrades()

  lazy val flavorText: List[String] = findFlavorText()

  private def findUpgradeCount(): Int = {
    val plusIndex = item.name.indexOf("(+")
    if (plusIndex >= 0) {
      val closingIndex = item.name.indexOf(")", plusIndex)
      val upgradeCount = item.name.substring(plusIndex + 1, closingIndex)
      upgradeCount.toInt
    } else 0
  }

  private def findLine(contains: String): Option[Int] =
    indexedTooltip
      .find { case (line, _) => line.contains(contains) }
      .map { case (_, i) => i }

  private def findAttributeValue(line: Option[Int]): Int =
    line.map(tooltip) match {
      case Some(line) =>
        val plusIndex = line.indexOf('+')
        val spaceIndex = line.indexOf(' ', plusIndex)
        val statValue = line.substring(plusIndex + 1, spaceIndex)
        statValue.toInt
      case None => 0
    }

  private def findAvailableUpgrades(): Int =
    availableUpgradesLine.map(tooltip) match {
      case Some(line) =>
        val cleanedLine = line.stripChatCodes
        val spaceIndex = cleanedLine.indexOf(' ')
        val upgradeValue = cleanedLine.substring(0, spaceIndex)
        if (upgradeValue == "No") 0 else upgradeValue.toInt
      case None => 0
    }

  private def findFlavorText(): List[String] =
    indexedTooltip
      .findLast { case (line, _) => line.isEmpty }
      .map { case (_, i) => i } match {
      case Some(lastEmptyLine) =>
        if (
          lastEmptyLine > lastAttributeLine.getOrElse(0)
          && lastEmptyLine > availableUpgradesLine.getOrElse(0)
        ) {
          tooltip.drop(lastEmptyLine).toList
        } else Nil
      case None => Nil
    }
}
