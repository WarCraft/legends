/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.handler

import gg.warcraft.monolith.api.block.{
  BlockPreBreakEvent, BlockPrePlaceEvent, BlockPreTriggerEvent, BlockType
}
import gg.warcraft.monolith.api.core.auth.AuthService
import gg.warcraft.monolith.api.core.event.{Event, PreEvent}
import gg.warcraft.monolith.api.core.GameMode
import gg.warcraft.monolith.api.util.typing._

class BlockBreakPlaceHandler(implicit
    authService: AuthService
) extends Event.Handler {
  override def reduce[T <: PreEvent](event: T): T = event match {
    case event @ BlockPreBreakEvent(_, player, _, _, _) =>
      if (player.mode == GameMode.CREATIVE && authService.isModerating(player)) {
        event.copy(explicitlyAllowed = true).as[T]
      } else event.copy(cancelled = true).as[T]
    case event @ BlockPrePlaceEvent(_, _, _, player, _, _) =>
      if (player.mode == GameMode.CREATIVE && authService.isModerating(player)) {
        event.copy(explicitlyAllowed = true).as[T]
      } else event.copy(cancelled = true).as[T]
    case event @ BlockPreTriggerEvent(block, _, _, _) =>
      if (block.`type` == BlockType.FARMLAND) event.copy(cancelled = true).as[T]
      else event.as[T]
    case _ => event
  }
}
