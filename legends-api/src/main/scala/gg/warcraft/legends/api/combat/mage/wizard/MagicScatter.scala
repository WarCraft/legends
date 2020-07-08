/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat.mage.wizard

import gg.warcraft.combat.api.{Hitbox, Movement}
import gg.warcraft.combat.api.ability.{AbilityService, RangedAbility}
import gg.warcraft.combat.api.ability.RangedAbility.Flag
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.projectile.ProjectileService
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
  private final val maxInaccuracyAngle = 35f
  private val range = snapshot.range.modified
  private val count = snapshot.count.modified
  private val homingRange = snapshot.distance.modified
  private val location = caster.eyeLocation
  private val direction = location.rotation
  private val velocity = direction * snapshot.velocity.modified
  private val hitbox = Hitbox.sphere(snapshot.radius.modified, _.team != caster.team)

  (1 to count.toInt).foreach { _ =>
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
    projectileService.createProjectile(this, movement, hitbox, effect.stop)
  }

  worldService.playSound(caster.location, castSound, soundCategory)

  override def onHit(entity: Entity): Boolean = {
    entityService.damageEntity(entity.id, snapshot.damage)
    worldService.playSound(entity.location, hitSound, soundCategory)
    true
  }

  override def onMiss(entity: Entity): Boolean = false

  override def onMaxRange(location: Location): Boolean = {
    val projectileTarget: Option[Entity] = target.orElse {
      entityService.getNearbyEntities(location, homingRange)
        .filter { _.team != caster.team }
        .foldLeft((None, Float.MaxValue): (Option[Entity], Float)) {
          (accumulator, entity) =>
            val distance = entity.location.distanceTo(location)
            if (distance < accumulator._2) (Some(entity), distance) else accumulator
        }._1
    }
    if (projectileTarget.isDefined) {
      val metadata = Map("location" -> location, "target" -> projectileTarget.get)
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
      particleAdapter: ParticleAdapter,
      abilityService: AbilityService
  ) extends RangedAbility.Factory {
    override val flags: Set[Flag] =
      Set(RangedAbility.ENTITY_TARGET_OPTIONAL)

    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        blockTarget: Option[Block],
        entityTarget: Option[Entity]
    ): RangedAbility = new MagicScatter(caster, snapshot, entityTarget)
  }

  class ParticleEffect(movement: Movement)(implicit
      taskService: TaskService,
      particleAdapter: ParticleAdapter
  ) extends Effect {
    private final val radius = 0.1f
    private final val outerParticleCount = 3

    private final val innerParticle = Particle.Redstone.color(Particle.Color.Purple)
    private final val innerVectors = EffectVectors.origin()
    private final val innerRenderer = EffectRenderer(innerParticle, innerVectors)

    private final val outerParticle = Particle.Redstone.color(Particle.Color.Fuchsia)
    private final val outerVectors = EffectVectors.sphere(radius, outerParticleCount)
    private final val outerRenderer = EffectRenderer(outerParticle, outerVectors)

    override protected val location: () => Location = movement.get

    addRenderer(innerRenderer)
    addRenderer(outerRenderer)
  }
}
