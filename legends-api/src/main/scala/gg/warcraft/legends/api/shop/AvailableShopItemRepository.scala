/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.shop

import com.typesafe.config.Config
import gg.warcraft.legends.api.util.codecs.legends._
import gg.warcraft.monolith.api.util.codecs.quill._
import io.getquill._
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait AvailableShopItemRepository {
  def loadAvailable()(implicit
      shopService: ShopService
  ): List[ShopItem]

  def saveAvailable(items: List[ShopItem]): Future[Unit]

  def deleteAvailable(): Future[Unit]
}

private trait AvailableShopItemContext[I <: SqlIdiom, N <: NamingStrategy] {
  this: JdbcContext[I, N] =>

  def queryAvailable =
    quote { (q: EntityQuery[AvailableShopItem]) => q.map { _.item } }

  def insertAvailable =
    quote { (q: EntityQuery[AvailableShopItem], item: AvailableShopItem) =>
      q.insert(item)
    }

  def _deleteAvailable =
    quote { (q: EntityQuery[AvailableShopItem]) => q.delete }
}

private[legends] class PostgresAvailableShopItemRepository(
    config: Config
) extends AvailableShopItemRepository {
  private val database = new PostgresJdbcContext[SnakeCase](SnakeCase, config)
    with AvailableShopItemContext[PostgresDialect, SnakeCase]
  import database._

  override def loadAvailable()(implicit
      shopService: ShopService
  ): List[ShopItem] =
    run { queryAvailable(query[AvailableShopItem]) }

  override def saveAvailable(items: List[ShopItem]): Future[Unit] = Future {
    val availableItems = items.map(AvailableShopItem)
    run {
      liftQuery(availableItems).foreach {
        insertAvailable(query[AvailableShopItem], _)
      }
    }
  }

  override def deleteAvailable(): Future[Unit] = Future {
    run { _deleteAvailable(query[AvailableShopItem]) }
  }
}

private[legends] class SqliteAvailableShopItemRepository(
    config: Config
) extends AvailableShopItemRepository {
  private val database = new SqliteJdbcContext[SnakeCase](SnakeCase, config)
    with AvailableShopItemContext[SqliteDialect, SnakeCase]
  import database._

  override def loadAvailable()(implicit
      shopService: ShopService
  ): List[ShopItem] =
    run { queryAvailable(query[AvailableShopItem]) }

  override def saveAvailable(items: List[ShopItem]): Future[Unit] = Future {
    val availableItems = items.map(AvailableShopItem)
    run {
      liftQuery(availableItems).foreach {
        insertAvailable(query[AvailableShopItem], _)
      }
    }
  }

  override def deleteAvailable(): Future[Unit] = Future {
    run { _deleteAvailable(query[AvailableShopItem]) }
  }
}
