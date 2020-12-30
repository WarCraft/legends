/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.statistic.handler

import gg.warcraft.legends.api.item.crate.LootCrateOpenedEvent
import gg.warcraft.legends.api.player.statistic.LegendsStatistic._
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.player.statistic.StatisticService

class CrateStatisticHandler(implicit
    service: StatisticService
) extends Event.Handler {
  private final val crateStatistics: List[String] =
    DailyCrates :: MonthlyCrates :: AlltimeCrates :: Nil

  override def handle(event: Event): Unit = event match {
    case LootCrateOpenedEvent(player, _) =>
      service.increaseStatistics(crateStatistics, 1, player.id)
    case _ =>
  }
}
