/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.leaderboard

import gg.warcraft.legends.api.LegendsConfig
import gg.warcraft.legends.api.player.statistic.LegendsStatistic.LegendsStatistic
import gg.warcraft.monolith.api.player.PlayerService
import gg.warcraft.monolith.api.player.statistic.StatisticRepository

class LeaderboardService(implicit
    statisticRepository: StatisticRepository,
    playerService: PlayerService
) {
  private var _leaderboards: Map[LegendsStatistic, Leaderboard] = Map.empty
  def leaderboards: Map[LegendsStatistic, Leaderboard] = _leaderboards

  def readConfig(config: LegendsConfig): Unit =
    _leaderboards = config.leaderboards.leaderboards.map { it =>
      it.statistic -> new Leaderboard(it.statistic, it.name, it.description)
    }.toMap
}
