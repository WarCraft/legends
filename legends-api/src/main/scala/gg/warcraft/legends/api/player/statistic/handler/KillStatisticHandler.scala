/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.statistic.handler

import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.combat.api.util.entity._
import gg.warcraft.legends.api.player.statistic.LegendsStatistic._
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.entity.EntityFatalDamageEvent
import gg.warcraft.monolith.api.player.Player
import gg.warcraft.monolith.api.player.statistic.StatisticService

class KillStatisticHandler(implicit
    statisticService: StatisticService,
    combatDataService: CombatDataService
) extends Event.Handler {
  private final val killStatistics: List[String] =
    DailyKills :: MonthlyKills :: AlltimeKills :: Nil

  private final val mobStatistics: List[String] =
    DailyMobs :: MonthlyMobs :: AlltimeMobs :: Nil

  override def handle(event: Event): Unit = event match {
    case EntityFatalDamageEvent(player: Player, _) =>
      statisticService.increaseStatistics(
        killStatistics,
        1,
        player.combatData.attackedBy.toList: _*
      )
    case EntityFatalDamageEvent(entity, _) =>
      entity.combatDataOption match {
        case Some(combatData) =>
          statisticService.increaseStatistics(
            mobStatistics,
            1,
            combatData.attackedBy.toList: _*
          )
        case None =>
      }
    case _ =>
  }
}
