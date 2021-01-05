/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.game.handler

import gg.warcraft.legends.api.core.game.GameService
import gg.warcraft.monolith.api.core.DailyTickEvent
import gg.warcraft.monolith.api.core.event.Event

class GameTickHandler(implicit
    gameService: GameService
) extends Event.Handler
    with Runnable {

  override def handle(event: Event): Unit = event match {
    case DailyTickEvent(today, _) =>
      gameService.runScore()
      if (today.getDayOfMonth == 1) {
        gameService.endWar()
        gameService.startWar()
      }
    case _ =>
  }

  override def run(): Unit = gameService.tickActiveGame()
}
