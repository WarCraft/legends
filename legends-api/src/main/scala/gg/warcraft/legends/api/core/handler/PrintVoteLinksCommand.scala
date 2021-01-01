/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.handler

import gg.warcraft.monolith.api.core.Message
import gg.warcraft.monolith.api.core.auth.Principal
import gg.warcraft.monolith.api.core.command.Command
import gg.warcraft.monolith.api.player.{Player, PlayerService}

class PrintVoteLinksCommand() extends Command("vote")

object PrintVoteLinksCommand {
  private final val voteMessages = List(
    Message.server("https://minecraft-server-list.com/server/423343/vote/"),
    Message.server("https://minecraftservers.org/vote/257786/"),
    Message.server("https://minecraft-server.net/vote/warcraftgg/"),
    Message.server("https://minecraft-mp.com/server/192075/vote/"),
    Message.server("https://minecraftservers.biz/servers/106701/")
  )

  private final val onlineInventoryWarning =
    Message.server(
      "Please make sure you are online and have a free inventory slot when casting votes!"
    )

  class Handler(implicit
      playerService: PlayerService
  ) extends Command.Handler {
    override def handle(
        executor: Principal,
        command: Command,
        args: String*
    ): Command.Result = executor match {
      case player: Player =>
        voteMessages.foreach { playerService.sendMessage(player.id, _) }
        playerService.sendMessage(player.id, onlineInventoryWarning)
        Command.success
      case _ => Command.playersOnly
    }
  }
}
