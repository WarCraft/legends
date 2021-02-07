/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.ai

import gg.warcraft.legends.api.ai.npc.NpcService
import gg.warcraft.monolith.api.core.ServerShutdownEvent
import gg.warcraft.monolith.api.core.event.{Event, PreEvent}
import gg.warcraft.monolith.api.entity.EntityPreRespawnEvent

class AiPersistenceHandler(implicit
    npcService: NpcService
) extends Event.Handler {
  override def handle(event: Event): Unit = event match {
    case ServerShutdownEvent() =>
      npcService.npcsById.keys.foreach(npcService.despawnNpc)
    case _ =>
  }

  override def reduce[T <: PreEvent](event: T): T = event match {
    case event @ EntityPreRespawnEvent(entity, _, _, _) =>
      if (entity.name.endsWith(" Pet")) {
        event.copy(cancelled = true).asInstanceOf[T]
      } else if (
        npcService.npcNames.exists(entity.name.endsWith) &&
        !npcService.npcsById.contains(entity.id)
      ) {
        event.copy(cancelled = true).asInstanceOf[T]
      } else event.asInstanceOf[T]
    case _ => event
  }
}
