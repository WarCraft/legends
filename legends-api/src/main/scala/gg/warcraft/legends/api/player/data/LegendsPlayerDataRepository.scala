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

  def loadData(id: UUID)(implicit
      query: EntityQuery[LegendsPlayerData]
  ) = quote {
    query.filter { _.id == lift(id) }
  }

  def upsertData(data: LegendsPlayerData)(implicit
      query: EntityQuery[LegendsPlayerData]
  ) = quote {
    query.insert(lift(data)).onConflictUpdate(_.id)(
      (t, e) => t.tutorialStep -> e.tutorialStep,
      (t, e) => t.combatClassCooldown -> e.combatClassCooldown,
      (t, e) => t.launchRewardsClaimed -> e.launchRewardsClaimed,
      (t, e) => t.levelRewardsClaimed -> e.levelRewardsClaimed
    )
  }
}

private[monolith] class PostgresPlayerDataRepository(
    config: Config
) extends LegendsPlayerDataRepository {
  private val database = new PostgresJdbcContext[SnakeCase](SnakeCase, config)
    with PlayerDataContext[PostgresDialect, SnakeCase]
  import database._

  private implicit val query: EntityQuery[LegendsPlayerData] =
    query[LegendsPlayerData]

  override def load(id: UUID): Option[LegendsPlayerData] =
    run { loadData(id) }.headOption

  override def save(data: LegendsPlayerData): Future[Unit] =
    Future { run { upsertData(data) } }
}

private[monolith] class SqlitePlayerDataRepository(
    config: Config
) extends LegendsPlayerDataRepository {
  private val database = new SqliteJdbcContext[SnakeCase](SnakeCase, config)
    with PlayerDataContext[SqliteDialect, SnakeCase]
  import database._

  private implicit val query: EntityQuery[LegendsPlayerData] =
    query[LegendsPlayerData]

  override def load(id: UUID): Option[LegendsPlayerData] =
    run { loadData(id) }.headOption

  override def save(data: LegendsPlayerData): Future[Unit] =
    Future { run { upsertData(data) } }
}
