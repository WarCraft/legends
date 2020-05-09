package gg.warcraft.legends.api.player.statistic.handler

import gg.warcraft.legends.api.player.statistic.LegendsStatistic
import gg.warcraft.legends.api.player.statistic.LegendsStatistic._
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.entity.EntityDamageEvent
import gg.warcraft.monolith.api.player.statistic.StatisticService
import gg.warcraft.monolith.api.player.Player

class DamageStatisticHandler(implicit
    statisticService: StatisticService
) extends Event.Handler {
  private final val damageStatistics: List[LegendsStatistic] =
    List(DAILY_DAMAGE, MONTHLY_DAMAGE, ALLTIME_DAMAGE)

  override def handle(event: Event): Unit = event match {
    case EntityDamageEvent(entity: Player, damage) =>
      statisticService.increaseStatistic(entity.id, damage.modified, damageStatistics: _ *)
  }
}
