/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.game

import gg.warcraft.monolith.api.core.auth.{AuthService, Principal}
import gg.warcraft.monolith.api.core.command.Command

class StartWarCommand() extends Command("start-war")
class EndWarCommand() extends Command("end-war")

class StartSkirmishCommand() extends Command("start-skirmish")
class EndSkirmishCommand() extends Command("end-skirmish")

class PauseGameCommand() extends Command("pause-game")
class ResumeGameCommand() extends Command("resume-game")

object GameCommand {
  class Handler(implicit
      authService: AuthService,
      gameService: GameService
  ) extends Command.Handler {
    override def handle(
        executor: Principal,
        command: Command,
        args: String*
    ): Command.Result = {
      if (authService.isAdmin(executor)) {
        command.name match {
          case "start-war" => gameService.startWar()
          case "end-war"   => gameService.endWar()

          case "start-skirmish" => gameService.startSkirmish()
          case "end-skirmish"   => gameService.endSkirmish()

          case "pause-game"  => gameService.pauseGame()
          case "resume-game" => gameService.resumeGame()
        }
        Command.success
      } else Command.invalid
    }
  }
}
