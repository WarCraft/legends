/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.shop.profile

import com.typesafe.config.Config
import gg.warcraft.monolith.api.util.future.FutureOps
import io.getquill._
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom

import java.util.UUID
import java.util.logging.Logger
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait UnlockedShopItemRepository {
  def load(playerId: UUID): Set[String]

  def save(id: String, playerId: UUID): Future[Unit]
}

private trait UnlockedShopItemContext[I <: SqlIdiom, N <: NamingStrategy] {
  this: JdbcContext[I, N] =>

  def queryById = quote {
    (q: EntityQuery[UnlockedShopItem], playerId: UUID) =>
      q.filter { _.playerId == playerId }.map { _.id }
  }

  def insert = quote {
    (q: EntityQuery[UnlockedShopItem], item: UnlockedShopItem) => q.insertValue(item)
  }
}

private[legends] class PostgresUnlockedShopItemRepository(
    config: Config
)(implicit
    logger: Logger
) extends UnlockedShopItemRepository {
  private val database = new PostgresJdbcContext[SnakeCase](SnakeCase, config)
    with UnlockedShopItemContext[PostgresDialect, SnakeCase]
  import database._

  override def load(playerId: UUID): Set[String] =
    run { queryById(query[UnlockedShopItem], lift(playerId)) }.toSet

  override def save(id: String, playerId: UUID) = Future[Unit] {
    val item = UnlockedShopItem(id, playerId)
    run { insert(query[UnlockedShopItem], lift(item)) }
  }.andThenLog("UnlockedShopItemRepository", "insert", id, playerId)
}

private[legends] class SqliteUnlockedShopItemRepository(
    config: Config
)(implicit
    logger: Logger
) extends UnlockedShopItemRepository {
  private val database = new SqliteJdbcContext[SnakeCase](SnakeCase, config)
    with UnlockedShopItemContext[SqliteDialect, SnakeCase]
  import database._

  override def load(playerId: UUID): Set[String] =
    run { queryById(query[UnlockedShopItem], lift(playerId)) }.toSet

  override def save(id: String, playerId: UUID) = Future[Unit] {
    val item = UnlockedShopItem(id, playerId)
    run { insert(query[UnlockedShopItem], lift(item)) }
  }.andThenLog("UnlockedShopItemRepository", "insert", id, playerId)
}
