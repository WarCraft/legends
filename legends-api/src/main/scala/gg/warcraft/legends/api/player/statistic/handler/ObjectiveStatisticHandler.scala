/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.statistic.handler

import gg.warcraft.legends.api.player.statistic.LegendsStatistic._
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.player.statistic.StatisticService
import gg.warcraft.monolith.api.util.option._
import gg.warcraft.siege.api.objective.ObjectiveCaptureEvent

class ObjectiveStatisticHandler(implicit
    statisticService: StatisticService
) extends Event.Handler {
  private final val fortStatistics: List[LegendsStatistic] =
    DailyForts :: MonthlyForts :: AlltimeForts :: Nil

  private final val strongholdStatistics: List[LegendsStatistic] =
    DailyStrongholds :: MonthlyStrongholds :: AlltimeStrongholds :: Nil

  override def handle(event: Event): Unit = event match {
    case ObjectiveCaptureEvent(objective, _, attackers) =>
      val statistics: Option[List[LegendsStatistic]] = objective match {
        case it if it.id.contains("FORT")       => fortStatistics
        case it if it.id.contains("STRONGHOLD") => strongholdStatistics
        case _                                  => None
      }
      statistics.foreach { statistics =>
        statisticService.increaseStatistics(statistics, 1, attackers.map { _.id })
      }
    case _ =>
  }
}
