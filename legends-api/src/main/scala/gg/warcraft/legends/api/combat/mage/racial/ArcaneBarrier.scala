package gg.warcraft.legends.api.combat.mage.racial

import gg.warcraft.combat.api.ability.MeleeAbility
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.monolith.api.entity.{Entity, EntityService}

object ArcaneBarrier {
  class Factory(
      implicit entityService: EntityService
  ) extends MeleeAbility.Factory {
    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        target: Option[Entity]
    ): MeleeAbility = new ArcaneBarrier(caster, snapshot)
  }
}

class ArcaneBarrier(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(
    implicit entityService: EntityService
) extends MeleeAbility {
  entityService addPotionEffect (caster.id, snapshot.absorption)
  // TODO play sound
}
