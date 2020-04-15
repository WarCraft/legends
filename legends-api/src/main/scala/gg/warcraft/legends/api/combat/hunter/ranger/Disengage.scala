package gg.warcraft.legends.api.combat.hunter.ranger

import gg.warcraft.combat.api.ability.MeleeAbility
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.world.{Sound, SoundCategory, WorldService}

object Disengage {
  class Factory(
      implicit entityService: EntityService,
      worldService: WorldService
  ) extends MeleeAbility.Factory {
    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        target: Option[Entity]
    ): MeleeAbility = new Disengage(caster, snapshot)
  }
}

class Disengage(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(
    implicit entityService: EntityService,
    worldService: WorldService
) extends MeleeAbility {
  private final val soundCategory = SoundCategory.HOSTILE
  private final val castSound = Sound.ENTITY_ENDER_DRAGON_FLAP // BAT_TAKEOFF?

  // TODO add an extra melee ability flag which prevents hit/miss checks for selfcast abilities?

  private val direction = caster.eyeLocation.rotation.withY(0).multiply(-1)
  entityService knockBackEntity (caster.id, direction, snapshot.distance.modified)

  worldService playSound (caster.location, castSound, soundCategory)
}
