package gg.warcraft.legends.api.combat.mage.wizard

import gg.warcraft.combat.api.ability.RangedAbility
import gg.warcraft.combat.api.ability.RangedAbility.Flag
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.projectile.ProjectileService
import gg.warcraft.monolith.api.block.Block
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.world.{Sound, SoundCategory, WorldService}

class Juke(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot,
    target: Entity
)(implicit
    entityService: EntityService,
    projectileService: ProjectileService,
    worldService: WorldService
) extends RangedAbility {
  private final val soundCategory = SoundCategory.HOSTILE
  private final val castSound = Sound.ENTITY_ENDERMAN_TELEPORT
  private val casterLocation = caster.location

  override def onHit(entity: Entity): Boolean = {
    val targetLocation = entity.location
    entityService.teleportEntity(caster.id, targetLocation)
    entityService.teleportEntity(entity.id, casterLocation)
    worldService.playSound(casterLocation, castSound, soundCategory)
    worldService.playSound(targetLocation, castSound, soundCategory)

    entityService.damageEntity(entity.id, snapshot.damage)
    entityService.addPotionEffect(entity.id, snapshot.blindness)
    entityService.addPotionEffect(entity.id, snapshot.nausea)
    true
  }

  override def onMiss(entity: Entity): Boolean = false
}

object Juke {
  class Factory(implicit
      entityService: EntityService,
      projectileService: ProjectileService,
      worldService: WorldService
  ) extends RangedAbility.Factory {
    override val flags: Set[Flag] =
      Set(RangedAbility.ENTITY_TARGET_REQUIRED)

    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        blockTarget: Option[Block],
        entityTarget: Option[Entity]
    ): RangedAbility = new Juke(caster, snapshot, entityTarget.get)
  }
}
