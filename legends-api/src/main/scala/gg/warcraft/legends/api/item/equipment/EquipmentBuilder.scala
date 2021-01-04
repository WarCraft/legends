/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.equipment

import gg.warcraft.legends.api.entity.attribute.LegendsAttribute
import gg.warcraft.legends.api.item.ItemRarity
import gg.warcraft.monolith.api.core.{ColorCode, FormatCode}
import gg.warcraft.monolith.api.item.{Item, ItemService, ItemTypeOrVariant}

class EquipmentBuilder(
    typ: ItemTypeOrVariant,
    rarity: ItemRarity,
    name: String
)(implicit
    itemService: ItemService
) {
  private final val reset = s"${FormatCode.RESET}${ColorCode.GRAY}"

  private var attributes: Map[LegendsAttribute, Int] = Map.empty
  private var availableUpgrades = 0
  private var upgradeCount = 0
  private var customModelData = 0
  private var flavorText: List[String] = Nil

  def addAttribute(attribute: LegendsAttribute, amount: Int): EquipmentBuilder = {
    attributes += (attribute -> amount)
    this
  }

  def withAttributes(attributes: Map[LegendsAttribute, Int]): EquipmentBuilder = {
    this.attributes = attributes
    this
  }

  def withAvailableUpgrades(count: Int): EquipmentBuilder = {
    availableUpgrades = count
    this
  }

  def withUpgradeCount(count: Int): EquipmentBuilder = {
    upgradeCount = count
    this
  }

  def withCustomModelData(data: Int): EquipmentBuilder = {
    customModelData = data
    this
  }

  def addFlavorText(text: String): EquipmentBuilder = {
    flavorText ::= text
    this
  }

  def withFlavorText(text: List[String]): EquipmentBuilder = {
    flavorText = text
    this
  }

  def build(): Item = {
    val upgradedName = if (upgradeCount > 0) s"$name (+$upgradeCount)" else name
    var item = itemService.create(typ).withName(s"${rarity.color}$upgradedName")

    // set type at top of tooltip
    item = item.addTooltip(s"${reset}Equipment")

    // add stats
    item = if (attributes.nonEmpty) item.addTooltip("") else item
    attributes.foreach { case (attribute, count) =>
      item = if (count != 0) item.addTooltip(s"$reset+$count $attribute") else item
    }

    // add upgrades
    item =
      if (availableUpgrades <= 0) item
      else item.addTooltip("", s"$reset$availableUpgrades Available Upgrades")

    // set custom model data
    item =
      if (customModelData == 0) item
      else item.withCustomModelData(Some(customModelData))

    // add flavor text
    item = if (flavorText.nonEmpty) item.addTooltip("") else item
    flavorText.foreach { it =>
      item = item.addTooltip(s"$reset${FormatCode.ITALIC}$it")
    }

    item.withHideAttributes(true)
  }
}
