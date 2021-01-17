/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.equipment.upgrade

import gg.warcraft.legends.api.entity.attribute.LegendsAttribute._
import gg.warcraft.legends.api.item.equipment.{EquipmentBuilder, EquipmentReader}
import gg.warcraft.monolith.api.block.BlockPreInteractEvent
import gg.warcraft.monolith.api.core.event.{Event, PreEvent}
import gg.warcraft.monolith.api.entity.EntityPreInteractEvent
import gg.warcraft.monolith.api.entity.Equipment._
import gg.warcraft.monolith.api.item.{ItemService, StackableItem}
import gg.warcraft.monolith.api.player.{
  Player, PlayerPreInteractEvent, PlayerService
}
import gg.warcraft.monolith.api.util.option._
import gg.warcraft.monolith.api.world.{Sound, SoundCategory, WorldService}

class EquipmentUpgradeUseHandler(implicit
    playerService: PlayerService,
    worldService: WorldService,
    itemService: ItemService
) extends Event.Handler {
  private def onInteract(player: Player): Boolean = {
    val equipment = player.equipment
    (equipment.mainHand, equipment.offHand) match {
      case (Some(mainHand), Some(offHand)) =>
        val itemReader = new EquipmentReader(mainHand)
        val upgradeReader = new EquipmentReader(offHand)
        if (itemReader.isEquipment && upgradeReader.isEquipmentUpgrade) {
          val availableUpgrades = itemReader.availableUpgrades
          if (availableUpgrades > 0) {
            val cleanedName = itemReader.name.indexOf(" (+") match {
              case -1           => itemReader.name
              case upgradeIndex => itemReader.name.substring(0, upgradeIndex)
            }
            val builder =
              new EquipmentBuilder(mainHand.data, itemReader.rarity, cleanedName)

            builder.withUpgradeCount(itemReader.upgradeCount + 1)

            val updatedStrength = itemReader.strength + upgradeReader.strength
            builder.addAttribute(Strength, updatedStrength)
            val updatedDexterity = itemReader.dexterity + upgradeReader.dexterity
            builder.addAttribute(Dexterity, updatedDexterity)
            val updatedIntellect = itemReader.intellect + upgradeReader.intellect
            builder.addAttribute(Intellect, updatedIntellect)
            val updatedVitality = itemReader.vitality + upgradeReader.vitality
            builder.addAttribute(Vitality, updatedVitality)
            val updatedCrit = itemReader.crit + upgradeReader.crit
            builder.addAttribute(Crit, updatedCrit)

            builder.withAvailableUpgrades(availableUpgrades - 1)

            builder.withFlavorText(itemReader.flavorText)

            val upgradedMainHand = builder.build()
            val reducedOffHand = offHand.asInstanceOf[StackableItem].oneLessOrNone
            playerService.setEquipment(player.id, MAIN_HAND, upgradedMainHand, true)
            playerService.setEquipment(player.id, OFF_HAND, reducedOffHand, true)

            worldService.playSound(
              player.location,
              Sound.ENTITY_PLAYER_LEVELUP,
              SoundCategory.NEUTRAL
            )

            true
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
