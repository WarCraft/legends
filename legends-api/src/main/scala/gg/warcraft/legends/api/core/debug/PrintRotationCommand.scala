/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.debug

import gg.warcraft.monolith.api.core.Message
import gg.warcraft.monolith.api.core.auth.Principal
import gg.warcraft.monolith.api.core.command.Command
import gg.warcraft.monolith.api.player.{Player, PlayerService}

class PrintRotationCommand() extends Command("printrot")

object PrintRotationCommand {
  class Handler(implicit
      playerService: PlayerService
  ) extends Command.Handler {
    override def handle(
        executor: Principal,
        command: Command,
        args: String*
    ): Command.Result = executor match {
      case player: Player =>
        val it = player.location.rotation
        val message = Message.server(s"Rotation: x=${it.x}, y=${it.y}, z=${it.z}")
        playerService.sendMessage(player.id, message)
        Command.success
      case _ => Command.playersOnly
    }
  }
}
