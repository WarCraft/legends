/*
 * MIT License
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package gg.warcraft.legends.api.core.data

import com.typesafe.config.Config
import gg.warcraft.monolith.api.util.codecs.quill._
import gg.warcraft.monolith.api.util.future.FutureOps
import io.getquill._
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom

import java.util.logging.Logger
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait LegendsServerDataRepository {
  def load: Option[LegendsServerData]
  def save(data: LegendsServerData): Future[Unit]
}

private trait LegendsServerDataContext[I <: SqlIdiom, N <: NamingStrategy] {
  this: JdbcContext[I, N] =>

  def queryAll = quote {
    (q: EntityQuery[LegendsServerData]) => q
  }

  def update = quote {
    (q: EntityQuery[LegendsServerData], data: LegendsServerData) => q.update(data)
  }
}

private[legends] class PostgresLegendsServerDataRepository(
    config: Config
)(implicit
    logger: Logger
) extends LegendsServerDataRepository {
  private val database = new PostgresJdbcContext[SnakeCase](SnakeCase, config)
    with LegendsServerDataContext[PostgresDialect, SnakeCase]
  import database._

  override def load: Option[LegendsServerData] =
    run { queryAll(query[LegendsServerData]) }.headOption

  override def save(data: LegendsServerData) = Future[Unit] {
    run { update(query[LegendsServerData], lift(data)) }
  }.andThenLog("LegendsServerDataRepository", "update", data)
}

private[legends] class SqliteLegendsServerDataRepository(
    config: Config
)(implicit
    logger: Logger
) extends LegendsServerDataRepository {
  private val database = new SqliteJdbcContext[SnakeCase](SnakeCase, config)
    with LegendsServerDataContext[SqliteDialect, SnakeCase]
  import database._

  override def load: Option[LegendsServerData] =
    run { queryAll(query[LegendsServerData]) }.headOption

  override def save(data: LegendsServerData) = Future[Unit] {
    run { update(query[LegendsServerData], lift(data)) }
  }.andThenLog("LegendsServerDataRepository", "update", data)
}
