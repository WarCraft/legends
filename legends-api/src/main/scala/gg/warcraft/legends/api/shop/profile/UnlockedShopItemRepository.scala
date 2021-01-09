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
import java.util.logging.Logger
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

trait UnlockedShopItemRepository {
  def loadItems(playerId: UUID): Set[String]

  def saveItem(id: String, playerId: UUID): Future[Unit]
}

private trait UnlockedShopItemContext[I <: SqlIdiom, N <: NamingStrategy] {
  this: JdbcContext[I, N] =>

  def queryItems = quote {
    (q: EntityQuery[UnlockedShopItem], playerId: UUID) =>
      q.filter { _.playerId == playerId }.map { _.id }
  }

  def insertItem = quote {
    (q: EntityQuery[UnlockedShopItem], item: UnlockedShopItem) => q.insert(item)
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

  override def loadItems(playerId: UUID): Set[String] =
    run { queryItems(query[UnlockedShopItem], lift(playerId)) }.toSet

  override def saveItem(id: String, playerId: UUID): Future[Unit] = Future {
    val item = UnlockedShopItem(id, playerId)
    run { insertItem(query[UnlockedShopItem], lift(item)) }
  }
//    .andThen { TODO explore database error reporting
//    case Failure(exception) =>
//      logger.severe(exception.getMessage)
//      logger.severe(s"Failed to persist UnlockedShopItemRepository::saveItem($id, $playerId)")
//    case Success(value)     =>
//  }.asInstanceOf[Future[Unit]]
}

private[legends] class SqliteUnlockedShopItemRepository(
    config: Config
) extends UnlockedShopItemRepository {
  private val database = new SqliteJdbcContext[SnakeCase](SnakeCase, config)
    with UnlockedShopItemContext[SqliteDialect, SnakeCase]
  import database._

  override def loadItems(playerId: UUID): Set[String] =
    run { queryItems(query[UnlockedShopItem], lift(playerId)) }.toSet

  override def saveItem(id: String, playerId: UUID): Future[Unit] = Future {
    val item = UnlockedShopItem(id, playerId)
    run { insertItem(query[UnlockedShopItem], lift(item)) }
  }
}
