/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.handler

import gg.warcraft.monolith.api.block.{
  BlockPreBreakEvent, BlockPrePlaceEvent, BlockPreTriggerEvent, BlockType
}
import gg.warcraft.monolith.api.core.GameMode
import gg.warcraft.monolith.api.core.auth.AuthService
import gg.warcraft.monolith.api.core.event.{Event, PreEvent}
import gg.warcraft.monolith.api.player.Player

class BlockBreakPlaceHandler(implicit
    authService: AuthService
) extends Event.Handler {
  private def isBuilding(player: Player): Boolean =
    player.mode == GameMode.CREATIVE && authService.isBuilding(player)

  override def reduce[T <: PreEvent](event: T): T = event match {
    case event @ BlockPreBreakEvent(_, player, _, _, _) =>
      if (isBuilding(player)) event.copy(explicitlyAllowed = true).asInstanceOf[T]
      else event.copy(cancelled = true).asInstanceOf[T]
    case event @ BlockPrePlaceEvent(_, _, _, player, _, _) =>
      if (isBuilding(player)) event.copy(explicitlyAllowed = true).asInstanceOf[T]
      else event.copy(cancelled = true).asInstanceOf[T]
    case event @ BlockPreTriggerEvent(block, _, _, _) =>
      if (block.`type` == BlockType.FARMLAND)
        event.copy(cancelled = true).asInstanceOf[T]
      else event.asInstanceOf[T]
    case _ => event
  }
}
