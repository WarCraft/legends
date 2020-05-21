package gg.warcraft.legends.api.player.statistic.handler

import gg.warcraft.legends.api.item.crate.LootCrateOpenedEvent
import gg.warcraft.legends.api.player.statistic.LegendsStatistic._
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.player.statistic.StatisticService

class CrateStatisticHandler(implicit
    service: StatisticService
) extends Event.Handler {
  // TODO get current day and month counter for correct statistic
  private final val crateStatistics: List[String] =
    DailyCrates.id :: MonthlyCrates.id :: AlltimeCrates.id :: Nil

  override def handle(event: Event): Unit = event match {
    case LootCrateOpenedEvent(player, _) =>
      service.increaseStatistic(player.id, 1, crateStatistics: _*)
    case _ =>
  }
}
