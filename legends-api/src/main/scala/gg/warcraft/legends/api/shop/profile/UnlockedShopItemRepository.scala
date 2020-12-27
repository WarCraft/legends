/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.shop.profile

import com.typesafe.config.Config
import io.getquill._
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom

import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait UnlockedShopItemRepository {
  def loadItems(playerId: UUID): Set[String]

  def saveItem(id: String, playerId: UUID): Future[Unit]
}

private trait UnlockedShopItemContext[I <: SqlIdiom, N <: NamingStrategy] {
  this: JdbcContext[I, N] =>

  def queryItems(playerId: UUID)(implicit
      q: EntityQuery[UnlockedShopItem]
  ) =
    quote { q.filter { _.playerId == playerId }.map { _.id } }

  def insertItem(item: UnlockedShopItem)(implicit
      q: EntityQuery[UnlockedShopItem]
  ) =
    quote { q.insert(item) }
}

private[monolith] class PostgresUnlockedShopItemRepository(
    config: Config
) extends UnlockedShopItemRepository {
  private val database = new PostgresJdbcContext[SnakeCase](SnakeCase, config)
    with UnlockedShopItemContext[PostgresDialect, SnakeCase]
  import database._

  private implicit val q: EntityQuery[UnlockedShopItem] = query[UnlockedShopItem]

  override def loadItems(playerId: UUID): Set[String] =
    run { queryItems(lift(playerId)) }.toSet

  override def saveItem(id: String, playerId: UUID): Future[Unit] = Future {
    val item = UnlockedShopItem(id, playerId)
    run { insertItem(lift(item)) }
  }
}

private[monolith] class SqliteUnlockedShopItemRepository(
    config: Config
) extends UnlockedShopItemRepository {
  private val database = new SqliteJdbcContext[SnakeCase](SnakeCase, config)
    with UnlockedShopItemContext[SqliteDialect, SnakeCase]
  import database._

  private implicit val q: EntityQuery[UnlockedShopItem] = query[UnlockedShopItem]

  override def loadItems(playerId: UUID): Set[String] =
    run { queryItems(lift(playerId)) }.toSet

  override def saveItem(id: String, playerId: UUID): Future[Unit] = Future {
    val item = UnlockedShopItem(id, playerId)
    run { insertItem(lift(item)) }
  }
}
