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
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.effect.{
  Effect, EffectRenderer, EffectVectors, Particle, ParticleAdapter
}
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.math.Vector3f
import gg.warcraft.monolith.api.world.{Location, Sound, SoundCategory, WorldService}

class ArcaneMissile(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot,
    target: Entity
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

  private val movement = Movement.homing(
    caster.eyeLocation,
    snapshot.velocity.modified,
    () => target.location + Vector3f.Offset.chest
  )
  private val hitbox = Hitbox.sphere(snapshot.radius.modified, _.team != caster.team)
  private val effect = new ArcaneMissile.ParticleEffect(movement)
  projectileService.createProjectile(this, movement, hitbox, effect.stop)

  worldService.playSound(caster.location, castSound, soundCategory)

  override def onHit(entity: Entity): Boolean = {
    entityService.damageEntity(entity.id, snapshot.damage)
    worldService.playSound(entity.location, hitSound, soundCategory)
    true
  }

  override def onMiss(entity: Entity): Boolean = false
}

object ArcaneMissile {
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
    ): RangedAbility = new ArcaneMissile(caster, snapshot, entityTarget.get)
  }

  class ParticleEffect(movement: Movement)(implicit
      taskService: TaskService,
      particleAdapter: ParticleAdapter
  ) extends Effect {
    private final val radius = 0.1f
    private final val innerParticleCount = 1
    private final val innerParticleSpeed = 1
    private final val outerParticleCount = 6

    private final val innerParticle =
      Particle.EnchantmentTable.speed(innerParticleSpeed, innerParticleCount)
    private final val innerVectors = EffectVectors.origin()
    private final val innerRenderer = EffectRenderer(innerParticle, innerVectors)

    private final val outerParticle = Particle.DragonBreath
    private final val outerVectors = EffectVectors.sphere(radius, outerParticleCount)
    private final val outerRenderer = EffectRenderer(outerParticle, outerVectors)

    override protected val location: () => Location = movement.get

    addRenderer(innerRenderer)
    addRenderer(outerRenderer)
  }
}
