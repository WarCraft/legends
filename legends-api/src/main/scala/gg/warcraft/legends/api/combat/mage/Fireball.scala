package gg.warcraft.legends.api.combat.mage

import gg.warcraft.combat.api.{Hitbox, Movement}
import gg.warcraft.combat.api.ability.RangedAbility
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.projectile.ProjectileService
import gg.warcraft.monolith.api.block.Block
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.effect.{
  EffectRenderer, EffectVectors, Particle, ParticleAdapter
}
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.world.{Sound, SoundCategory, WorldService}

object Fireball {
  class Factory(
      implicit entityService: EntityService,
      projectileService: ProjectileService,
      worldService: WorldService,
      particleAdapter: ParticleAdapter,
      taskService: TaskService
  ) extends RangedAbility.Factory {
    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        blockTarget: Option[Block],
        entityTarget: Option[Entity]
    ): RangedAbility = new Fireball(caster, snapshot)
  }

  class Effect(movement: Movement)(
      implicit particleAdapter: ParticleAdapter,
      taskService: TaskService
  ) extends gg.warcraft.monolith.api.effect.Effect(movement.get) {
    private final val radius = 0.2f
    private final val particleCount = 20

    {
      val innerParticle = Particle.FLAME
      val innerVectors = EffectVectors.origin()
      val innerRenderer = EffectRenderer(innerParticle, innerVectors)
      addRenderer(innerRenderer)

      val outerParticle = Particle.REDSTONE.color(Particle.Color.ORANGE)
      val outerVectors = EffectVectors.sphere(radius, particleCount)
      val outerRenderer = EffectRenderer(outerParticle, outerVectors)
      addRenderer(outerRenderer)
    }
  }
}

class Fireball(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(
    implicit entityService: EntityService,
    projectileService: ProjectileService,
    worldService: WorldService,
    particleAdapter: ParticleAdapter,
    taskService: TaskService
) extends RangedAbility {
  private final val maxDistance = 30f
  private final val soundCategory = SoundCategory.HOSTILE
  private final val castSound = Sound.ENTITY_GHAST_SHOOT

  {
    val location = caster.eyeLocation
    val direction = location.rotation
    val velocity = direction multiply snapshot.speed.modified
    val movement = Movement.linear(location, velocity, maxDistance)
    val hitbox = Hitbox.sphere(snapshot.radius.modified, _.team != caster.team)
    val effect = new Fireball.Effect(movement)
    projectileService createProjectile (this, movement, hitbox, () => effect.cancel())
    worldService playSound (caster.location, castSound, soundCategory, 1, 1)
    // TODO play sound at fly locations (maybe use onTick for this or create something for sound similar to effect)
  }

  override def onHit(entity: Entity): Boolean = {
    entityService damageEntity (entity.id, snapshot.damage)
    entityService burnEntity (entity.id, snapshot.burn)
    true
  }

  override def onMiss(entity: Entity): Boolean = false
}
