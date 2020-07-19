/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat.mage.wizard

import gg.warcraft.combat.api.{Hitbox, Movement}
import gg.warcraft.combat.api.ability.{Ability, ChildAbility, RangedAbility}
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.projectile.ProjectileService
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.effect.{
  Effect, EffectRenderer, EffectVectors, Particle, ParticleAdapter
}
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.math.Vector3f
import gg.warcraft.monolith.api.world.{Location, Sound, SoundCategory, WorldService}
class MagicScatterProjectile(
    override val parent: Ability,
    override val snapshot: AbilitySnapshot,
    metadata: Map[String, Any]
)(implicit
    taskService: TaskService,
    entityService: EntityService,
    projectileService: ProjectileService,
    worldService: WorldService,
    particleAdapter: ParticleAdapter
) extends ChildAbility
    with RangedAbility {
  private final val soundCategory = SoundCategory.HOSTILE
  private final val castSound = Sound.ENTITY_ENDER_DRAGON_HURT
  private final val hitSound = Sound.ENTITY_ENDER_DRAGON_HURT
  private val target = metadata("target").asInstanceOf[Entity]
  private val hitbox = Hitbox.sphere(snapshot.radius.modified, _.team != caster.team)
  private val location = metadata("location").asInstanceOf[Location]

  private val movement = Movement.homing(
    location,
    snapshot.velocity.modified,
    () => target.location + Vector3f.Offset.chest
  )
  private val effect = new MagicScatterProjectile.ParticleEffect(movement)
  projectileService.createProjectile(this, movement, hitbox, effect.stop)

  worldService.playSound(caster.location, castSound, soundCategory)

  override def onHit(entity: Entity): Boolean = {
    entityService.damageEntity(entity.id, snapshot.damage)
    worldService.playSound(entity.location, hitSound, soundCategory)
    true
  }

  override def onMiss(entity: Entity): Boolean = false
}

object MagicScatterProjectile {

  class Factory(implicit
      taskService: TaskService,
      entityService: EntityService,
      projectileService: ProjectileService,
      worldService: WorldService,
      particleAdapter: ParticleAdapter
  ) extends ChildAbility.Factory {
    override def create(
        parent: Ability,
        snapshot: AbilitySnapshot,
        metadata: Map[String, Any]
    ): ChildAbility = new MagicScatterProjectile(parent, snapshot, metadata)
  }

  class ParticleEffect(movement: Movement)(implicit
      taskService: TaskService,
      particleAdapter: ParticleAdapter
  ) extends Effect {
    private final val innerParticle = Particle.Redstone.color(Particle.Color.Purple)
    private final val innerVectors = EffectVectors.origin()
    private final val innerRenderer = EffectRenderer(innerParticle, innerVectors)

    override protected val location: () => Location = movement.get

    addRenderer(innerRenderer)
    start()
  }
}
