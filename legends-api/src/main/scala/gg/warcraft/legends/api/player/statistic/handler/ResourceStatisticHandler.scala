package gg.warcraft.legends.api.player.statistic.handler

import gg.warcraft.legends.api.player.statistic.LegendsStatistic
import gg.warcraft.legends.api.player.statistic.LegendsStatistic._
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.player.statistic.StatisticService

class ResourceStatisticHandler(implicit
    statisticService: StatisticService
) extends Event.Handler {
  private final val resourceStatistics: List[LegendsStatistic] =
    DAILY_RESOURCES :: MONTHLY_RESOURCES :: ALLTIME_RESOURCES :: Nil

  override def handle(event: Event): Unit = event match {
//    case BlockGatheredEvent | EntityGatheredEvent => TODO
//      statisticService.increaseStatistic(player.id, 1, resourceStatistics: _*)
    case _ =>
  }
}
