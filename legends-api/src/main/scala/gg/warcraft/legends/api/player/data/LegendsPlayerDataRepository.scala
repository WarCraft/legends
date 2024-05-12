/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.data

import com.typesafe.config.Config
import gg.warcraft.monolith.api.util.future.FutureOps
import io.getquill._
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom

import java.util.UUID
import java.util.logging.Logger
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait LegendsPlayerDataRepository {
  def load(id: UUID): Option[LegendsPlayerData]

  def save(data: LegendsPlayerData): Future[Unit]
}

private trait PlayerDataContext[I <: SqlIdiom, N <: NamingStrategy] {
  this: JdbcContext[I, N] =>

  def queryById = quote {
    (q: EntityQuery[LegendsPlayerData], id: UUID) => q.filter { _.id == id }
  }

  def upsert = quote {
    (q: EntityQuery[LegendsPlayerData], data: LegendsPlayerData) =>
      q.insertValue(data).onConflictUpdate(_.id)(
        (t, e) => t.tutorialStep -> e.tutorialStep,
        (t, e) => t.combatClassCooldown -> e.combatClassCooldown,
        (t, e) => t.launchRewardsClaimed -> e.launchRewardsClaimed,
        (t, e) => t.levelRewardsClaimed -> e.levelRewardsClaimed
      )
  }
}

private[legends] class PostgresLegendsPlayerDataRepository(
    config: Config
)(implicit
    logger: Logger
) extends LegendsPlayerDataRepository {
  private val database = new PostgresJdbcContext[SnakeCase](SnakeCase, config)
    with PlayerDataContext[PostgresDialect, SnakeCase]
  import database._

  override def load(id: UUID): Option[LegendsPlayerData] =
    run { queryById(query[LegendsPlayerData], lift(id)) }.headOption

  override def save(data: LegendsPlayerData) = Future[Unit] {
    run { upsert(query[LegendsPlayerData], lift(data)) }
  }.andThenLog("LegendsPlayerDataRepository", "upsert", data)
}

private[legends] class SqliteLegendsPlayerDataRepository(
    config: Config
)(implicit
    logger: Logger
) extends LegendsPlayerDataRepository {
  private val database = new SqliteJdbcContext[SnakeCase](SnakeCase, config)
    with PlayerDataContext[SqliteDialect, SnakeCase]
  import database._

  override def load(id: UUID): Option[LegendsPlayerData] =
    run { queryById(query[LegendsPlayerData], lift(id)) }.headOption

  override def save(data: LegendsPlayerData) = Future[Unit] {
    run { upsert(query[LegendsPlayerData], lift(data)) }
  }.andThenLog("LegendsPlayerDataRepository", "upsert", data)
}
