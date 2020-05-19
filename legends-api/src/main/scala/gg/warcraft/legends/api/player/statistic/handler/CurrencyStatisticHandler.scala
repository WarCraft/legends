package gg.warcraft.legends.api.player.statistic.handler

import gg.warcraft.legends.api.player.currency.LegendsCurrency._
import gg.warcraft.legends.api.player.statistic.LegendsStatistic
import gg.warcraft.legends.api.player.statistic.LegendsStatistic._
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.player.statistic.StatisticService
import gg.warcraft.monolith.api.player.PlayerCurrencyGainedEvent

class CurrencyStatisticHandler(implicit
    service: StatisticService
) extends Event.Handler {
  private final val goldStatistics: List[LegendsStatistic] =
    DAILY_GOLD :: MONTHLY_GOLD :: ALLTIME_GOLD :: Nil
  private final val favorStatistics: List[LegendsStatistic] =
    DAILY_FAVOR :: MONTHLY_FAVOR :: ALLTIME_FAVOR :: Nil

  override def handle(event: Event): Unit = event match {
    case PlayerCurrencyGainedEvent(player, currency, amount, _, _) =>
      currency match { // TODO
//        case Gold.name  => service.increase(player.id, amount, goldStatistics: _*)
//        case Favor.name => service.increase(player.id, amount, favorStatistics: _*)
        case _          =>
      }
  }
}
