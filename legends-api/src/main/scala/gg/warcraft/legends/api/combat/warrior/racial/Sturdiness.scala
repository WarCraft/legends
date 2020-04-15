package gg.warcraft.legends.api.combat.warrior.racial

import gg.warcraft.monolith.api.combat.{CombatSource, CombatValue}
import gg.warcraft.monolith.api.core.Duration
import gg.warcraft.monolith.api.core.event.{Event, PreEvent}
import gg.warcraft.monolith.api.entity.{
  EntityHealthChangedEvent, EntityPreDamageEvent
}
import gg.warcraft.monolith.api.entity.status.Status

object Sturdiness {
  class StatusEffect(
      override val source: CombatSource,
      override val duration: Option[Duration]
  ) extends Status.Effect {
    override val name: String = "Sturdiness"
    override val group: String = "legends.racial"

    private var percentHealth = 1f

    override def handle(event: Event): Unit = event match {
      case EntityHealthChangedEvent(_, _, _, _, _, newPercentHealth) =>
        percentHealth = newPercentHealth
      case _ =>
    }

    override def reduce[T <: PreEvent](event: T): T = event match {
      case it: EntityPreDamageEvent =>
        val modifierValue = -0.25f * (1 - percentHealth) // TODO add modifier to config
        val modifier =
          CombatValue.Modifier(source, CombatValue.Modifier.PERCENT, modifierValue)
        it.copy(damage = it.damage.copy(modifiers = modifier :: it.damage.modifiers))
          .asInstanceOf[T]
      case _ => event
    }
  }
}
