/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.handler

import gg.warcraft.combat.api.ability.AbilityPreHitEvent
import gg.warcraft.monolith.api.core.event.{Event, PreEvent}
import gg.warcraft.monolith.api.entity.Entity
import gg.warcraft.monolith.api.world.World

class TutorialPvpHandler extends Event.Handler {
  override def reduce[T <: PreEvent](event: T): T = event match {
    case event @ AbilityPreHitEvent(_, _, target, false, false) =>
      val isPlayer = target.typed == Entity.Type.PLAYER
      val isTutorial = target.location.world.typ == World.TheEnd
      if (isPlayer && isTutorial) event.copy(cancelled = true).asInstanceOf[T]
      else event.asInstanceOf[T]
    case _ => event
  }
}
