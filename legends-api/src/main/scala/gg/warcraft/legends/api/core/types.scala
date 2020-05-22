package gg.warcraft.legends.api.core

import io.getquill.{SnakeCase, SqliteDialect}
import io.getquill.context.jdbc.JdbcContext

object types {
  private[legends] type DatabaseContext = JdbcContext[SqliteDialect, SnakeCase]
}
