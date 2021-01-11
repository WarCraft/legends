/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat.mage.wizard

import gg.warcraft.combat.api.ability.RangedAbility
import gg.warcraft.combat.api.ability.RangedAbility.Flag
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.projectile.ProjectileService
import gg.warcraft.combat.api.{Hitbox, Movement}
import gg.warcraft.monolith.api.block.Block
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.math.Vector3f
import gg.warcraft.monolith.api.world.{Sound, SoundCategory, WorldService}

class Teleport(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot,
    blockTarget: Option[Block]
)(implicit
    entityService: EntityService,
    projectileService: ProjectileService,
    worldService: WorldService
) extends RangedAbility {
  private final val soundCategory = SoundCategory.HOSTILE
  private final val castSound = Sound.ENTITY_ENDERMAN_TELEPORT

  private val origin = caster.location
  private val yaw = caster.eyeLocation.rotation.toPitchYaw._2

  private val target = blockTarget match {
    case Some(blockTarget) =>
      val targetLocation = blockTarget.location
      val relativeLocation = targetLocation + (0, 1, 0)
      val relativeBlock = worldService.getBlock(relativeLocation)
      if (relativeBlock.solid) {
        val adjustedTarget = targetLocation.toLocation + (.5f, .5f, .5f)
        val targetToCaster = (origin - adjustedTarget).normalized
        (adjustedTarget + targetToCaster).toBlockLocation.toLocation + (.5f, 0, .5f)
      } else relativeLocation.toLocation + (.5f, 0, .5f)
    case None =>
      caster.eyeLocation + caster.eyeLocation.rotation * snapshot.range.modified
  }

  worldService.playSound(origin, castSound, soundCategory)
  entityService.teleportEntity(caster.id, target, Vector3f(0, yaw))
  worldService.playSound(target, castSound, soundCategory)

  private val movement = Movement.oneOff(target)
  private val hitbox = Hitbox.sphere(snapshot.radius.modified, _.team != caster.team)
  projectileService.createProjectile(this, movement, hitbox)

  override def onHit(entity: Entity): Boolean = {
    entityService.damageEntity(entity.id, snapshot.damage)
    entityService.addPotionEffect(entity.id, snapshot.slow)
    false
  }

  override def onMiss(entity: Entity): Boolean = false
}

object Teleport {
  class Factory(implicit
      entityService: EntityService,
      projectileService: ProjectileService,
      worldService: WorldService
  ) extends RangedAbility.Factory {
    override val flags: Set[Flag] =
      Set(RangedAbility.BLOCK_TARGET_OPTIONAL)

    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        blockTarget: Option[Block],
        entityTarget: Option[Entity]
    ): RangedAbility = new Teleport(caster, snapshot, blockTarget)
  }
}
