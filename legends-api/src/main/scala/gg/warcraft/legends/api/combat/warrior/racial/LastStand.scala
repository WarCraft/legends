package gg.warcraft.legends.api.combat.warrior.racial

import gg.warcraft.combat.api.ability.{AbilityService, MeleeAbility}
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.monolith.api.combat.CombatSource
import gg.warcraft.monolith.api.core.Duration
import gg.warcraft.monolith.api.core.event.PreEvent
import gg.warcraft.monolith.api.entity.{Entity, EntityPreFatalDamageEvent}
import gg.warcraft.monolith.api.entity.status.{Status, StatusService}
import gg.warcraft.monolith.api.world.{Sound, SoundCategory, WorldService}

object LastStand {
  class Factory(
      implicit statusService: StatusService,
      worldService: WorldService
  ) extends MeleeAbility.Factory {
    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        target: Option[Entity]
    ): MeleeAbility = new LastStand(caster, snapshot)
  }

  class PassiveStatusEffect(
      override val source: CombatSource,
      override val duration: Option[Duration]
  )(
      abilityService: AbilityService
  ) extends Status.Effect {
    require(source.entityId.isDefined)

    override val name: String = "Last Stand"
    override val group: String = "legends.racial"

    override def reduce[T <: PreEvent](event: T): T = event match {
      case it: EntityPreFatalDamageEvent =>
        val ability = abilityService castAbility (name, source.entityId.get, None)
        if (ability.isDefined) it.copy(cancelled = true).asInstanceOf[T]
        else event
      case _ => event
    }
  }

  class ActiveStatusEffect(
      override val source: CombatSource,
      override val duration: Option[Duration]
  ) extends Status.Effect {
    override val name: String = "Last Stand"
    override val group: String = "legends.racial"

    override def reduce[T <: PreEvent](event: T): T = event match {
      case it: EntityPreFatalDamageEvent => it.copy(cancelled = true).asInstanceOf[T]
      case _                             => event
    }
  }
}

class LastStand(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(
    implicit statusService: StatusService,
    worldService: WorldService
) extends MeleeAbility {
  private final val soundCategory = SoundCategory.HOSTILE
  private final val castSound = Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED

  private val status = new LastStand.ActiveStatusEffect(caster, snapshot.duration)
  statusService addEffect (caster.id, status)
  worldService playSound (caster.location, castSound, soundCategory)
}
