/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.statistic

import gg.warcraft.monolith.api.player.statistic.Statistic

import java.time.LocalDate
import java.util.UUID

case class StatisticArchive(
    playerId: UUID,
    statistic: String,
    value: Long = 0,
    date: LocalDate
)

object StatisticArchive {
  def apply(statistic: Statistic, day: LocalDate): StatisticArchive =
    StatisticArchive(statistic.playerId, statistic.statistic, statistic.value, day)
}
