/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.shop

import gg.warcraft.legends.api.entity.attribute.LegendsAttribute
import gg.warcraft.legends.api.item.ItemRarity
import gg.warcraft.monolith.api.entity.{Entity, Equipment}
import gg.warcraft.monolith.api.item.{Item, ItemService, ItemTypeOrVariant}

import scala.util.chaining.scalaUtilChainingOps

trait ShopItem {
  val id: String
  val typ: ShopItem.Type
  val rarity: ItemRarity
  val icon: Option[ItemTypeOrVariant]
  val skull: Option[String]
  val name: String
  val description: List[String]

  def price(implicit shopService: ShopService): Int =
    shopService.priceByTypeAndRarity(typ)(rarity)
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
    icon: Option[ItemTypeOrVariant],
    name: String,
    description: List[String]
) extends ShopItem {
  val skull: Option[String] = None
}

case class Decoration(
    id: String,
    typ: ShopItem.Type,
    rarity: ItemRarity,
    name: String,
    description: List[String],
    itemType: ItemTypeOrVariant,
    pack: String
) extends ShopItem {
  val icon: Option[ItemTypeOrVariant] = Some(itemType)
  val skull: Option[String] = None

  private var _stack: Item = _
  def stack(implicit itemService: ItemService): Item =
    if (_stack != null) _stack
    else itemService.create(itemType).withTooltip("Decoration").tap { _stack = _ }
}

case class DecorationPack(
    id: String,
    typ: ShopItem.Type,
    rarity: ItemRarity,
    icon: Option[ItemTypeOrVariant],
    name: String,
    description: List[String],
    decorations: List[String]
) extends ShopItem {
  val skull: Option[String] = None
}

case class MiniPet(
    id: String,
    typ: ShopItem.Type,
    rarity: ItemRarity,
    skull: Option[String],
    name: String,
    description: List[String],
    entityType: Entity.Type,
    rainbow: Boolean
) extends ShopItem {
  val icon: Option[ItemTypeOrVariant] = None
}

case class Promotion(
    id: String,
    typ: ShopItem.Type,
    rarity: ItemRarity,
    name: String,
    description: List[String],
    itemType: ItemTypeOrVariant,
    equipmentSlot: Equipment.Slot,
    attributes: Map[LegendsAttribute, Int]
) extends ShopItem {
  val icon: Option[ItemTypeOrVariant] = Some(itemType)
  val skull: Option[String] = None
}
