/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.leaderboard.handler

import gg.warcraft.legends.api.LegendsConfig
import gg.warcraft.legends.api.core.game.WarStartedEvent
import gg.warcraft.legends.api.core.leaderboard.{Leaderboard, LeaderboardService}
import gg.warcraft.legends.api.item.crate.LootCrateService
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.core.{DailyTickEvent, Message}
import gg.warcraft.monolith.api.item.{Chest, ItemService}
import gg.warcraft.monolith.api.player.PlayerService

class LeaderboardRewardHandler(config: LegendsConfig)(implicit
    leaderboardService: LeaderboardService,
    lootCrateService: LootCrateService,
    itemService: ItemService,
    playerService: PlayerService
) extends Event.Handler {
  private final val appendix: List[String] =
    "st" :: "nd" :: "rd" :: "th" :: "th" :: "th" :: "th" :: "th" :: "th" :: "th" :: Nil

  private val dailyRewards = config.leaderboards.dailyPrizes
    .map { it => lootCrateService.crates(it.crate).copy(count = it.count) }
  private val monthlyRewards = config.leaderboards.monthlyPrizes
    .map { it => lootCrateService.crates(it.crate).copy(count = it.count) }

  override def handle(event: Event): Unit = event match {
    case DailyTickEvent(_, _) =>
      leaderboardService.leaderboards.values
        .filter { _.isDaily }
        .foreach { awardPrizes(_, dailyRewards) }
    case WarStartedEvent(_) =>
      leaderboardService.leaderboards.values
        .filter { _.isMonthly }
        .foreach { awardPrizes(_, monthlyRewards) }
    case _ =>
  }

  private def awardPrizes(leaderboard: Leaderboard, prizes: List[Chest]): Unit = {
    val rankings = leaderboard.rankings
    prizes.zipWithIndex.foreach {
      case (prize, index) if rankings.size > index =>
        val (winner, score) = rankings(index)
        itemService.giveTo(winner.id, prize)

        val messageTail =
          s"${index + 1}${appendix(index)} on the ${leaderboard.name} with $score!"
        if (index == 0) {
          val winnerMessage = Message.server(s"${winner.name} placed $messageTail")
          playerService.getOnlinePlayers.foreach { _.sendMessage(winnerMessage) }
        }
        val plural = if (prize.count > 1) "s" else ""
        val prizeMessage = Message.server(
          s"You received ${prize.count} ${prize.name}$plural for placing $messageTail"
        )
        playerService.sendMessage(winner.id, prizeMessage)
    }

    leaderboard.clear()
  }
}
