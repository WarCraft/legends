package gg.warcraft.legends.api.player.statistic.handler

import gg.warcraft.legends.api.player.statistic.LegendsStatistic._
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.entity.EntityDamageEvent
import gg.warcraft.monolith.api.player.statistic.StatisticService
import gg.warcraft.monolith.api.player.Player

class DamageStatisticHandler(implicit
    service: StatisticService
) extends Event.Handler {
  private final val statistics: List[String] =
    DailyDamage.id :: MonthlyDamage.id :: AlltimeDamage.id :: Nil

  override def handle(event: Event): Unit = event match {
    case EntityDamageEvent(entity: Player, damage) =>
      service.increaseStatistic(entity.id, damage.modified.toLong, statistics: _*)
    case _ =>
  }
}
