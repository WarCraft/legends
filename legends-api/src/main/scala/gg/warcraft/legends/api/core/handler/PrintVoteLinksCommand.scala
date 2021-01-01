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
  private final val voteMessage1 =
    Message.server("https://minecraft-server-list.com/server/423343/vote/")
  private final val voteMessage2 =
    Message.server("https://minecraftservers.org/vote/257786/")
  private final val voteMessage3 =
    Message.server("https://minecraft-server.net/vote/warcraftgg/")
  private final val voteMessage4 =
    Message.server("https://minecraft-mp.com/server/192075/vote/")
  private final val voteMessage5 =
    Message.server("https://minecraftservers.biz/servers/106701/")

  class Handler(implicit
      playerService: PlayerService
  ) extends Command.Handler {
    override def handle(
        executor: Principal,
        command: Command,
        args: String*
    ): Command.Result = executor match {
      case player: Player =>
        playerService.sendMessage(player.id, voteMessage1)
        playerService.sendMessage(player.id, voteMessage2)
        playerService.sendMessage(player.id, voteMessage3)
        playerService.sendMessage(player.id, voteMessage4)
        playerService.sendMessage(player.id, voteMessage5)
        Command.success
      case _ => Command.playersOnly
    }
  }
}
