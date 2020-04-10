package gg.warcraft.legends.api.combat.warrior

import gg.warcraft.combat.api.ability.MeleeAbility
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.ability.MeleeAbility.Flag
import gg.warcraft.monolith.api.entity.Entity
import gg.warcraft.monolith.api.entity.status.StatusService
import gg.warcraft.monolith.api.world.{Sound, SoundCategory, WorldService}

object Grudge {
  class Factory(
      implicit statusService: StatusService,
      worldService: WorldService
  ) extends MeleeAbility.Factory {
    override val flags: Set[Flag] = Set(MeleeAbility.TARGET_REQUIRED)

    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        target: Option[Entity]
    ): MeleeAbility = new Grudge(caster, snapshot)
  }
}

class Grudge(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(
    implicit statusService: StatusService,
    worldService: WorldService
) extends MeleeAbility {
  private final val soundCategory = SoundCategory.HOSTILE
  private final val hitSound = Sound.ENTITY_WITHER_SPAWN
  private final val missSound = Sound.ENTITY_FISHING_BOBBER_THROW

  override def onHit(entity: Entity): Unit = {
    // TODO
//    StatusEffect grudge =
//        warriorStatusEffectFactory.createGrudgeStatusEffect(targetId, getCasterId(), duration, heal);
//    statusCommandService.addStatusEffect(grudge, entity.getId());

    // TODO    warriorEffectFactory.createGrudgeEffect(entity, duration);
    worldService playSound (entity.location, hitSound, soundCategory, 1, 1)
  }

  override def onMiss(entity: Entity): Unit =
    worldService playSound (entity.location, missSound, soundCategory, 1, 1)
}
