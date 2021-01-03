/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.game

import gg.warcraft.monolith.api.core.Duration
import gg.warcraft.monolith.api.core.Duration.DurationOps
import gg.warcraft.monolith.api.entity.team.Team

import java.time.LocalDateTime

case class Game(
    id: Int,
    startTime: LocalDateTime,
    endTime: Option[LocalDateTime] = None,
    timer: Duration = Game.defaultTimer,
    objectives: Map[String, Team],
    dailyPoints: Map[Team, Int],
    monthlyScore: Map[Team, Int]
)

object Game{
  final val defaultTimer = 900.seconds
}
