/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.game

import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.entity.team.Team

// SKIRMISH
case class SkirmishStartedEvent(
    skirmish: Game
) extends Event

case class SkirmishFinishedEvent(
    skirmish: Game,
    winner: Team
) extends Event

// WAR
case class WarStartedEvent(
    war: Game
) extends Event

case class WarTallyEvent(
    updated: Game,
    outdated: Game
) extends Event

case class WarFinishedEvent(
    war: Game,
    winner: Team
) extends Event
