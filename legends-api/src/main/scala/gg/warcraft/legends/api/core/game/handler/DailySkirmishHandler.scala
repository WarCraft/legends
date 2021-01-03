/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.game.handler

import gg.warcraft.legends.api.LegendsConfig
import gg.warcraft.legends.api.core.game.GameService

import java.time.LocalTime

class DailySkirmishHandler(gameService: GameService) extends Runnable {
  private var skirmishTimes: List[LocalTime] = Nil

  def readConfig(config: LegendsConfig): Unit =
    config.skirmishTimes.foreach { skirmishTimes ::= LocalTime.of(_, 0) }

  override def run(): Unit = {
    val now = LocalTime.now.withSecond(0).withNano(0)
    if (skirmishTimes.contains(now)) {
      gameService.activeSkirmish match {
        case Some(_) =>
        case None    => gameService.startSkirmish()
      }
    }
  }
}
