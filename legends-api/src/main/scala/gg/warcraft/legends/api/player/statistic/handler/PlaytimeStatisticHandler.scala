/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.statistic.handler

import gg.warcraft.legends.api.player.statistic.LegendsStatistic._
import gg.warcraft.monolith.api.player.statistic.StatisticService
import gg.warcraft.monolith.api.player.{Player, PlayerService}

class PlaytimeStatisticHandler(implicit
    playerService: PlayerService,
    statisticService: StatisticService
) extends Runnable {
  private final val playtimeStatistics: List[LegendsStatistic] =
    DailyPlaytime :: MonthlyPlaytime :: AlltimePlaytime :: Nil

  private var previousOnlinePlayers: List[Player] = Nil

  // TODO this is already handled by monolith, listen for those events and map to these in stead?
  override def run(): Unit = {
    val onlinePlayers = playerService.getOnlinePlayers
    val tickPlayers = onlinePlayers.intersect(previousOnlinePlayers)
    statisticService.increaseStatistics(
      playtimeStatistics,
      10,
      tickPlayers.map { _.id }: _*
    )
    previousOnlinePlayers = onlinePlayers
  }
}
