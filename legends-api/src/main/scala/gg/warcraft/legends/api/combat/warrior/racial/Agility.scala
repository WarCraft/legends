package gg.warcraft.legends.api.combat.warrior.racial

import gg.warcraft.combat.api.ability.AbilityPreHitEvent
import gg.warcraft.monolith.api.combat.CombatSource
import gg.warcraft.monolith.api.core.Duration
import gg.warcraft.monolith.api.core.event.PreEvent
import gg.warcraft.monolith.api.entity.status.Status

object Agility {
  class StatusEffect(
      override val source: CombatSource,
      override val duration: Option[Duration]
  ) extends Status.Effect {
    require(source.entityId.isDefined)

    private final val maxCount = 7

    override val name: String = "Agility"
    override val group: String = "legends.racial"

    private var count = 0

    override def reduce[T <: PreEvent](event: T): T = event match {
      case it: AbilityPreHitEvent =>
        if (it.targetId == source.entityId.get) {
          count += 1
          if (count >= maxCount) {
            count = 0
            it.copy(cancelled = true).asInstanceOf[T]
          } else event
        } else event
      case _ => event
    }
  }
}
