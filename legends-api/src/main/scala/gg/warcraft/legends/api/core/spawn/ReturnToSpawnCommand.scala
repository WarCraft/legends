/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.spawn

import gg.warcraft.monolith.api.core.auth.Principal
import gg.warcraft.monolith.api.core.command.Command
import gg.warcraft.monolith.api.player.Player

class ReturnToSpawnCommand() extends Command("spawn")

object ReturnToSpawnCommand {
  class Handler(implicit
      spawnService: SpawnService
  ) extends Command.Handler {
    override def handle(
        executor: Principal,
        command: Command,
        args: String*
    ): Command.Result = executor match {
      case player: Player =>
        spawnService.warpToSpawn(player.id)
        Command.success
      case _ => Command.playersOnly
    }
  }
}
