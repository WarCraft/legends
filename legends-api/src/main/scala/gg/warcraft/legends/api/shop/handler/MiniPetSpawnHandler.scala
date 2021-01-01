/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.shop.handler

import gg.warcraft.legends.api.ai.MiniPetFactory
import gg.warcraft.monolith.api.core.ServerShutdownEvent
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.entity.EntityService
import gg.warcraft.monolith.api.player.PlayerDisconnectEvent

class MiniPetSpawnHandler(implicit
    entityService: EntityService,
    miniPetFactory: MiniPetFactory
) extends Event.Handler {
  override def handle(event: Event): Unit = event match {
    case PlayerDisconnectEvent(player) => miniPetFactory.remove(player.id)
    case ServerShutdownEvent()         => miniPetFactory.removeAll()
    case _                             =>
  }
}
