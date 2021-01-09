/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.data

import gg.warcraft.monolith.api.core.event.{Event, PreEvent}
import gg.warcraft.monolith.api.player.{PlayerDisconnectEvent, PlayerPreConnectEvent}

class LegendsPlayerDataCacheHandler(implicit
    service: LegendsPlayerDataService
) extends Event.Handler {
  override def handle(event: Event): Unit = event match {
    case PlayerDisconnectEvent(player) => service.invalidateData(player.id)
    case _                             =>
  }

  override def reduce[T <: PreEvent](event: T): T = event match {
    case PlayerPreConnectEvent(playerId, _, false, _) =>
      service.loadData(playerId) match {
        case Some(_) =>
        case None =>
          val data = LegendsPlayerData(playerId)
          service.setData(data)
      }
      event
    case _ => event
  }
}
