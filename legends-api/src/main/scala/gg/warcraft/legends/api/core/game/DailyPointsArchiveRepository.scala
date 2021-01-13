/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.game

import com.typesafe.config.Config
import gg.warcraft.monolith.api.util.codecs.quill._
import gg.warcraft.monolith.api.util.future.FutureOps
import gg.warcraft.monolith.api.util.types.MonolithDate
import io.getquill._
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom

import java.util.logging.Logger
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait DailyPointsArchiveRepository {
  def archive(game: Game, date: MonolithDate): Future[Unit]
}

private trait DailyPointsArchiveContext[I <: SqlIdiom, N <: NamingStrategy] {
  this: JdbcContext[I, N] =>

  def insert = quote {
    (q: EntityQuery[DailyPointsArchive], archive: DailyPointsArchive) =>
      q.insert(archive)
  }
}

private[legends] class PostgresDailyPointsArchiveRepository(
    config: Config
)(implicit
    logger: Logger
) extends DailyPointsArchiveRepository {
  private val database = new PostgresJdbcContext[SnakeCase](SnakeCase, config)
    with DailyPointsArchiveContext[PostgresDialect, SnakeCase]
  import database._

  override def archive(game: Game, date: MonolithDate) = Future[Unit] {
    val dailyPoints = game.dailyPoints
      .map { case (team, points) => s"${team.id}:$points" }.mkString(",")
    val archive = DailyPointsArchive(game.id, date, dailyPoints)
    run { insert(query[DailyPointsArchive], lift(archive)) }
  }.andThenLog("DailyPointsArchiveRepository", "insert", game, date)
}

private[legends] class SqliteDailyPointsArchiveRepository(
    config: Config
)(implicit
    logger: Logger
) extends DailyPointsArchiveRepository {
  private val database = new SqliteJdbcContext[SnakeCase](SnakeCase, config)
    with DailyPointsArchiveContext[SqliteDialect, SnakeCase]
  import database._

  override def archive(game: Game, date: MonolithDate) = Future[Unit] {
    val dailyPoints = game.dailyPoints
      .map { case (team, points) => s"${team.id}:$points" }.mkString(",")
    val archive = DailyPointsArchive(game.id, date, dailyPoints)
    run { insert(query[DailyPointsArchive], lift(archive)) }
  }.andThenLog("DailyPointsArchiveRepository", "insert", game, date)
}
