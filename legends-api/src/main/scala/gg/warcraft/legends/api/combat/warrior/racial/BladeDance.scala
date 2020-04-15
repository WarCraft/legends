package gg.warcraft.legends.api.combat.warrior.racial

import gg.warcraft.combat.api.ability.AbilityPreHitEvent
import gg.warcraft.monolith.api.combat.CombatSource
import gg.warcraft.monolith.api.core.Duration
import gg.warcraft.monolith.api.core.event.PreEvent
import gg.warcraft.monolith.api.entity.status.Status

object BladeDance {
  class StatusEffect(
      override val source: CombatSource,
      override val duration: Option[Duration]
  ) extends Status.Effect {
    require(source.entityId.isDefined)

    private final val maxCount = 5

    override val name: String = "Agility"
    override val group: String = "legends.racial"

    private var evadeCount = 0

    override def reduce[T <: PreEvent](event: T): T = event match {
      case it: AbilityPreHitEvent =>
        if (it.targetId == source.entityId.get) {
          evadeCount += 1
          if (evadeCount >= maxCount) {
            evadeCount = 0
            it.copy(cancelled = true).asInstanceOf[T]
          } else event
        } else event
      case _ => event
    }
  }
}
