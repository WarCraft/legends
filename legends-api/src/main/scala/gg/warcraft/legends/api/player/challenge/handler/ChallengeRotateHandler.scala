/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.challenge.handler

import gg.warcraft.legends.api.core.game.WarStartedEvent
import gg.warcraft.legends.api.player.challenge.ChallengeService
import gg.warcraft.monolith.api.core.DailyTickEvent
import gg.warcraft.monolith.api.core.event.Event

class ChallengeRotateHandler(implicit
    service: ChallengeService
) extends Event.Handler {
  override def handle(event: Event): Unit = event match {
    case DailyTickEvent(_, _) => service.rotateDailies()
    case WarStartedEvent(_)   => service.rotateMonthlies()
    case _                    =>
  }
}
