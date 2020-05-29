/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat.warrior.berserker

import gg.warcraft.combat.api.{Hitbox, Movement}
import gg.warcraft.combat.api.ability.RangedAbility
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.projectile.{Projectile, ProjectileService}
import gg.warcraft.monolith.api.block.Block
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.world.{Sound, SoundCategory, WorldService}

class Shatter(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(implicit
    entityService: EntityService,
    projectileService: ProjectileService,
    worldService: WorldService
) extends RangedAbility {
  private final val soundCategory = SoundCategory.HOSTILE
  private final val hitSound = Sound.ENTITY_GENERIC_EXPLODE

  private val movement = Movement.entity(caster)
  private val hitbox = Hitbox.void()
  projectileService.createProjectile(this, movement, hitbox)

  private val direction = caster.eyeLocation.rotation
  entityService.leapEntity(caster.id, direction, snapshot.distance.modified)

  private var ticks = 0

  override def onHit(entity: Entity): Boolean = {
    entityService.damageEntity(entity.id, snapshot.damage)
    entityService.knockUpEntity(entity.id, snapshot.launch.modified)
    entityService.addPotionEffect(entity.id, snapshot.nausea)
    false
  }

  override def onMiss(entity: Entity): Boolean = false

  override def onTick(projectile: Projectile): Boolean = {
    ticks += 1
    if (caster.isGrounded && ticks >= 20) {
      val movement = Movement.oneOff(caster.location)
      val hitbox = Hitbox.sphere(snapshot.radius.modified, _.team != caster.team)
      projectileService.createProjectile(this, movement, hitbox)

      worldService.playSound(caster.location, hitSound, soundCategory)
      true
    } else false
  }
}

object Shatter {
  class Factory(implicit
      entityService: EntityService,
      projectileService: ProjectileService,
      worldService: WorldService
  ) extends RangedAbility.Factory {
    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        blockTarget: Option[Block],
        entityTarget: Option[Entity]
    ): RangedAbility = new Shatter(caster, snapshot)
  }
}
