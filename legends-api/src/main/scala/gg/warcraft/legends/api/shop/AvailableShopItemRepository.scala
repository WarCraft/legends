/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.shop

import com.typesafe.config.Config
import gg.warcraft.legends.api.util.quill._
import io.getquill._
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait AvailableShopItemRepository {
  def loadAvailable(): List[ShopItem]

  def saveAvailable(items: List[ShopItem]): Future[Unit]

  def deleteAvailable(): Future[Unit]
}

private trait AvailableShopItemContext[I <: SqlIdiom, N <: NamingStrategy] {
  this: JdbcContext[I, N] =>

  def queryAvailable(implicit
      q: Quoted[EntityQuery[AvailableShopItem]]
  ) =
    quote { q.map { _.item } }

  def insertAvailable(item: AvailableShopItem)(implicit
      q: Quoted[EntityQuery[AvailableShopItem]]
  ) =
    quote { q.insert(item) }

  def _deleteAvailable(implicit
      q: Quoted[EntityQuery[AvailableShopItem]]
  ) =
    quote { q.delete }
}

private[legends] class PostgresAvailableShopItemRepository(
    config: Config
)(implicit
    shopService: ShopService
) extends AvailableShopItemRepository {
  private val database = new PostgresJdbcContext[SnakeCase](SnakeCase, config)
    with AvailableShopItemContext[PostgresDialect, SnakeCase]
  import database._

  private implicit val decoder: MappedEncoding[String, ShopItem] = shopItemDecoder

  private implicit val q: Quoted[EntityQuery[AvailableShopItem]] =
    quote { query[AvailableShopItem] }

  override def loadAvailable(): List[ShopItem] =
    run { queryAvailable }

  override def saveAvailable(items: List[ShopItem]): Future[Unit] = Future {
    val availableItems = items.map(AvailableShopItem)
    run { liftQuery(availableItems).foreach { insertAvailable(_) } }
  }

  override def deleteAvailable(): Future[Unit] = Future {
    run { _deleteAvailable }
  }
}

private[legends] class SqliteAvailableShopItemRepository(
    config: Config
)(implicit
    shopService: ShopService
) extends AvailableShopItemRepository {
  private val database = new SqliteJdbcContext[SnakeCase](SnakeCase, config)
    with AvailableShopItemContext[SqliteDialect, SnakeCase]
  import database._

  private implicit val decoder: MappedEncoding[String, ShopItem] = shopItemDecoder

  private implicit val q: Quoted[EntityQuery[AvailableShopItem]] =
    quote { query[AvailableShopItem] }

  override def loadAvailable(): List[ShopItem] =
    run { queryAvailable }

  override def saveAvailable(items: List[ShopItem]): Future[Unit] = Future {
    val availableItems = items.map(AvailableShopItem)
    run { liftQuery(availableItems).foreach { insertAvailable(_) } }
  }

  override def deleteAvailable(): Future[Unit] = Future {
    run { _deleteAvailable }
  }
}
