/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.crate

import gg.warcraft.legends.api.item.ItemRarity
import gg.warcraft.legends.api.item.ItemRarity._
import gg.warcraft.monolith.api.block.variant.ChestVariant
import gg.warcraft.monolith.api.item.Chest

import scala.collection.SortedMap
import scala.collection.immutable.TreeMap

class LootCrate(
    val id: String,
    name: String,
    val rarity: ItemRarity,
    val chestVariant: ChestVariant,
    val dropTables: List[LootCrate.DropTable],
    count: Int = 1
) extends Chest(
      chestVariant,
      s"${rarity.color}$name",
      LootCrate.tooltip,
      count,
      Set.empty,
      true
    ) {
  val dropTablesMap: SortedMap[Float, LootCrate.DropTable] = {
    var totalChance = 0f
    var map = TreeMap.empty[Float, LootCrate.DropTable]
    dropTables.foreach { it =>
      map += ((totalChance + it.chance) -> it)
      totalChance += it.chance
    }
    map
  }
}

object LootCrate {
  type Id = String
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

  private[crate] final val tooltip: List[String] = List(
    "Loot Crate",
    "",
    "[RIGHT CLICK] To open for a chance",
    "at unlocking collectibles or receiving",
    "random combat items."
  )

  case class Config(
      id: String,
      name: String,
      rarity: ItemRarity,
      chestVariant: ChestVariant,
      dropTables: List[LootCrate.DropTable]
  ) {
    def parse(): LootCrate = new LootCrate(
      id,
      name,
      rarity,
      chestVariant,
      dropTables
    )
  }
}
