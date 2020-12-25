/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.statistic.handler

import gg.warcraft.legends.api.core.game.WarStartedEvent
import gg.warcraft.legends.api.player.statistic.StatisticArchiveService
import gg.warcraft.monolith.api.core.DailyTickEvent
import gg.warcraft.monolith.api.core.event.Event

class StatisticArchiveHandler(implicit
    service: StatisticArchiveService
) extends Event.Handler {
  override def handle(event: Event): Unit = event match {
    case DailyTickEvent(_, yesterday) =>
      service.archiveDailies(yesterday)
    case WarStartedEvent(war) =>
      val lastMonth = war.startTime.toLocalDate.minusMonths(1).withDayOfMonth(1)
      service.archiveMonthlies(lastMonth)
    case _ =>
  }
}
