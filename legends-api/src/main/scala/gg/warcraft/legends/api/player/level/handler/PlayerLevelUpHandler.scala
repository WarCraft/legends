/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.level.handler

import gg.warcraft.legends.api.player.PlayerFavorGainedEvent
import gg.warcraft.legends.api.player.level.{LevelService, PlayerLevelUpEvent}
import gg.warcraft.legends.api.util.player._
import gg.warcraft.monolith.api.core.event.{Event, EventService}
import gg.warcraft.monolith.api.player.PlayerService

class PlayerLevelUpHandler(implicit
    eventService: EventService,
    playerService: PlayerService,
    levelService: LevelService
) extends Event.Handler {
  override def handle(event: Event): Unit = event match {
    case PlayerFavorGainedEvent(player, amount, _, newLifeTime) =>
      val previousLifeTime = newLifeTime - amount
      val previousLevel = levelService.levelForFavor(previousLifeTime)
      val newLevel = player.level
      if (previousLevel != newLevel) {
        playerService.setLevel(player.id, newLevel.value)
        val event = PlayerLevelUpEvent(player, newLevel)
        eventService.publish(event)
      }

    /* TODO update xp display
        float adjustedNewLifetimeFavor = newLifetimeFavor - newRank.getFavor();
        float adjustedRequiredFavor = newRank.getRequiredFavor() - newRank.getFavor();
        playerServerAdapter.setExperienceProgress(playerId, adjustedNewLifetimeFavor / adjustedRequiredFavor);
     */
    case _ =>
  }
}
