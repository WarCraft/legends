/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.leaderboard

import gg.warcraft.legends.api.player.statistic.LegendsStatistic.LegendsStatistic
import gg.warcraft.monolith.api.player.statistic.StatisticRepository
import gg.warcraft.monolith.api.player.{OfflinePlayer, Player, PlayerService}

import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class Leaderboard(
    val statistic: LegendsStatistic,
    val name: String,
    val description: String
)(implicit
    statisticRepository: StatisticRepository,
    playerService: PlayerService
) {
  private final val maxEntries = 100

  private var _rankings: Array[(OfflinePlayer, Long)] = Array(maxEntries)
  private var _rankingsCache: List[(OfflinePlayer, Long)] = _
  def rankings: List[(OfflinePlayer, Long)] = {
    if (_rankingsCache == null) _rankingsCache = _rankings.toList
    _rankingsCache
  }

  private var _players: Map[UUID, Int] = Map.empty
  def players: Map[UUID, Int] = _players

  val isDaily: Boolean = statistic.startsWith("DAILY_")
  val isMonthly: Boolean = statistic.startsWith("MONTHLY_")

  statisticRepository.queryTop(maxEntries, statistic).onComplete {
    case Success(rankings) =>
      rankings.zipWithIndex.foreach { it =>
        val player = playerService.getOfflinePlayer(it._1.playerId)
        _rankings(it._2) = (player, it._1.value)
        _players += (player.id -> it._2)
      }
    case Failure(exception) =>
  }

  private[leaderboard] def clear(): Unit = {
    _rankings = Array(maxEntries)
    _rankingsCache = null
    _players = Map.empty
  }

  private[leaderboard] def update(player: Player, value: Long): Unit =
    _players.get(player.id) match {
      case Some(index) =>
        val newIndex = findIndexFor(value, index)
        if (newIndex != index) {
          val offlinePlayer = _rankings(index)._1
          updateIndices(offlinePlayer, newIndex, value, updateInPlace = true)
        } else _rankings(index) = _rankings(index).copy(_2 = value)
      case None =>
        if (_players.size < maxEntries || value > _rankings.last._2) {
          val newIndex = findIndexFor(value, lastIndex + 1)
          val offlinePlayer = playerService.getOfflinePlayer(player.id)
          updateIndices(offlinePlayer, newIndex, value).foreach { _players -= _ }
        }
    }

  private def lastIndex: Int = _players.size - 1

  private def findIndexFor(value: Long, from: Int): Int = {
    var index = from
    while (index > 0 && value > _rankings(index - 1)._2) index -= 1
    index
  }

  private def updateIndices(
      player: OfflinePlayer,
      newIndex: Int,
      value: Long,
      updateInPlace: Boolean = false
  ): Option[UUID] = {
    var removed: Option[UUID] = None
    if (!updateInPlace && newIndex <= lastIndex) {
      var copyLength = lastIndex - newIndex
      if (_players.size < maxEntries) copyLength += 1
      else removed = Some(_rankings.last._1.id)
      Array.copy(_rankings, newIndex, _rankings, newIndex + 1, copyLength)
    }

    _rankings(newIndex) = (player, value)
    _rankingsCache = null
    _players += (player.id -> newIndex)
    removed
  }
}
