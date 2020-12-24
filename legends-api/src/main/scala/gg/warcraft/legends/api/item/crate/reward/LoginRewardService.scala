/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.crate.reward

import gg.warcraft.legends.api.LegendsConfig
import gg.warcraft.legends.api.item.crate.{LootCrate, LootCrateService}
import gg.warcraft.monolith.api.core.data.ServerDataService
import gg.warcraft.monolith.api.item.ItemService
import gg.warcraft.monolith.api.player.PlayerService

import java.time.LocalDate
import java.util.UUID
import scala.util.chaining._

class LoginRewardService(implicit
    repository: LoginRewardRepository,
    lootCrateService: LootCrateService,
    serverDataService: ServerDataService,
    playerService: PlayerService,
    itemService: ItemService
) {
  private val timeZone = serverDataService.serverTimeZone

  private var _rewards: Map[Int, LootCrate] = Map.empty
  def rewards: Map[Int, LootCrate] = _rewards

  private var _rewardsClaimed: Map[UUID, LoginRewardsClaimed] = Map.empty
  def rewardsClaimed: Map[UUID, LoginRewardsClaimed] = _rewardsClaimed

  def readConfig(config: LegendsConfig): Unit =
    config.dailyRewards.foreach { it =>
      val crate = lootCrateService.crates(it.crateId)
      val reward = crate.copy(count = it.count)
      val day = _rewards.size + 1
      _rewards += (day -> reward)
    }

  def claimReward(playerId: UUID): Unit = {
    val outdated = _rewardsClaimed(playerId)
    val today = LocalDate.now(timeZone)
    if (today.isAfter(outdated.date)) {
      val updated = outdated.copy(counter = outdated.counter + 1, date = today)
      val rewards = _rewards(updated.counter)
      itemService.giveTo(playerId, rewards)

      _rewardsClaimed += (playerId -> updated)
      repository.save(updated)
    }
  }

  private[reward] def loadRewardsClaimed(playerId: UUID): Unit =
    repository.load(playerId).tap {
      case Some(data) => _rewardsClaimed += (playerId -> data)
      case None       => _rewardsClaimed += (playerId -> LoginRewardsClaimed(playerId))
    }.tap { _ => claimReward(playerId) }

  private[reward] def invalidateRewardsClaimed(playerId: UUID): Unit =
    _rewardsClaimed -= playerId
}
