/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat.warrior.racial

import gg.warcraft.monolith.api.combat.{CombatSource, CombatValue}
import gg.warcraft.monolith.api.core.Duration
import gg.warcraft.monolith.api.core.event.{Event, PreEvent}
import gg.warcraft.monolith.api.entity.{
  EntityHealthChangedEvent, EntityPreDamageEvent
}
import gg.warcraft.monolith.api.entity.status.Status
import gg.warcraft.monolith.api.util.typing._

object Sturdiness {
  class StatusEffect(
      override val source: CombatSource
  ) extends Status.Effect {
    override val name: String = "Sturdiness"
    override val group: String = "legends.racial"
    override val duration: Option[Duration] = None

    private var percentHealth = 1f

    override def handle(event: Event): Unit = event match {
      case EntityHealthChangedEvent(_, _, _, _, newPercentHealth) =>
        percentHealth = newPercentHealth
      case _ =>
    }

    override def reduce[T <: PreEvent](event: T): T = event match {
      case it @ EntityPreDamageEvent(_, damage, _, _) =>
        // TODO add modifier to config
        val modifierValue = -0.25f * (1 - percentHealth)
        val modifier = CombatValue.Modifier.percent(source, modifierValue)
        it.copy(damage = damage.copy(modifiers = modifier :: damage.modifiers)).as[T]
      case _ => event
    }
  }
}
