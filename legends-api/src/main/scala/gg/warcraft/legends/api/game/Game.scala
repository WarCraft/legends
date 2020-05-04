package gg.warcraft.legends.api.game

import java.time.LocalDateTime

import gg.warcraft.monolith.api.core.Duration
import gg.warcraft.monolith.api.entity.team.Team

trait Game {
  val id: Int
  val points: Map[Team, Int]
  val objectives: Map[String, Team]
  val timer: Duration
  val startTime: LocalDateTime
  val endTime: Option[LocalDateTime]
}

case class Skirmish(
    override val id: Int,
    override val points: Map[Team, Int],
    override val objectives: Map[String, Team],
    override val timer: Duration,
    override val startTime: LocalDateTime,
    override val endTime: Option[LocalDateTime]
) extends Game

case class War(
    override val id: Int,
    override val points: Map[Team, Int],
    override val objectives: Map[String, Team],
    override val timer: Duration,
    override val startTime: LocalDateTime,
    override val endTime: Option[LocalDateTime],
    warScore: Map[Team, Int]
) extends Game
