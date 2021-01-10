/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.calendar

import com.typesafe.config.Config
import gg.warcraft.monolith.api.util.codecs.quill._
import io.getquill._
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom

import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait LoginRewardRepository {
  def load(playerId: UUID): Option[LoginCalendar]

  def save(data: LoginCalendar): Future[Unit]
}

private trait PlayerDataContext[I <: SqlIdiom, N <: NamingStrategy] {
  this: JdbcContext[I, N] =>

  def loadData = quote {
    (q: Query[LoginCalendar], playerId: UUID) =>
      q.filter { _.playerId == playerId }
  }

  def upsertData = quote {
    (q: EntityQuery[LoginCalendar], data: LoginCalendar) =>
      q.insert(data).onConflictUpdate(_.playerId)(
        (t, e) => t.day -> e.day,
        (t, e) => t.claimed -> e.claimed
      )
  }
}

private[legends] class PostgresLoginRewardRepository(
    config: Config
) extends LoginRewardRepository {
  private val database = new PostgresJdbcContext[SnakeCase](SnakeCase, config)
    with PlayerDataContext[PostgresDialect, SnakeCase]
  import database._

  override def load(playerId: UUID): Option[LoginCalendar] =
    run { loadData(query[LoginCalendar], lift(playerId)) }.headOption

  override def save(data: LoginCalendar): Future[Unit] = Future {
    run { upsertData(query[LoginCalendar], lift(data)) }
  }
}

private[legends] class SqliteLoginRewardRepository(
    config: Config
) extends LoginRewardRepository {
  private val database = new SqliteJdbcContext[SnakeCase](SnakeCase, config)
    with PlayerDataContext[SqliteDialect, SnakeCase]
  import database._

  override def load(playerId: UUID): Option[LoginCalendar] =
    run { loadData(query[LoginCalendar], lift(playerId)) }.headOption

  override def save(data: LoginCalendar): Future[Unit] = Future {
    run { upsertData(query[LoginCalendar], lift(data)) }
  }
}
