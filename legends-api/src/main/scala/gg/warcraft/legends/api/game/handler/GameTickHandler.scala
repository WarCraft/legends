package gg.warcraft.legends.api.game.handler

import gg.warcraft.legends.api.game.GameService

class GameTickHandler(gameService: GameService) extends Runnable {
  override def run(): Unit = gameService.tickActiveGame()
}
