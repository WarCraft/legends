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
import scala.concurrent.{ExecutionContext, Future}

trait LoginRewardRepository {
  def load(playerId: UUID): Option[LoginRewardsClaimed]

  def save(data: LoginRewardsClaimed)(implicit
      executionContext: ExecutionContext = ExecutionContext.global
  ): Future[Unit]
}

private trait PlayerDataContext[I <: SqlIdiom, N <: NamingStrategy] {
  this: JdbcContext[I, N] =>

  def loadData = quote {
    (q: Query[LoginRewardsClaimed], playerId: UUID) =>
      q.filter { _.playerId == playerId }
  }

  def upsertData = quote {
    (q: EntityQuery[LoginRewardsClaimed], data: LoginRewardsClaimed) =>
      q.insert(data).onConflictUpdate(_.playerId)(
        (t, e) => t.counter -> e.counter,
        (t, e) => t.date -> e.date
      )
  }
}

private[monolith] class PostgresLoginRewardRepository(
    config: Config
) extends LoginRewardRepository {
  private val databaseContext = new PostgresJdbcContext[SnakeCase](SnakeCase, config)
    with PlayerDataContext[PostgresDialect, SnakeCase]
  import databaseContext._

  override def load(playerId: UUID): Option[LoginRewardsClaimed] =
    run { loadData(query[LoginRewardsClaimed], lift(playerId)) }.headOption

  override def save(data: LoginRewardsClaimed)(implicit
      executionContext: ExecutionContext = ExecutionContext.global
  ): Future[Unit] =
    Future { run { upsertData(query[LoginRewardsClaimed], lift(data)) } }
}

private[monolith] class SqliteLoginRewardRepository(
    config: Config
) extends LoginRewardRepository {
  private val context = new SqliteJdbcContext[SnakeCase](SnakeCase, config)
    with PlayerDataContext[SqliteDialect, SnakeCase]
  import context._

  override def load(playerId: UUID): Option[LoginRewardsClaimed] =
    run { loadData(query[LoginRewardsClaimed], lift(playerId)) }.headOption

  override def save(data: LoginRewardsClaimed)(implicit
      executionContext: ExecutionContext = ExecutionContext.global
  ): Future[Unit] =
    Future { run { upsertData(query[LoginRewardsClaimed], lift(data)) } }
}
