/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.challenge.handler

import gg.warcraft.legends.api.LegendsConfig
import gg.warcraft.legends.api.player.challenge.ChallengeService
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.player.PlayerStatisticsChangedEvent

class ChallengeCompletionHandler(config: LegendsConfig)(implicit
    service: ChallengeService
) extends Event.Handler {
  private val challengesByStatistic = config.challenges.groupBy { _.statisticId }

  override def handle(event: Event): Unit = event match {
    case PlayerStatisticsChangedEvent(player, statistic, _, newCurrent) =>
      challengesByStatistic.get(statistic) match {
        case Some(challenges) => challenges
            .filter { it => !it.isDaily || service.activeDailies.contains(it) }
            .filter { it => !it.isMonthly || service.activeMonthlies.contains(it) }
            .foreach { challenge =>
              if (
                newCurrent >= challenge.requirement &&
                !service.completedChallenges(player.id).contains(challenge.id)
              ) service.completeChallenge(player, challenge)
            }
        case None =>
      }
    case _ =>
  }
}
