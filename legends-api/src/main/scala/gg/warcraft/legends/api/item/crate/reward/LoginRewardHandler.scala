/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.crate.reward

import gg.warcraft.monolith.api.core.event.{Event, PreEvent}
import gg.warcraft.monolith.api.player.{PlayerDisconnectEvent, PlayerPreConnectEvent}

class LoginRewardHandler(implicit
    service: LoginRewardService
) extends Event.Handler {
  override def handle(event: PreEvent): Unit = event match {
    case PlayerPreConnectEvent(playerId, _, _, _) =>
      service.loadRewardsClaimed(playerId)
    case _ =>
  }

  override def handle(event: Event): Unit = event match {
    case PlayerDisconnectEvent(player) => service.invalidateRewardsClaimed(player.id)
    case _                             =>
  }
}
