/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.equipment.basic

import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.combat.api.util.entity.EntityOps
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.entity.Equipment
import gg.warcraft.monolith.api.item.ItemService
import gg.warcraft.monolith.api.player.{PlayerRespawnEvent, PlayerService}
import gg.warcraft.monolith.api.util.option._

class BasicEquipmentHandler(implicit
    playerService: PlayerService,
    combatDataService: CombatDataService,
    itemService: ItemService
) extends Event.Handler {
  private val factory = new BasicEquipmentFactory
  override def handle(event: Event): Unit = event match {
    case PlayerRespawnEvent(player, _) =>
      factory.create(player.combatClass) match {
        case Some(basicWeapon) =>
          if (player.equipment.mainHand.isEmpty) {
            playerService.setEquipment(player.id, Equipment.MAIN_HAND, basicWeapon)
          } else itemService.giveTo(player.id, basicWeapon)
        case None =>
      }
    case _ =>
  }
}
