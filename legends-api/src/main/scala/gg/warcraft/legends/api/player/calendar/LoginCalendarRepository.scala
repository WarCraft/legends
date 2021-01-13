/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.calendar

import com.typesafe.config.Config
import gg.warcraft.monolith.api.util.codecs.quill._
import gg.warcraft.monolith.api.util.future.FutureOps
import io.getquill._
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom

import java.util.UUID
import java.util.logging.Logger
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait LoginCalendarRepository {
  def load(playerId: UUID): Option[LoginCalendar]

  def save(data: LoginCalendar): Future[Unit]
}

private trait LoginCalendarContext[I <: SqlIdiom, N <: NamingStrategy] {
  this: JdbcContext[I, N] =>

  def queryById = quote {
    (q: Query[LoginCalendar], playerId: UUID) =>
      q.filter { _.playerId == playerId }
  }

  def upsert = quote {
    (q: EntityQuery[LoginCalendar], data: LoginCalendar) =>
      q.insert(data).onConflictUpdate(_.playerId)(
        (t, e) => t.day -> e.day,
        (t, e) => t.claimed -> e.claimed
      )
  }
}

private[legends] class PostgresLoginCalendarRepository(
    config: Config
)(implicit
    logger: Logger
) extends LoginCalendarRepository {
  private val database = new PostgresJdbcContext[SnakeCase](SnakeCase, config)
    with LoginCalendarContext[PostgresDialect, SnakeCase]
  import database._

  override def load(playerId: UUID): Option[LoginCalendar] =
    run { queryById(query[LoginCalendar], lift(playerId)) }.headOption

  override def save(data: LoginCalendar) = Future[Unit] {
    run { upsert(query[LoginCalendar], lift(data)) }
  }.andThenLog("LoginRewardRepository", "upsert", data)
}

private[legends] class SqliteLoginCalendarRepository(
    config: Config
)(implicit
    logger: Logger
) extends LoginCalendarRepository {
  private val database = new SqliteJdbcContext[SnakeCase](SnakeCase, config)
    with LoginCalendarContext[SqliteDialect, SnakeCase]
  import database._

  override def load(playerId: UUID): Option[LoginCalendar] =
    run { queryById(query[LoginCalendar], lift(playerId)) }.headOption

  override def save(data: LoginCalendar) = Future[Unit] {
    run { upsert(query[LoginCalendar], lift(data)) }
  }.andThenLog("LoginRewardRepository", "upsert", data)
}
