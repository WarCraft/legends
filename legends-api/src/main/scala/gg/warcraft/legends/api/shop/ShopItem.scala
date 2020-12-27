/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.shop

import gg.warcraft.legends.api.entity.attribute.LegendsAttribute
import gg.warcraft.legends.api.item.ItemRarity
import gg.warcraft.monolith.api.entity.{Entity, Equipment}
import gg.warcraft.monolith.api.item.ItemTypeOrVariant

trait ShopItem {
  val id: String
  val typ: ShopItem.Type
  val rarity: ItemRarity
  val price: Int
  val icon: Option[ItemTypeOrVariant]
  val skull: Option[String]
  val name: String
  val tooltip: List[String]
}

object ShopItem {
  type Type = String
  final val CombatClass: Type = "COMBAT_CLASS"
  final val Decoration: Type = "DECORATION"
  final val DecorationPack: Type = "DECORATION_PACK"
  final val MiniPet: Type = "MINI_PET"
  final val Promotion: Type = "PROMOTION"
  // WarPass
  // TitanCrate
  // Bundle
  // Emote
  // AbilitySkin
  // SiegeSkin
  // Tool
  // Utility
}

case class CombatClass(
    id: String,
    typ: ShopItem.Type,
    rarity: ItemRarity,
    price: Int,
    icon: Option[ItemTypeOrVariant],
    skull: Option[String],
    name: String,
    tooltip: List[String]
) extends ShopItem

case class Decoration(
    id: String,
    typ: ShopItem.Type,
    rarity: ItemRarity,
    price: Int,
    icon: Option[ItemTypeOrVariant],
    skull: Option[String],
    name: String,
    tooltip: List[String],
    itemType: ItemTypeOrVariant,
    pack: String
) extends ShopItem

case class DecorationPack(
    id: String,
    typ: ShopItem.Type,
    rarity: ItemRarity,
    price: Int,
    icon: Option[ItemTypeOrVariant],
    skull: Option[String],
    name: String,
    tooltip: List[String],
    decorations: List[Decoration]
) extends ShopItem

case class MiniPet(
    id: String,
    typ: ShopItem.Type,
    rarity: ItemRarity,
    price: Int,
    icon: Option[ItemTypeOrVariant],
    skull: Option[String],
    name: String,
    tooltip: List[String],
    entityType: Entity.Type,
    rainbow: Boolean
) extends ShopItem

case class Promotion(
    id: String,
    typ: ShopItem.Type,
    rarity: ItemRarity,
    price: Int,
    icon: Option[ItemTypeOrVariant],
    skull: Option[String],
    name: String,
    tooltip: List[String],
    itemType: ItemTypeOrVariant,
    slot: Option[Equipment.Slot],
    attributes: Map[LegendsAttribute, Int]
) extends ShopItem
