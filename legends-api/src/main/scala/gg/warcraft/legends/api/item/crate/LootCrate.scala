/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.crate

import gg.warcraft.legends.api.item.ItemRarity
import gg.warcraft.legends.api.item.ItemRarity._
import gg.warcraft.monolith.api.block.variant.ChestVariant
import gg.warcraft.monolith.api.core.{ColorCode, FormatCode}
import gg.warcraft.monolith.api.item.Chest

import scala.collection.SortedMap
import scala.collection.immutable.TreeMap

trait LootCrate {
  this: Chest =>

  val id: String
  val rarity: ItemRarity
  val dropTables: SortedMap[Float, LootCrate.DropTable]
}

object LootCrate {
  type Type = Chest with LootCrate
  type Id = String

  def apply(
      _id: String,
      _name: String,
      _rarity: ItemRarity,
      _chestVariant: ChestVariant,
      _dropTables: List[LootCrate.DropTable]
  ): LootCrate.Type = {
    new Chest(
      _chestVariant,
      s"${_rarity.color}${_name}",
      LootCrate.tooltip,
      1,
      Set.empty,
      true
    ) with LootCrate {
      val id: String = _id
      val rarity: ItemRarity = _rarity
      val dropTables: SortedMap[Float, LootCrate.DropTable] = {
        var totalChance = 0f
        var map = TreeMap.empty[Float, LootCrate.DropTable]
        _dropTables.foreach { it =>
          map += ((totalChance + it.chance) -> it)
          totalChance += it.chance
        }
        map
      }
    }
  }

  def changeCount(
      crate: LootCrate.Type,
      count: Int
  ): LootCrate.Type =
    new Chest(
      crate.variant,
      crate.name,
      crate.tooltip,
      count,
      crate.attributes,
      crate.hideAttributes
    ) with LootCrate {
      val id: String = crate.id
      val rarity: ItemRarity = crate.rarity
      val dropTables: SortedMap[Float, LootCrate.DropTable] = crate.dropTables
    }

  type DropType = String
  // gameplay categories
  final val Consumable: DropType = "CONSUMABLE"
  final val Equipment: DropType = "EQUIPMENT"
  final val EquipmentUpgrade: DropType = "EQUIPMENT_UPGRADE"
  final val Gold: DropType = "GOLD"
  // shop categories
  final val Decoration: DropType = "DECORATION"
  final val DecorationPack: DropType = "DECORATION_PACK"
  final val MiniPet: DropType = "MINI_PET"
  final val CombatClass: DropType = "COMBAT_CLASS"

  case class DropTable(
      typ: DropType,
      chance: Float,
      distribution: Map[ItemRarity, Float]
  ) {
    val distributionMap: SortedMap[Float, ItemRarity] = {
      var totalChance = 0f
      var map = TreeMap.empty[Float, ItemRarity]
      (Common :: Rare :: Epic :: Legendary :: Nil).foreach { rarity =>
        distribution.get(rarity) match {
          case Some(chance) =>
            map += ((totalChance + chance) -> rarity)
            totalChance += chance
          case None =>
        }
      }
      map
    }
  }

  private final val reset = s"${FormatCode.RESET}${ColorCode.GRAY}"
  private[crate] final val tooltip: List[String] = List(
    s"${reset}Loot Crate",
    "",
    s"$reset[Right Click] To open for a chance",
    s"${reset}at unlocking collectibles or receiving",
    s"${reset}random combat items."
  )

  case class Config(
      id: String,
      name: String,
      rarity: ItemRarity,
      chestVariant: ChestVariant,
      dropTables: List[LootCrate.DropTable]
  ) {
    def parse(): LootCrate.Type =
      LootCrate(id, name, rarity, chestVariant, dropTables)
  }
}
