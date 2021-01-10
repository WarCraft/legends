/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.equipment.upgrade

import gg.warcraft.legends.api.entity.attribute.LegendsAttribute
import gg.warcraft.legends.api.item.ItemRarity
import gg.warcraft.monolith.api.core.{ColorCode, FormatCode}
import gg.warcraft.monolith.api.item.{Item, ItemService, ItemType}

class EquipmentUpgradeBuilder(rarity: ItemRarity, name: String)(implicit
    itemService: ItemService
) {
  private final val reset = s"${FormatCode.RESET}${ColorCode.GRAY}"

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
    val item = itemService.create(ItemType.PAPER).withName(s"${rarity.color}$name")

    val tooltip: List[String] = List(
      s"${reset}Equipment Upgrade",
      ""
    ) ++ attributes.map {
      case (attribute, amount) => s"$reset+$amount $attribute"
    } ++ List(
      "",
      s"$reset[Right Click] While holding a piece of equipment",
      s"${reset}in your main hand and this equipment upgrade in",
      s"${reset}your offhand to apply this upgrade to the item."
    )

    item.withTooltip(tooltip: _*)
  }
}
