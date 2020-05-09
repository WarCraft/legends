package gg.warcraft.legends.api.player.statistic.handler

import gg.warcraft.legends.api.player.statistic.LegendsStatistic
import gg.warcraft.legends.api.player.statistic.LegendsStatistic._
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.player.statistic.StatisticService

class CrateStatisticHandler(implicit
    statisticService: StatisticService
) extends Event.Handler {
  private final val crateStatistics: List[LegendsStatistic] =
    List(DAILY_CRATES, MONTHLY_CRATES, ALLTIME_CRATES)

  override def handle(event: Event): Unit = event match {
    case LLootCrateOpenedEvent(player) =>
      statisticService.increaseStatistic(player.id, 1, crateStatistics: _*)
  }
}
