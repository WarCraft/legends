package gg.warcraft.legends.api.player.statistic.handler

import gg.warcraft.legends.api.player.statistic.LegendsStatistic
import gg.warcraft.legends.api.player.statistic.LegendsStatistic._
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.player.statistic.StatisticService

class DamageStatisticHandler(implicit
    statisticService: StatisticService
) extends Event.Handler {
  private final val statistics: List[LegendsStatistic] =
    DAILY_DAMAGE :: MONTHLY_DAMAGE :: ALLTIME_DAMAGE :: Nil

  override def handle(event: Event): Unit = event match {
//    case EntityDamageEvent(entity: Player, damage) => TODO
//      statisticService.increaseStatistic(entity.id, damage.modified, statistics: _*)
    case _ =>
  }
}
