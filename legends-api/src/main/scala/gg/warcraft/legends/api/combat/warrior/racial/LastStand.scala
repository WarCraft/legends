package gg.warcraft.legends.api.combat.warrior.racial

import gg.warcraft.combat.api.ability.MeleeAbility
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

  class Status(
      override val source: CombatSource,
      override val duration: Option[Duration]
  ) extends Status.Effect {
    override val name: String = "Last Stand"
    override val group: String = "racial"

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

  {
    val status = new LastStand.Status(caster, snapshot.duration)
    statusService addEffect (caster.id, status)
    worldService playSound (caster.location, castSound, soundCategory, 1, 1)
  }
}
