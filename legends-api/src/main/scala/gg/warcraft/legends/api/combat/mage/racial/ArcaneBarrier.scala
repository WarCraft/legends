package gg.warcraft.legends.api.combat.mage.racial

import gg.warcraft.combat.api.ability.{AbilityService, MeleeAbility}
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.monolith.api.combat.CombatSource
import gg.warcraft.monolith.api.core.Duration
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.entity.{
  Entity, EntityHealthChangedEvent, EntityService
}
import gg.warcraft.monolith.api.entity.status.Status
import gg.warcraft.monolith.api.world.{Sound, SoundCategory, WorldService}

object ArcaneBarrier {
  class Factory(implicit
      entityService: EntityService,
      worldService: WorldService
  ) extends MeleeAbility.Factory {
    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        target: Option[Entity]
    ): MeleeAbility = new ArcaneBarrier(caster, snapshot)
  }

  class StatusEffect(
      override val source: CombatSource
  )(implicit
      abilityService: AbilityService
  ) extends Status.Effect {
    require(source.entityId.isDefined)

    override val name: String = "Arcane Barrier"
    override val group: String = "legends.racial"
    override val duration: Option[Duration] = None

    override def handle(event: Event): Unit = event match {
      case EntityHealthChangedEvent(_, _, _, _, newPercentHealth) =>
        if (newPercentHealth <= 0.5f)
          abilityService.castAbility(name, source.entityId.get, None)
      case _ =>
    }
  }
}

class ArcaneBarrier(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(implicit
    entityService: EntityService,
    worldService: WorldService
) extends MeleeAbility {
  private final val soundCategory = SoundCategory.HOSTILE
  private final val castSound = Sound.BLOCK_ENCHANTMENT_TABLE_USE

  entityService.addPotionEffect(caster.id, snapshot.absorption)
  worldService.playSound(caster.location, castSound, soundCategory)
}
