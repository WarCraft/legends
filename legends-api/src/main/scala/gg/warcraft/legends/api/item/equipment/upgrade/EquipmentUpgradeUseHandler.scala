/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.equipment.upgrade

import gg.warcraft.legends.api.entity.attribute.LegendsAttribute._
import gg.warcraft.legends.api.item.equipment.EquipmentReader
import gg.warcraft.monolith.api.block.BlockPreInteractEvent
import gg.warcraft.monolith.api.core.event.{Event, PreEvent}
import gg.warcraft.monolith.api.entity.EntityPreInteractEvent
import gg.warcraft.monolith.api.entity.Equipment._
import gg.warcraft.monolith.api.item.StackableItem
import gg.warcraft.monolith.api.player.{
  Player, PlayerPreInteractEvent, PlayerService
}
import gg.warcraft.monolith.api.util.option._

class EquipmentUpgradeUseHandler(implicit
    playerService: PlayerService
) extends Event.Handler {
  private def onInteract(player: Player): Boolean = {
    val equipment = player.equipment
    (equipment.mainHand, equipment.offHand) match {
      case (Some(mainHand), Some(offHand)) =>
        val equipmentReader = new EquipmentReader(mainHand)
        val upgradeReader = new EquipmentReader(offHand)
        if (equipmentReader.isEquipment && upgradeReader.isEquipmentUpgrade) {
          val availableUpgrades = equipmentReader.availableUpgrades
          if (availableUpgrades > 0) {
            // TODO move this logic into separate equipment modifier class?
            val name = {
              val upgrades = equipmentReader.upgradeCount
              if (upgrades == 0) s"${mainHand.name} (+1)"
              else mainHand.name.replace(s"(+$upgrades)", s"(+${upgrades + 1})")
            }

            val str = equipmentReader.strength + upgradeReader.strength
            val dex = equipmentReader.dexterity + upgradeReader.dexterity
            val int = equipmentReader.intellect + upgradeReader.intellect
            val vit = equipmentReader.vitality + upgradeReader.vitality
            val crit = equipmentReader.crit + upgradeReader.crit

            val tooltip = mainHand.tooltip.map {
              case it if it.contains(Strength.name)  => s"+$str ${Strength.name}"
              case it if it.contains(Dexterity.name) => s"+$dex ${Dexterity.name}"
              case it if it.contains(Intellect.name) => s"+$int ${Intellect.name}"
              case it if it.contains(Vitality.name)  => s"+$vit ${Vitality.name}"
              case it if it.contains(Crit.name)      => s"+$crit ${Crit.name}"

              case it if it.contains("Upgrades Available") =>
                if (availableUpgrades == 1) "No Available Upgrades"
                else if (availableUpgrades == 2) "1 Available Upgrade"
                else s"${availableUpgrades - 1} Available Upgrades"

              case it => it
            }

            val upgradedMainHand = mainHand.withName(name).withTooltip(tooltip: _*)
            val reducedOffHand = offHand.asInstanceOf[StackableItem].oneLessOrNone
            playerService.setEquipment(player.id, MAIN_HAND, upgradedMainHand, true)
            playerService.setEquipment(player.id, OFF_HAND, reducedOffHand, true)
          } else false
        } else false
      case _ => false
    }
  }

  override def reduce[T <: PreEvent](event: T): T = event match {
    case event @ BlockPreInteractEvent(_, _, player, _, _) =>
      if (onInteract(player)) event.copy(cancelled = true).asInstanceOf[T]
      else event.asInstanceOf[T]
    case event @ EntityPreInteractEvent(_, player, _, _, _) =>
      if (onInteract(player)) event.copy(cancelled = true).asInstanceOf[T]
      else event.asInstanceOf[T]
    case event @ PlayerPreInteractEvent(player, _, _) =>
      if (onInteract(player)) event.copy(cancelled = true).asInstanceOf[T]
      else event.asInstanceOf[T]
    case _ => event
  }
}
