/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.equipment.upgrade

import gg.warcraft.legends.api.entity.attribute.LegendsAttribute
import gg.warcraft.legends.api.item.ItemRarity
import gg.warcraft.monolith.api.item.{Item, ItemService, ItemType}

class EquipmentUpgradeBuilder(rarity: ItemRarity, name: String)(implicit
    itemService: ItemService
) {
  private var attributes = Map.empty[LegendsAttribute, Int]

  def withAttribute(
      attribute: LegendsAttribute,
      amount: Int
  ): EquipmentUpgradeBuilder = {
    attributes += (attribute -> amount)
    this
  }

  def withAttributes(
      attributes: Map[LegendsAttribute, Int]
  ): EquipmentUpgradeBuilder = {
    this.attributes = attributes
    this
  }

  def build(): Item = {
    var item = itemService.create(ItemType.PAPER).withName(s"${rarity.color}name")

    // set type at top of tooltip
    var tooltip: List[String] = "Equipment Upgrade" :: Nil

    // add attributes
    tooltip ::= ""
    attributes.foreach { case (attribute, amount) =>
      tooltip ::= s"+$amount $attribute"
    }

    tooltip ::= ""
    tooltip ::= "[Right Click] While holding a piece of equipment"
    tooltip ::= "in your main hand and this equipment upgrade in"
    tooltip ::= "your offhand to apply this upgrade to the item."

    item.withTooltip(tooltip: _*)
  }
}
