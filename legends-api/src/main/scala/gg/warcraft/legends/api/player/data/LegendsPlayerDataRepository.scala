/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.data

import com.typesafe.config.Config
import io.getquill._
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom

import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait LegendsPlayerDataRepository {
  def load(id: UUID): Option[LegendsPlayerData]

  def save(data: LegendsPlayerData): Future[Unit]
}

private trait PlayerDataContext[I <: SqlIdiom, N <: NamingStrategy] {
  this: JdbcContext[I, N] =>

  def loadData = quote {
    (q: EntityQuery[LegendsPlayerData], id: UUID) => q.filter { _.id == id }
  }

  def upsertData = quote {
    (q: EntityQuery[LegendsPlayerData], data: LegendsPlayerData) =>
      q.insert(data).onConflictUpdate(_.id)(
        (t, e) => t.tutorialStep -> e.tutorialStep,
        (t, e) => t.combatClassCooldown -> e.combatClassCooldown,
        (t, e) => t.launchRewardsClaimed -> e.launchRewardsClaimed,
        (t, e) => t.levelRewardsClaimed -> e.levelRewardsClaimed
      )
  }
}

private[legends] class PostgresPlayerDataRepository(
    config: Config
) extends LegendsPlayerDataRepository {
  private val database = new PostgresJdbcContext[SnakeCase](SnakeCase, config)
    with PlayerDataContext[PostgresDialect, SnakeCase]
  import database._

  override def load(id: UUID): Option[LegendsPlayerData] =
    run { loadData(query[LegendsPlayerData], lift(id)) }.headOption

  override def save(data: LegendsPlayerData): Future[Unit] = Future {
    run { upsertData(query[LegendsPlayerData], lift(data)) }
  }
}

private[legends] class SqlitePlayerDataRepository(
    config: Config
) extends LegendsPlayerDataRepository {
  private val database = new SqliteJdbcContext[SnakeCase](SnakeCase, config)
    with PlayerDataContext[SqliteDialect, SnakeCase]
  import database._

  override def load(id: UUID): Option[LegendsPlayerData] =
    run { loadData(query[LegendsPlayerData], lift(id)) }.headOption

  override def save(data: LegendsPlayerData): Future[Unit] = Future {
    run { upsertData(query[LegendsPlayerData], lift(data)) }
  }
}
