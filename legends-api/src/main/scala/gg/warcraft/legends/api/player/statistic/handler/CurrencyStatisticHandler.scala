/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.statistic.handler

import gg.warcraft.legends.api.player.currency.LegendsCurrency._
import gg.warcraft.legends.api.player.statistic.LegendsStatistic._
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.player.statistic.StatisticService
import gg.warcraft.monolith.api.player.PlayerCurrencyGainedEvent

class CurrencyStatisticHandler(implicit
    service: StatisticService
) extends Event.Handler {
  import service._

  private final val goldStatistics: List[String] =
    DailyGold.id :: MonthlyGold.id :: AlltimeGold.id :: Nil
  private final val favorStatistics: List[String] =
    DailyFavor.id :: MonthlyFavor.id :: AlltimeFavor.id :: Nil

  override def handle(event: Event): Unit = event match {
    case PlayerCurrencyGainedEvent(player, currency, amount, _, _) =>
      currency match {
        case Gold.name  => increaseStatistic(player.id, amount, goldStatistics: _*)
        case Favor.name => increaseStatistic(player.id, amount, favorStatistics: _*)
        case _          =>
      }
  }
}
