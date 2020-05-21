package gg.warcraft.legends.api.combat.hunter

import gg.warcraft.monolith.api.combat.{Projectile, ProjectilePrePickupEvent}
import gg.warcraft.monolith.api.core.event.{Event, PreEvent}

class ArrowPickUpHandler extends Event.Handler {
  override def reduce[T <: PreEvent](event: T): T = event match {
    case it: ProjectilePrePickupEvent =>
      if (it.projectileType == Projectile.ARROW)
        it.copy(cancelled = true).asInstanceOf[T]
      else event
    case _ => event
  }
}
