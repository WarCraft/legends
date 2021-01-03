/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.game.handler

import gg.warcraft.legends.api.core.game.{SkirmishFinishedEvent, WarFinishedEvent}
import gg.warcraft.legends.api.item.crate.LootCrateService
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.player.PlayerService

class GameWinnerRewardHandler(implicit
    playerService: PlayerService,
    lootCrateService: LootCrateService
) extends Event.Handler {
  private val winnerCrate = lootCrateService.crates("WAR_CRATE").withCount(5)
  private val loserCrate = lootCrateService.crates("WAR_CRATE").withCount(2)

  override def handle(event: Event): Unit = event match {
    case WarFinishedEvent(war, winner) =>
    // TODO impl daily and monthly rewards when item cache is impl

    case SkirmishFinishedEvent(_, winner) =>
      playerService.getOnlinePlayers.foreach { player =>
        if (player.team.contains(winner)) {
          playerService.giveItem(player.id, winnerCrate)
        } else playerService.giveItem(player.id, loserCrate)
      }
    case _ =>
  }
}
