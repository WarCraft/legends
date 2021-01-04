/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.game

import com.typesafe.config.Config
import gg.warcraft.legends.api.entity.LegendsTeam._
import gg.warcraft.monolith.api.core.Duration.DurationOps
import gg.warcraft.monolith.api.entity.team.TeamService
import io.getquill._
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom

import java.time.LocalDateTime
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait GameRepository {
  def load(id: Long)(implicit
      teamService: TeamService
  ): Option[Game]

  def save(game: Game): Future[Long]
}

private case class _Game(
    id: Int,
    startTime: LocalDateTime,
    endTime: Option[LocalDateTime],
    timer: Int,
    objectives: String,
    dailyPoints: String,
    monthlyScore: String
) {
  def toGame(implicit
      teamService: TeamService
  ): Game = Game(
    id,
    startTime,
    endTime,
    timer.seconds,
    objectives.split(",")
      .map { _.split(":") }
      .map { it => it(0) -> it(1).instance }.toMap,
    dailyPoints.split(",")
      .map { _.split(":") }
      .map { it => it(0).instance -> it(1).toInt }.toMap,
    monthlyScore.split(",")
      .map { _.split(":") }
      .map { it => it(0).instance -> it(1).toInt }.toMap
  )
}

private object _Game {
  def apply(game: Game): _Game = _Game(
    game.id,
    game.startTime,
    game.endTime,
    game.timer.seconds,
    game.objectives
      .map { case (obj, team) => s"$obj:${team.id}" }.mkString(","),
    game.dailyPoints
      .map { case (team, points) => s"${team.id}:$points" }.mkString(","),
    game.monthlyScore
      .map { case (team, score) => s"${team.id}:$score" }.mkString(",")
  )
}

private trait GameContext[I <: SqlIdiom, N <: NamingStrategy] {
  this: JdbcContext[I, N] =>

  def queryGame(id: Long)(implicit
      schema: Quoted[EntityQuery[_Game]]
  ) =
    quote { schema.filter { _.id == lift(id) } }

  def upsertGame(game: Game)(implicit
      schema: Quoted[EntityQuery[_Game]]
  ) = quote {
    schema.insert(lift(_Game(game))).onConflictUpdate(_.id)(
      (t, e) => t.endTime -> e.endTime,
      (t, e) => t.timer -> e.timer,
      (t, e) => t.objectives -> e.objectives,
      (t, e) => t.dailyPoints -> e.dailyPoints,
      (t, e) => t.monthlyScore -> e.monthlyScore
    ).returningGenerated(_.id)
  }
}

private[legends] class PostgresCombatDataRepository(
    config: Config
) extends GameRepository {
  private val database = new PostgresJdbcContext[SnakeCase](SnakeCase, config)
    with GameContext[PostgresDialect, SnakeCase]
  import database._

  private implicit val schema = quote { querySchema[_Game]("game") }

  override def load(id: Long)(implicit
      teamService: TeamService
  ): Option[Game] =
    run { queryGame(id) }.headOption.map { _.toGame }

  override def save(game: Game): Future[Long] =
    Future { run { upsertGame(game) } }
}

private[legends] class SqliteCombatDataRepository(
    config: Config
) extends GameRepository {
  private val database = new SqliteJdbcContext[SnakeCase](SnakeCase, config)
    with GameContext[SqliteDialect, SnakeCase]
  import database._

  private implicit val schema = quote { querySchema[_Game]("game") }

  override def load(id: Long)(implicit
      teamService: TeamService
  ): Option[Game] =
    run { queryGame(id) }.headOption.map { _.toGame }

  override def save(game: Game): Future[Long] =
    Future { run { upsertGame(game) } }
}
