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
            .foreach { challenge => if (newCurrent >= challenge.requirement) {

            } }
        case None =>
      }
    case _ =>
  }
}

/* TODO
public class ChallengeCompletionHandler {
            if (event.getNewCurrent() >= challenge.getRequiredStatisticValue()) {
                String playerDataKey = challenge.getPlayerData().getKey();
                if (!player.getDataBool(playerDataKey)) {
                    playerCommandService.setData(playerId, playerDataKey, "true");
                    playerCommandService.addCurrency(playerId, LegendsCurrency.FAVOR, challenge.getFavorReward());
                    playerCommandService.sendNotification(playerId, "You have completed the " +
                            challenge.getName() + " challenge!");
                    playerCommandService.sendNotification(playerId, "You gained " +
                            challenge.getFavorReward() + " favor.");
                }
            }
        });
    }
}

 */
