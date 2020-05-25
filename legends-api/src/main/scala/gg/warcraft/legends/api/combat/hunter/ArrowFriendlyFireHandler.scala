package gg.warcraft.legends.api.combat.hunter

import java.util.logging.Logger

import gg.warcraft.monolith.api.combat.ProjectilePreHitEvent
import gg.warcraft.monolith.api.core.event.{Event, PreEvent}
import gg.warcraft.monolith.api.entity.EntityService

class ArrowFriendlyFireHandler(implicit
    logger: Logger,
    entityService: EntityService
) extends Event.Handler {
  override def reduce[T <: PreEvent](event: T): T = event match {
    case it: ProjectilePreHitEvent =>
      val shooter = it.shooterId.flatMap { entityService.getEntityOption }
      val target = it.entityId.flatMap { entityService.getEntityOption }
      if (shooter.isEmpty || target.isEmpty) {
        logger.warning("Shooter or target is missing in ArrowFriendlyFireHandler")
        event
      } else if (shooter.get.team == target.get.team) {
        it.copy(cancelled = true).asInstanceOf[T]
      } else event
    case _ => event
  }
}
