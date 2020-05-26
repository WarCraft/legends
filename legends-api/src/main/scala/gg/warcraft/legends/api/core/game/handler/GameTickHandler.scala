/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.game.handler

import gg.warcraft.legends.api.core.game.GameService

class GameTickHandler(gameService: GameService) extends Runnable {
  override def run(): Unit = gameService.tickActiveGame()
}
