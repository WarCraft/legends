package gg.warcraft.legends.api.combat.warrior.racial

import gg.warcraft.combat.api.ability.AbilityPreCastEvent
import gg.warcraft.monolith.api.combat.{CombatSource, CombatValue, PotionEffect}
import gg.warcraft.monolith.api.core.Duration
import gg.warcraft.monolith.api.core.Duration._
import gg.warcraft.monolith.api.core.event.{Event, PreEvent}
import gg.warcraft.monolith.api.entity.{EntityHealthChangedEvent, EntityService}
import gg.warcraft.monolith.api.entity.status.Status

object Bloodlust {
  class StatusEffect(
      override val source: CombatSource
  )(implicit
      entityService: EntityService
  ) extends Status.Effect {
    require(source.entityId.isDefined)

    private final val potionEffect =
      PotionEffect(PotionEffect.INCREASE_DAMAGE, 1, Integer.MAX_VALUE.ticks)
    private final val damageModifier =
      CombatValue.Modifier(source, CombatValue.Modifier.PERCENT, 0.15f)

    override val name: String = "Bloodlust"
    override val group: String = "legends.racial"
    override val duration: Option[Duration] = None

    private var active = false

    override def handle(event: Event): Unit = event match {
      case EntityHealthChangedEvent(_, _, _, _, newPercentHealth) =>
        if (newPercentHealth <= 0.5f) { // TODO add threshold to config
          entityService.addPotionEffect(source.entityId.get, potionEffect)
          active = true
        } else {
          entityService.removePotionEffect(source.entityId.get, potionEffect.typed)
          active = false
        }
      case _ =>
    }

    override def reduce[T <: PreEvent](event: T): T = event match {
      case it: AbilityPreCastEvent =>
        if (active) {
          val modifiers = damageModifier :: it.damageModifiers
          it.copy(damageModifiers = modifiers).asInstanceOf[T]
        } else event
      case _ => event
    }
  }
}
