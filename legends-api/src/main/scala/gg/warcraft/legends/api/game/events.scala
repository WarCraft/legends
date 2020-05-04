package gg.warcraft.legends.api.game

import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.entity.team.Team

// SKIRMISH
case class SkirmishStartedEvent(
    skirmish: Skirmish
) extends Event

case class SkirmishFinishedEvent(
    skirmish: Skirmish,
    winner: Team
) extends Event

// WAR
case class WarStartedEvent(
    war: War
) extends Event

case class WarTallyEvent(
    war: War,
    points: Map[Team, Int]
) extends Event

case class WarPausedEvent(
    war: War
) extends Event

case class WarResumedEvent(
    war: War
) extends Event

case class WarFinishedEvent(
    war: War,
    winner: Team
) extends Event
