/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.leaderboard.handler

import gg.warcraft.legends.api.core.leaderboard.LeaderboardService
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.player.PlayerStatisticsChangedEvent

class LeaderboardUpdateHandler(implicit
    service: LeaderboardService
) extends Event.Handler {
  override def handle(event: Event): Unit = event match {
    case PlayerStatisticsChangedEvent(player, statistic, _, newCurrent) =>
      service.leaderboards(statistic).update(player, newCurrent)
    case _ =>
  }
}
