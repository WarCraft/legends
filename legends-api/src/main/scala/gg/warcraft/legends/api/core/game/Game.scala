/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.game

import gg.warcraft.monolith.api.core.Duration
import gg.warcraft.monolith.api.entity.team.Team
import gg.warcraft.monolith.api.util.types.MonolithDateTime

case class Game(
    id: Long,
    startTime: MonolithDateTime,
    endTime: Option[MonolithDateTime] = None,
    timer: Duration,
    objectives: Map[String, Team] = Map.empty,
    dailyPoints: Map[Team, Int] = Map.empty,
    monthlyScore: Map[Team, Int] = Map.empty
)
