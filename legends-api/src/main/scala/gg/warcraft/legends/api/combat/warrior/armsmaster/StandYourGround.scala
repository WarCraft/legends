package gg.warcraft.legends.api.combat.warrior.armsmaster

import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.ability.MeleeAbility
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.entity.attribute.AttributeService
import gg.warcraft.monolith.api.entity.status.StatusService
import gg.warcraft.monolith.api.world.{Sound, SoundCategory, WorldService}

object StandYourGround {
  class Factory(
      implicit entityService: EntityService,
      attributeService: AttributeService,
      statusService: StatusService,
      worldService: WorldService
  ) extends MeleeAbility.Factory {
    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        target: Option[Entity]
    ): MeleeAbility = new StandYourGround(caster, snapshot)
  }
}

class StandYourGround(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(
    implicit entityService: EntityService,
    attributeService: AttributeService,
    statusService: StatusService,
    worldService: WorldService
) extends MeleeAbility {
  private final val soundCategory = SoundCategory.HOSTILE
  private final val castSound = Sound.ENTITY_IRON_GOLEM_DEATH

  {
    entityService addPotionEffect (caster.id, snapshot.resistance)
    entityService addPotionEffect (caster.id, snapshot.fireResistance)
    entityService addPotionEffect (caster.id, snapshot.slow)

    // TODO
//    AttributeModifier knockbackModifier =
//        attributeCommandService.addAttributeModifier(caster.getId(), GenericAttribute.KNOCKBACK_RESISTANCE, 1);
//    taskService.runLater(() -> attributeCommandService.removeAttributeModifier(caster.getId(), knockbackModifier),
//      slow.getDuration());
//
//    warriorEffectFactory.createStandYourGroundEffect(caster, slow.getDuration());
//
//    StatusEffect statusEffect =
//        warriorStatusEffectFactory.createStandYourGroundStatusEffect(caster.getId(), slow.getDuration());
//    statusCommandService.addStatusEffect(statusEffect, caster.getId());
    worldService playSound (caster.location, castSound, soundCategory, 1, 1)
  }
}
