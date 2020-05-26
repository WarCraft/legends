/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat.mage.wizard

import gg.warcraft.combat.api.{Hitbox, Movement}
import gg.warcraft.combat.api.ability.RangedAbility
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.ability.RangedAbility.Flag
import gg.warcraft.combat.api.projectile.ProjectileService
import gg.warcraft.monolith.api.block.Block
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.math.Vector3f
import gg.warcraft.monolith.api.world.{Sound, SoundCategory, WorldService}

class Teleport(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot,
    blockTarget: Block
)(implicit
    entityService: EntityService,
    projectileService: ProjectileService,
    worldService: WorldService
) extends RangedAbility {
  private final val soundCategory = SoundCategory.HOSTILE
  private final val castSound = Sound.ENTITY_ENDERMAN_TELEPORT
  private final val hitSound = Sound.ENTITY_ENDERMAN_TELEPORT

  private val origin = caster.location
  private val yaw = caster.eyeLocation.rotation.toPitchYaw._2

  private val targetLocation = blockTarget.location
  private val relativeLocation = targetLocation + (0, 1, 0)
  private val relative = worldService.getBlock(relativeLocation)
  private val target =
    if (relative.solid) {
      val targetToCaster = (targetLocation.toLocation - origin).normalized
      val adjustedLocation =
        (targetLocation.toLocation + (.5f, .5f, .5f) + targetToCaster).toBlockLocation
      adjustedLocation.toLocation + (.5f, 1, .5f)
    } else relativeLocation.toLocation + (.5f, 0, .5f)

  entityService.teleportEntity(caster.id, target, Vector3f(0, yaw))
  worldService.playSound(origin, castSound, soundCategory)

  private val movement = Movement.oneOff(target)
  private val hitbox = Hitbox.sphere(snapshot.radius.modified, _.team != caster.team)
  projectileService.createProjectile(this, movement, hitbox)

  override def onHit(entity: Entity): Boolean = {
    entityService.damageEntity(entity.id, snapshot.damage)
    entityService.addPotionEffect(entity.id, snapshot.slow)
    worldService.playSound(entity.location, hitSound, soundCategory)
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
      Set(RangedAbility.BLOCK_TARGET_REQUIRED)

    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        blockTarget: Option[Block],
        entityTarget: Option[Entity]
    ): RangedAbility = new Teleport(caster, snapshot, blockTarget.get)
  }
}
