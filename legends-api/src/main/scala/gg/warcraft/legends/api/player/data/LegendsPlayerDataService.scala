/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.data

import gg.warcraft.monolith.api.util.chaining.ChainingOps

import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.chaining.scalaUtilChainingOps

class LegendsPlayerDataService(implicit
    repository: LegendsPlayerDataRepository
) {
  private var _data: Map[UUID, LegendsPlayerData] = Map.empty

  def data: Map[UUID, LegendsPlayerData] = _data
  def dataFuture(id: UUID): Future[LegendsPlayerData] =
    _data.get(id) match {
      case Some(data) => data |> Future.successful
      case None => Future {
          repository.load(id) match {
            case Some(data) => data
            case None       => LegendsPlayerData(id)
          }
        }
    }

  def setTutorialStep(playerId: UUID, step: Int): Unit = {
    val outdated = _data(playerId)
    val updated = outdated.copy(tutorialStep = step)
    setData(updated)
  }

  def setCombatClassCooldown(playerId: UUID, cooldown: Long): Unit = {
    val outdated = _data(playerId)
    val updated = outdated.copy(combatClassCooldown = cooldown)
    setData(updated)
  }

  def setLaunchRewardsClaimed(playerId: UUID, claimed: Boolean): Unit = {
    val outdated = _data(playerId)
    val updated = outdated.copy(launchRewardsClaimed = claimed)
    setData(updated)
  }

  def setLevelRewardsClaimed(playerId: UUID, claimed: Int): Unit = {
    val outdated = _data(playerId)
    val updated = outdated.copy(levelRewardsClaimed = claimed)
    setData(updated)
  }

  private[data] def setData(data: LegendsPlayerData): Unit = {
    _data += (data.id -> data)
    repository.save(data)
  }

  private[data] def loadData(id: UUID): Option[LegendsPlayerData] =
    repository.load(id).tap {
      case Some(data) => _data += (data.id -> data)
      case None       =>
    }

  private[data] def invalidateData(id: UUID): Unit =
    _data -= id
}
