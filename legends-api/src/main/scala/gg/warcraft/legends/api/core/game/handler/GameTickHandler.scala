package gg.warcraft.legends.api.core.game.handler

import gg.warcraft.legends.api.core.game.GameService

class GameTickHandler(gameService: GameService) extends Runnable {
  override def run(): Unit = gameService.tickActiveGame()
}
