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
import gg.warcraft.monolith.api.effect.{
  Effect, EffectRenderer, EffectVectors, Particle, ParticleAdapter
}
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.world.{Location, Sound, SoundCategory, WorldService}

class MagicScatter(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot,
    target: Option[Entity]
)(implicit
    taskService: TaskService,
    entityService: EntityService,
    projectileService: ProjectileService,
    worldService: WorldService,
    particleAdapter: ParticleAdapter,
    abilityService: AbilityService
) extends RangedAbility {
  private final val soundCategory = SoundCategory.HOSTILE
  private final val castSound = Sound.ENTITY_ENDER_DRAGON_HURT
  private final val hitSound = Sound.ENTITY_ENDER_DRAGON_HURT
  private val hitbox = Hitbox.sphere(snapshot.radius.modified, _.team != caster.team)
  private final val location = caster.eyeLocation
  private final val range = snapshot.count.modified
  private final val count = snapshot.count.modified
  private final val homingRange = snapshot.distance.modified
  private final val direction = location.rotation
  private final val velocity = direction * snapshot.velocity.modified

  private final val maxInaccuracyAngle = 35f

  (0 to count.toInt).foreach { _ =>
    val modifiedVelocity =
      velocity
        .rotateX(((Math.random() - 0.5) * maxInaccuracyAngle).toRadians.toFloat)
        .rotateZ(((Math.random() - 0.5) * maxInaccuracyAngle).toRadians.toFloat)
        .rotateY(((Math.random() - 0.5) * maxInaccuracyAngle).toRadians.toFloat)
    val movement = Movement.linear(
      caster.eyeLocation,
      modifiedVelocity,
      range
    )
    val effect = new MagicScatter.ParticleEffect(movement)
    projectileService.createProjectile(this, movement, hitbox, effect.cancel)
  }

  worldService.playSound(caster.location, castSound, soundCategory)

  override def onHit(entity: Entity): Boolean = {
    entityService.damageEntity(entity.id, snapshot.damage)
    worldService.playSound(entity.location, hitSound, soundCategory)
    true
  }

  override def onMiss(entity: Entity): Boolean = false

  override def onMaxRange(location: Location): Boolean = {
    val entityList = entityService.getNearbyEntities(location, homingRange).filter {
      _.team != caster.team
    }
    if (target.nonEmpty || entityList.nonEmpty) {
      val projectileTarget =
        if (target.isDefined) target.get
        else {
          var nearest: Entity = null
          var nearestDistance = Float.NaN
          for (entity <- entityList) {
            val distance = entity.location.distanceTo(location)
            if (
              (nearest == null || nearestDistance == Float.NaN) || (distance < nearestDistance)
            ) {
              nearest = entity
              nearestDistance = distance
            }
          }
          nearest
        }
      val metadata = Map("location" -> location, "target" -> projectileTarget)
      abilityService.spawnChild("Projectile", this, metadata)
    }
    true
  }
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
      Set(RangedAbility.ENTITY_TARGET_OPTIONAL)

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


