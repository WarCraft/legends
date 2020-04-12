package gg.warcraft.legends.api.combat.warrior.armsmaster

import gg.warcraft.combat.api.ability.MeleeAbility
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.monolith.api.combat.PotionEffect
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.world.{Sound, SoundCategory, WorldService}

object Hamstring {
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
    ): MeleeAbility = new Hamstring(caster, snapshot)
  }
}

class Hamstring(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(
    implicit entityService: EntityService,
    worldService: WorldService
) extends MeleeAbility {
  private final val soundCategory = SoundCategory.HOSTILE
  private final val hitSound = Sound.ENTITY_SILVERFISH_DEATH
  private final val missSound = Sound.ENTITY_FISHING_BOBBER_THROW

  override def onHit(entity: Entity): Unit = {
    entityService damageEntity (entity.id, snapshot.damage)
    val slow = snapshot.potionEffect(PotionEffect.SLOW)
    entityService addPotionEffect (entity.id, slow)
    // TODO warriorEffectFactory.createHamstringEffect(entity, slow.getDuration());
    worldService playSound (entity.location, hitSound, soundCategory, 1, 1)
  }

  override def onMiss(entity: Entity): Unit = {
    worldService playSound (entity.location, missSound, soundCategory, 1, 1)
  }
}
