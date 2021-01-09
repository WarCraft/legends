/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.calendar

import gg.warcraft.legends.api.LegendsConfig
import gg.warcraft.legends.api.item.crate.{LootCrate, LootCrateService}
import gg.warcraft.monolith.api.core.Message
import gg.warcraft.monolith.api.core.data.ServerDataService
import gg.warcraft.monolith.api.item.ItemService
import gg.warcraft.monolith.api.player.{Player, PlayerService}

import java.util.UUID
import scala.util.chaining._

class LoginCalendarService(implicit
    repository: LoginRewardRepository,
    lootCrateService: LootCrateService,
    serverDataService: ServerDataService,
    playerService: PlayerService,
    itemService: ItemService
) {
  private var _rewards: Map[Int, List[LootCrate]] = Map.empty
  def rewards: Map[Int, List[LootCrate]] = _rewards

  private var _rewardsClaimed: Map[UUID, LoginCalendar] = Map.empty
  def rewardsClaimed: Map[UUID, LoginCalendar] = _rewardsClaimed

  def readConfig(config: LegendsConfig): Unit =
    _rewards = config.dailyRewards.zipWithIndex.map { case (rewards, i) =>
      (i + 1) -> lootCrateService.parseRewards(rewards)
    }.toMap

  def claimReward(player: Player): Unit = {
    val outdated = _rewardsClaimed(player.id)
    val today = serverDataService.today
    if (today.isAfter(outdated.claimed)) {
      val updated = outdated.copy(day = outdated.day + 1, claimed = today)
      val rewards = _rewards(updated.day)
      if (player.inventory.hasSpaceFor(rewards: _*)) {
        rewards.foreach { itemService.giveTo(player.id, _) }
        _rewardsClaimed += (player.id -> updated)
        repository.save(updated)

        val rewardString = rewards.map { it =>
          val plural = if (it.count > 1) "s" else ""
          s"${it.count} ${it.name}$plural"
        }
        val message = Message.server(
          s"""Welcome back to WarCraft!
             |You received ${rewardString.mkString(", ")} just for logging in today,
             |we hope you have fun and to see you again tomorrow for even greater rewards.
             |""".stripMargin.replace('\n', ' ')
        )
        playerService.sendMessage(player.id, message)
      } else {
        val message = Message.server(
          s"""Welcome back to WarCraft!
             |You do not have enough space in your inventory to receive today's login rewards.
             |Please free up at least ${rewards.size} slots and relog to claim your rewards!
             |""".stripMargin.replace('\n', ' ')
        )
        playerService.sendMessage(player.id, message)
      }
    }
  }

  private[calendar] def loadRewardsClaimed(playerId: UUID): Unit =
    repository.load(playerId).tap {
      case Some(data) => _rewardsClaimed += (playerId -> data)
      case None       => _rewardsClaimed += (playerId -> LoginCalendar(playerId))
    }

  private[calendar] def invalidateRewardsClaimed(playerId: UUID): Unit =
    _rewardsClaimed -= playerId
}
