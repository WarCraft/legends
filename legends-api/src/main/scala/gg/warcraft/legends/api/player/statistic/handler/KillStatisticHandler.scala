/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.statistic.handler

import gg.warcraft.legends.api.player.statistic.LegendsStatistic._
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.entity.EntityFatalDamageEvent
import gg.warcraft.monolith.api.player.Player
import gg.warcraft.monolith.api.player.statistic.StatisticService

class KillStatisticHandler(implicit
    statisticService: StatisticService
) extends Event.Handler {
  private final val killStatistics: List[String] =
    DailyKills.id :: MonthlyKills.id :: AlltimeKills.id :: Nil

  override def handle(event: Event): Unit = event match {
    case EntityFatalDamageEvent(entity: Player, _) =>
//      val killers = getAllPeopleWhoAttackedPlayerOverLastXSeconds TODO
//      statisticService.increaseStatistic(killers, 1, killStatistics: _*)
    case _ =>
  }
}
