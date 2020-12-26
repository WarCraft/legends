/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.leaderboard

import gg.warcraft.legends.api.LegendsConfig
import gg.warcraft.legends.api.player.statistic.LegendsStatistic.LegendsStatistic
import gg.warcraft.monolith.api.player.statistic.StatisticRepository

class LeaderboardService(implicit statisticRepository: StatisticRepository) {
  private var _leaderboards: Map[LegendsStatistic, Leaderboard] = Map.empty
  def leaderboards: Map[LegendsStatistic, Leaderboard] = _leaderboards

  def readConfig(config: LegendsConfig): Unit = {
    config.leaderboards // TODO
    statisticRepository.queryTop(Leaderboard.maxEntries, statistic)
  }
}
