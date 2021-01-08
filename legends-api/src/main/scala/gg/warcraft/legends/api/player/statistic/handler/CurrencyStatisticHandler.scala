/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.statistic.handler

import gg.warcraft.legends.api.player.currency.LegendsCurrency._
import gg.warcraft.legends.api.player.statistic.LegendsStatistic._
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.player.PlayerCurrencyGainedEvent
import gg.warcraft.monolith.api.player.statistic.StatisticService

class CurrencyStatisticHandler(implicit
    service: StatisticService
) extends Event.Handler {
  import service._

  private final val goldStatistics: List[String] =
    DailyGold :: MonthlyGold :: AlltimeGold :: Nil
  private final val favorStatistics: List[String] =
    DailyFavor :: MonthlyFavor :: AlltimeFavor :: Nil

  override def handle(event: Event): Unit = event match {
    case PlayerCurrencyGainedEvent(player, currency, amount, _, _) =>
      currency match {
        case Gold  => increaseStatistics(goldStatistics, amount, player.id)
        case Favor => increaseStatistics(favorStatistics, amount, player.id)
        case _     =>
      }
    case _ =>
  }
}
