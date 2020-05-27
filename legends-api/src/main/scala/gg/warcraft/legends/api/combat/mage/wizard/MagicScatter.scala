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
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.effect.{Effect, EffectRenderer, EffectVectors, Particle, ParticleAdapter}
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.world.{Sound, SoundCategory, WorldService}

class MagicScatter(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(implicit
    taskService: TaskService,
    entityService: EntityService,
    projectileService: ProjectileService,
    worldService: WorldService,
    particleAdapter: ParticleAdapter
) extends RangedAbility {
  private final val soundCategory = SoundCategory.HOSTILE
  private final val castSound = Sound.ENTITY_ENDER_DRAGON_HURT
  private final val hitSound = Sound.ENTITY_ENDER_DRAGON_HURT
  private val hitbox = Hitbox.sphere(snapshot.radius.modified, _.team != caster.team)
  //
  private final val location = caster.eyeLocation
  private final val maxDistance = 5f
  private final val direction = location.rotation
  private final val velocity = direction * snapshot.velocity.modified
  private final val maxScatter = 15


  //Multiple scattered projectiles
  private val projectileCount = 5
  for( p <- 0 to projectileCount) {
    val tempVelocity = velocity
    tempVelocity.rotateX(Math.toRadians(Math.random() * maxScatter).toFloat)
    tempVelocity.rotateY(Math.toRadians(Math.random() * maxScatter).toFloat)
    tempVelocity.rotateZ(Math.toRadians(Math.random() * maxScatter).toFloat)
    val movement = Movement.linear(caster.eyeLocation, tempVelocity, maxDistance)
    val effect = new ArcaneMissile.ParticleEffect(movement)
    projectileService.createProjectile(this, movement, hitbox, effect.cancel)
  }

  worldService.playSound(caster.location, castSound, soundCategory)

  override def onHit(entity: Entity): Boolean = {
    entityService.damageEntity(entity.id, snapshot.damage)
    worldService.playSound(entity.location, hitSound, soundCategory)
    true
  }

  override def onMiss(entity: Entity): Boolean = false
}

object MagicScatter {
  class Factory(implicit
                taskService: TaskService,
                entityService: EntityService,
                projectileService: ProjectileService,
                worldService: WorldService,
                particleAdapter: ParticleAdapter
               ) extends RangedAbility.Factory {
    override val flags: Set[Flag] =
      Set(RangedAbility.ENTITY_TARGET_REQUIRED)

    override def create(
                         caster: Entity,
                         snapshot: AbilitySnapshot,
                         blockTarget: Option[Block],
                         entityTarget: Option[Entity]
                       ): RangedAbility = new MagicScatter(caster, snapshot)
  }

  class ParticleEffect(movement: Movement)(implicit
                                           taskService: TaskService,
                                           particleAdapter: ParticleAdapter
  ) extends Effect(movement.get) {
    private final val radius = 0.1f
    private final val innerParticleCount = 1
    private final val innerParticleSpeed = 1
    private final val outerParticleCount = 6

    private val innerParticle =
      Particle.EnchantmentTable.speed(innerParticleSpeed, innerParticleCount)
    private val innerVectors = EffectVectors.origin()
    private val innerRenderer = EffectRenderer(innerParticle, innerVectors)
    addRenderer(innerRenderer)

    private val outerParticle = Particle.DragonBreath
    private val outerVectors = EffectVectors.sphere(radius, outerParticleCount)
    private val outerRenderer = EffectRenderer(outerParticle, outerVectors)
    addRenderer(outerRenderer)
  }
}


