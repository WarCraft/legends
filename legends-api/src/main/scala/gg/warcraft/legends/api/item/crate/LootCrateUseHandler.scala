/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.crate

import gg.warcraft.monolith.api.block.BlockPreInteractEvent
import gg.warcraft.monolith.api.core.event.{Event, PreEvent}
import gg.warcraft.monolith.api.entity.EntityPreInteractEvent
import gg.warcraft.monolith.api.item.Item
import gg.warcraft.monolith.api.player.Player

class LootCrateUseHandler(lootCrateService: LootCrateService) extends Event.Handler {
  private def onInteract(player: Player, item: Option[Item]): Boolean = item match {
    case Some(item) =>
      val crate = lootCrateService.crates
        .find { case (_, crate) => item == crate }
        .map { _._2 }
      crate match {
        case Some(crate) =>
          lootCrateService.openCrate(player, crate)
          true
        case None => false
      }
    case None => false
  }

  override def reduce[T <: PreEvent](event: T): T = event match {
    case event @ BlockPreInteractEvent(_, _, player, _, _) =>
      if (onInteract(player, player.equipment.mainHand))
        event.copy(cancelled = true).asInstanceOf[T]
      else event.asInstanceOf[T]
    case event @ EntityPreInteractEvent(_, player, _, _, _) =>
      if (onInteract(player, player.equipment.mainHand))
        event.copy(cancelled = true).asInstanceOf[T]
      else event.asInstanceOf[T]
    case _ => event
  }
}
