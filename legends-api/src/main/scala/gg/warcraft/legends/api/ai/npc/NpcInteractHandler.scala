/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.ai.npc

import gg.warcraft.monolith.api.core.event.{Event, PreEvent}
import gg.warcraft.monolith.api.entity.EntityPreInteractEvent

class NpcInteractHandler(implicit
    npcService: NpcService
) extends Event.Handler {
  override def reduce[T <: PreEvent](event: T): T = event match {
    case it @ EntityPreInteractEvent(entity, player, _, _, _) =>
      if (npcService.npcsById.contains(entity.id)) {
        npcService.openDialog(entity.id, player.id)
        it.copy(cancelled = true).asInstanceOf[T]
      } else event
    case _ => event
  }
}
