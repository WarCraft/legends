/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.statistic

import gg.warcraft.monolith.api.player.statistic.StatisticRepository

import java.time.LocalDate
import scala.concurrent.ExecutionContext.Implicits.global

class StatisticArchiveService(implicit
    statisticRepository: StatisticRepository,
    statisticArchiveRepository: StatisticArchiveRepository
) {

  def archiveDailies(day: LocalDate): Unit =
    archiveStatistics("DAILY%", day)

  def archiveMonthlies(day: LocalDate): Unit =
    archiveStatistics("MONTHLY%", day)

  private def archiveStatistics(like: String, day: LocalDate): Unit =
    statisticRepository
      .queryAll(like)
      .map { _.map { StatisticArchive(_, day) } }
      .flatMap(statisticArchiveRepository.save)
      .flatMap { _ => statisticRepository.deleteAll(like) }
}
