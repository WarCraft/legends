/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.statistic.handler

import gg.warcraft.legends.api.player.statistic.LegendsStatistic._
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.entity.EntityDamageEvent
import gg.warcraft.monolith.api.player.statistic.StatisticService
import gg.warcraft.monolith.api.player.{Player, PlayerService}

class DamageStatisticHandler(implicit
    service: StatisticService,
    playerService: PlayerService
) extends Event.Handler {
  private final val damageStatistics: List[String] =
    DailyDamage :: MonthlyDamage :: AlltimeDamage :: Nil

  override def handle(event: Event): Unit = event match {
    case EntityDamageEvent(entity: Player, damage) =>
      damage.source.entityId
        .flatMap(playerService.getPlayerOption)
        .foreach { attacker =>
          service.increaseStatistics(
            damageStatistics,
            damage.modified.toLong,
            attacker.id
          )
        }
    case _ =>
  }
}
