package gg.warcraft.legends.api.combat.warrior.berserker

import gg.warcraft.combat.api.ability.MeleeAbility
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.monolith.api.combat.PotionEffect
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.world.{Sound, SoundCategory, WorldService}

object Maul {
  class Factory(
      implicit entityService: EntityService,
      worldService: WorldService
  ) extends MeleeAbility.Factory {
    override val flags: Set[MeleeAbility.Flag] =
      Set(MeleeAbility.TARGET_REQUIRED)

    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        target: Option[Entity]
    ): MeleeAbility = new Maul(caster, snapshot)
  }
}

class Maul(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(
    implicit entityService: EntityService,
    worldService: WorldService
) extends MeleeAbility {
  private final val soundCategory = SoundCategory.HOSTILE
  private final val hitSound = Sound.ENTITY_ZOMBIE_STEP
  private final val missSound = Sound.ENTITY_FISHING_BOBBER_THROW

  override def onHit(entity: Entity): Unit = {
    val absorption = snapshot potionEffect PotionEffect.ABSORPTION
    entityService addPotionEffect (entity.id, absorption)
    entityService damageEntity (entity.id, snapshot.damage)

    // TODO create effect
    worldService playSound (entity.location, hitSound, soundCategory, 1, 1)
  }

  override def onMiss(entity: Entity): Unit =
    worldService playSound (entity.location, missSound, soundCategory, 1, 1)
}
