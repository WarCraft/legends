/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.shop

import gg.warcraft.monolith.api.core.auth.{AuthService, Principal}
import gg.warcraft.monolith.api.core.command.Command
import gg.warcraft.monolith.api.player.Player

class CycleShopItemsCommand() extends Command("cycle-shop")

object CycleShopItemsCommand {
  class Handler(implicit
      authService: AuthService,
      shopService: ShopService
  ) extends Command.Handler {
    override def handle(
        executor: Principal,
        command: Command,
        args: String*
    ): Command.Result = executor match {
      case player: Player =>
        if (authService.isAdmin(player)) {
          shopService.rotateAvailable()
          Command.success
        } else Command.invalid
      case _ => Command.playersOnly
    }
  }
}
