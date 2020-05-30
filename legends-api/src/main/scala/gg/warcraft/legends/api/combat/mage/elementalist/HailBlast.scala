/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat.mage.elementalist

import gg.warcraft.combat.api.{Hitbox, Movement}
import gg.warcraft.combat.api.ability.RangedAbility
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.projectile.ProjectileService
import gg.warcraft.monolith.api.block.Block
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.core.Duration._
import gg.warcraft.monolith.api.effect.{
  Effect, EffectRenderer, EffectVectors, Particle, ParticleAdapter
}
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.math.Vector3f
import gg.warcraft.monolith.api.world.{Location, Sound, SoundCategory, WorldService}

class HailBlast(
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
  private final val castSound = Sound.BLOCK_SNOW_BREAK
  private final val castSound2 = Sound.ENTITY_SILVERFISH_HURT

  private val origin = caster.eyeLocation
  private val velocity = origin.rotation * snapshot.velocity.modified
  private val movement =
    Movement.linear(caster.eyeLocation, velocity, snapshot.range.modified)
  private val hitbox = Hitbox.sphere(snapshot.radius.modified, _.team != caster.team)

  private val effect = new HailBlast.ParticleEffect(movement, origin.rotation)
  projectileService.createProjectile(this, movement, hitbox, effect.cancel)

  worldService.playSound(caster.location, castSound, soundCategory)
  worldService.playSound(caster.location, castSound2, soundCategory, 1, 0.1f)
  (2 to 12 by 2).foreach { i =>
    taskService.evalLater(
      i.ticks,
      worldService.playSound(caster.location, castSound, soundCategory)
    )
  }

  override def onHit(entity: Entity): Boolean = {
    entityService.damageEntity(entity.id, snapshot.damage)
    entityService.addPotionEffect(entity.id, snapshot.slow)
    false
  }

  override def onMiss(entity: Entity): Boolean = false

  override def onGrounded(location: Location): Boolean = false
}

object HailBlast {
  class Factory(implicit
      taskService: TaskService,
      entityService: EntityService,
      projectileService: ProjectileService,
      worldService: WorldService,
      particleAdapter: ParticleAdapter
  ) extends RangedAbility.Factory {
    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        blockTarget: Option[Block],
        entityTarget: Option[Entity]
    ): RangedAbility = new HailBlast(caster, snapshot)
  }

  class ParticleEffect(
      movement: Movement,
      direction: Vector3f
  )(implicit
      taskService: TaskService,
      particleAdapter: ParticleAdapter
  ) extends Effect(movement.get) {
    private final val iterations = 15
    private final val particle = Particle.Cloud

    private val (pitch, yaw) = direction.toPitchYaw

    private var iteration = 1

    cancel(iterations.ticks)

    override def tick(): Unit = {
      clearRenderers()

      val vectors = EffectVectors
        .circle(0.4f + 0.2f * iteration, 4 * iteration)
        .rotateAxisX(Math.toRadians(90 + pitch).toFloat)
        .rotateAxisY(Math.toRadians(-yaw).toFloat)
      val renderer = EffectRenderer(particle, vectors)
      addRenderer(renderer)

      iteration += 1
    }
  }
}
