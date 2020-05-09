package gg.warcraft.legends.api.player.statistic.handler

import gg.warcraft.legends.api.player.currency.LegendsCurrency
import gg.warcraft.legends.api.player.statistic.LegendsStatistic
import gg.warcraft.legends.api.player.statistic.LegendsStatistic._
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.player.statistic.StatisticService
import gg.warcraft.monolith.api.player.PlayerCurrencyGainedEvent

class CurrencyStatisticHandler(implicit
    statisticService: StatisticService
) extends Event.Handler {
  private final val goldStatistics: List[LegendsStatistic] =
    List(DAILY_GOLD, MONTHLY_GOLD, ALLTIME_GOLD)
  private final val favorStatistics: List[LegendsStatistic] =
    List(DAILY_FAVOR, MONTHLY_FAVOR, ALLTIME_FAVOR)

  override def handle(event: Event): Unit = event match {
    case PlayerCurrencyGainedEvent(playerId, currency, amount, _, _) =>
      currency match {
        case LegendsCurrency.GOLD.name =>
          statisticService.increaseStatistic(player.id, amount, goldStatistics: _*)
        case LegendsCurrency.FAVOR.name =>
          statisticService.increaseStatistic(player.id, amount, favorStatistics: _*)
        case _ =>
      }
  }
}
