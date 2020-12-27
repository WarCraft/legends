/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.shop.profile

import gg.warcraft.monolith.api.core.event.{Event, PreEvent}
import gg.warcraft.monolith.api.player.{PlayerDisconnectEvent, PlayerPreConnectEvent}

class ShopProfileCacheHandler(implicit
    service: ShopProfileService
) extends Event.Handler {
  override def handle(event: PreEvent): Unit = event match {
    case PlayerPreConnectEvent(id, _, _, _) => service.loadProfile(id)
    case _                                  =>
  }
  override def handle(event: Event): Unit = event match {
    case PlayerDisconnectEvent(player) => service.invalidateProfile(player)
    case _                             =>
  }
}
