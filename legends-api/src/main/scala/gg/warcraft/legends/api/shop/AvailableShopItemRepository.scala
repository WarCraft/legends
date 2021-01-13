/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.shop

import com.typesafe.config.Config
import gg.warcraft.legends.api.util.codecs.legends._
import gg.warcraft.monolith.api.util.codecs.quill._
import gg.warcraft.monolith.api.util.future.FutureOps
import io.getquill._
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom

import java.util.logging.Logger
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait AvailableShopItemRepository {
  def loadAll()(implicit
      shopService: ShopService
  ): List[ShopItem]

  def save(items: List[ShopItem]): Future[Unit]

  def deleteAll(): Future[Unit]
}

private trait AvailableShopItemContext[I <: SqlIdiom, N <: NamingStrategy] {
  this: JdbcContext[I, N] =>

  def queryAll =
    quote { (q: EntityQuery[AvailableShopItem]) => q }

  def insert =
    quote { (q: EntityQuery[AvailableShopItem], item: AvailableShopItem) =>
      q.insert(item)
    }
}

private[legends] class PostgresAvailableShopItemRepository(
    config: Config
)(implicit
    logger: Logger
) extends AvailableShopItemRepository {
  private val database = new PostgresJdbcContext[SnakeCase](SnakeCase, config)
    with AvailableShopItemContext[PostgresDialect, SnakeCase]
  import database._

  override def loadAll()(implicit
      shopService: ShopService
  ): List[ShopItem] =
    run { queryAll(query[AvailableShopItem]) }
      .map { it => shopService.itemsById(it.id) }

  override def save(items: List[ShopItem]) = Future[Unit] {
    val availableItems = items.map { _.id }.map(AvailableShopItem)
    run { liftQuery(availableItems).foreach { insert(query[AvailableShopItem], _) } }
  }.andThenLog("AvailableShopItemRepository", "insert", items)

  override def deleteAll() = Future[Unit] {
    run { queryAll(query[AvailableShopItem]).delete }
  }.andThenLog("AvailableShopItemRepository", "deleteAll")
}

private[legends] class SqliteAvailableShopItemRepository(
    config: Config
)(implicit
    logger: Logger
) extends AvailableShopItemRepository {
  private val database = new SqliteJdbcContext[SnakeCase](SnakeCase, config)
    with AvailableShopItemContext[SqliteDialect, SnakeCase]
  import database._

  override def loadAll()(implicit
      shopService: ShopService
  ): List[ShopItem] =
    run { queryAll(query[AvailableShopItem]) }
      .map { it => shopService.itemsById(it.id) }

  override def save(items: List[ShopItem]) = Future[Unit] {
    val availableItems = items.map { _.id }.map(AvailableShopItem)
    run { liftQuery(availableItems).foreach { insert(query[AvailableShopItem], _) } }
  }.andThenLog("AvailableShopItemRepository", "insert", items)

  override def deleteAll() = Future[Unit] {
    run { queryAll(query[AvailableShopItem]).delete }
  }.andThenLog("AvailableShopItemRepository", "deleteAll")
}
