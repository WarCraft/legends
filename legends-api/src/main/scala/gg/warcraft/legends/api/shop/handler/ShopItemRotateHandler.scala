/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.shop.handler

import gg.warcraft.legends.api.shop.ShopService
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.core.{DailyTickEvent, Message}
import gg.warcraft.monolith.api.player.PlayerService

class ShopItemRotateHandler(
    shopService: ShopService,
    playerService: PlayerService
) extends Event.Handler {
  override def handle(event: Event): Unit = event match {
    case DailyTickEvent(_, _) =>
      shopService.rotateAvailable()
      val message = Message.server("The available items in the /shop have changed!")
      playerService.getOnlinePlayers.foreach { _.sendMessage(message) }
    case _ =>
  }
}
