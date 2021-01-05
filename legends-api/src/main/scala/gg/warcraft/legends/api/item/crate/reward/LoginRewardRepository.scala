/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.crate.reward

import com.typesafe.config.Config
import io.getquill._
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom

import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait LoginRewardRepository {
  def load(playerId: UUID): Option[LoginRewardsClaimed]

  def save(data: LoginRewardsClaimed): Future[Unit]
}

private trait PlayerDataContext[I <: SqlIdiom, N <: NamingStrategy] {
  this: JdbcContext[I, N] =>

  def loadData = quote {
    (q: Query[LoginRewardsClaimed], playerId: UUID) =>
      q.filter { _.playerId == lift(playerId) }
  }

  def upsertData = quote {
    (q: EntityQuery[LoginRewardsClaimed], data: LoginRewardsClaimed) =>
      q.insert(lift(data)).onConflictUpdate(_.playerId)(
        (t, e) => t.counter -> e.counter,
        (t, e) => t.date -> e.date
      )
  }
}

private[legends] class PostgresLoginRewardRepository(
    config: Config
) extends LoginRewardRepository {
  private val database = new PostgresJdbcContext[SnakeCase](SnakeCase, config)
    with PlayerDataContext[PostgresDialect, SnakeCase]
  import database._

  override def load(playerId: UUID): Option[LoginRewardsClaimed] =
    run { loadData(query[LoginRewardsClaimed], playerId) }.headOption

  override def save(data: LoginRewardsClaimed): Future[Unit] = Future {
    run { upsertData(query[LoginRewardsClaimed], data) }
  }
}

private[legends] class SqliteLoginRewardRepository(
    config: Config
) extends LoginRewardRepository {
  private val database = new SqliteJdbcContext[SnakeCase](SnakeCase, config)
    with PlayerDataContext[SqliteDialect, SnakeCase]
  import database._

  override def load(playerId: UUID): Option[LoginRewardsClaimed] =
    run { loadData(query[LoginRewardsClaimed], playerId) }.headOption

  override def save(data: LoginRewardsClaimed): Future[Unit] = Future {
    run { upsertData(query[LoginRewardsClaimed], data) }
  }
}
