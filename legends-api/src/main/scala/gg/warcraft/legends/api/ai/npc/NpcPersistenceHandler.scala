/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.ai.npc

import gg.warcraft.legends.api.LegendsConfig
import gg.warcraft.monolith.api.core.ServerShutdownEvent
import gg.warcraft.monolith.api.core.event.Event

class NpcPersistenceHandler(config: LegendsConfig)(implicit
    npcService: NpcService
) extends Event.Handler {
  override def handle(event: Event): Unit = event match {
    case ServerShutdownEvent() =>
      npcService.npcsById.keys.foreach(npcService.despawnNpc)
    case _ =>
  }
}
