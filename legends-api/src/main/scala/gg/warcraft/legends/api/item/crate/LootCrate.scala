package gg.warcraft.legends.api.item.crate

import gg.warcraft.legends.api.item.ItemRarity
import gg.warcraft.monolith.api.block.variant.ChestVariant
import gg.warcraft.monolith.api.item.Chest

import scala.collection.SortedMap

class LootCrate(
    val id: String,
    name: String,
    val rarity: ItemRarity,
    val chestVariant: ChestVariant,
    val dropTables: List[LootCrate.DropTable]
) extends Chest(
      chestVariant,
      s"${rarity.color}$name",
      LootCrate.tooltip,
      1,
      Set.empty,
      true
    )

object LootCrate {
  sealed trait DropType
  // gameplay categories
  case object CONSUMABLE extends DropType
  case object EQUIPMENT extends DropType
  case object EQUIPMENT_UPGRADE extends DropType
  case object GOLD extends DropType
  // shop categories
  case object DECORATION extends DropType
  case object DECORATION_PACK extends DropType
  case object MINI_PET extends DropType
  case object COMBAT_CLASS extends DropType

  case class DropTable(
      typed: DropType,
      chance: Float,
      distribution: SortedMap[ItemRarity, Float]
  )

  private[crate] final val tooltip: List[String] = List(
    "Loot Crate",
    "",
    "[RIGHT CLICK] To open for a chance",
    "at unlocking collectibles or receiving",
    "random combat items."
  )
}
