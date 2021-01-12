/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.statistic.handler

import gg.warcraft.legends.api.core.game.WarStartedEvent
import gg.warcraft.monolith.api.core.DailyTickEvent
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.player.statistic.archive.StatisticArchiveService
import gg.warcraft.monolith.api.util.types.tags.MonolithDateTag
import shapeless.tag

import java.time.LocalDate

class StatisticArchiveHandler(implicit
    service: StatisticArchiveService
) extends Event.Handler {
  override def handle(event: Event): Unit = event match {
    case DailyTickEvent(_, yesterday) =>
      service.archiveStatistics("DAILY%", yesterday)
    case WarStartedEvent(war) =>
      val lastMonth = war.startTime.toLocalDate.minusMonths(1).withDayOfMonth(1)
      val lastMonolithMonth = tag[MonolithDateTag][LocalDate](lastMonth)
      service.archiveStatistics("MONTHLY%", lastMonolithMonth)
    case _ =>
  }
}
