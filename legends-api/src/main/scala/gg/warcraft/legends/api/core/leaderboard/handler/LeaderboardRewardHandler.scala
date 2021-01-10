/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.leaderboard.handler

import gg.warcraft.legends.api.LegendsConfig
import gg.warcraft.legends.api.core.game.WarStartedEvent
import gg.warcraft.legends.api.core.leaderboard.{Leaderboard, LeaderboardService}
import gg.warcraft.legends.api.item.crate.{LootCrate, LootCrateService}
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.core.{DailyTickEvent, Message}
import gg.warcraft.monolith.api.item.ItemService
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
    .map(lootCrateService.parseRewards)
  private val monthlyRewards = config.leaderboards.monthlyPrizes
    .map(lootCrateService.parseRewards)

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

  private def awardPrizes(
      leaderboard: Leaderboard,
      prizes: List[List[LootCrate]]
  ): Unit = {
    val rankings = leaderboard.rankings
    prizes.zipWithIndex.foreach {
      case (prizes, index) if rankings.size > index =>
        val (winner, score) = rankings(index)
        prizes.foreach { itemService.giveTo(winner.id, _) }

        val prizeString = prizes.map { it =>
          val plural = if (it.count > 1) "s" else ""
          s"${it.count} ${it.name}$plural"
        }.mkString(", ")

        val messageTail =
          s"${index + 1}${appendix(index)} on the ${leaderboard.name} with $score!"
        if (index == 0) {
          val winnerMessage = Message.server(s"${winner.name} placed $messageTail")
          playerService.getOnlinePlayers.foreach { _.sendMessage(winnerMessage) }
        }
        val prizeMessage = Message.server(
          s"You received $prizeString for placing $messageTail"
        )
        playerService.sendMessage(winner.id, prizeMessage)

      case _ =>
    }

    leaderboard.clear()
  }
}
