package gg.warcraft.legends.api.combat.warrior.racial

import gg.warcraft.combat.api.ability.{AbilityHitEvent, AbilityPreHitEvent}
import gg.warcraft.monolith.api.combat.CombatSource
import gg.warcraft.monolith.api.core.Duration
import gg.warcraft.monolith.api.core.event.{Event, PreEvent}
import gg.warcraft.monolith.api.entity.status.Status

object BladeDance {
  class StatusEffect(
      override val source: CombatSource,
      override val duration: Option[Duration]
  ) extends Status.Effect {
    require(source.entityId.isDefined)

    private final val maxCount = 3

    override val name: String = "Agility"
    override val group: String = "legends.racial"

    private var counter = 0
    private var evade = false

    override def handle(event: Event): Unit = event match {
      case AbilityHitEvent("Melee", casterId, _) =>
        if (!evade && casterId == source.entityId.get) {
          counter += 1
          if (counter >= maxCount) {
            counter = 0
            evade = true
          }
        }
      case _ =>
    }

    override def reduce[T <: PreEvent](event: T): T = event match {
      case it @ AbilityPreHitEvent("Melee", _, targetId, _, _) =>
        if (evade && targetId == source.entityId.get) {
          evade = false
          it.copy(cancelled = true).asInstanceOf[T]
        } else event
      case _ => event
    }
  }
}
