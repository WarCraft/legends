package gg.warcraft.legends.api.combat.mage.wizard

import gg.warcraft.combat.api.{Hitbox, Movement}
import gg.warcraft.combat.api.ability.RangedAbility
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.projectile.ProjectileService
import gg.warcraft.monolith.api.block.Block
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.effect.{
  Effect, EffectRenderer, EffectVectors, Particle, ParticleAdapter
}
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.math.Vector3f
import gg.warcraft.monolith.api.world.{Sound, SoundCategory, WorldService}

class ArcaneShield(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(implicit
    entityService: EntityService,
    projectileService: ProjectileService,
    worldService: WorldService,
    particleAdapter: ParticleAdapter,
    taskService: TaskService
) extends RangedAbility {
  private final val soundCategory = SoundCategory.HOSTILE
  private final val castSound = Sound.ENTITY_ENDER_DRAGON_HURT
  private final val hitSound = Sound.ENTITY_ENDER_DRAGON_HURT
  private final val orbitRadius = 1.5f
  private final val anglePerTick = 10
  private final val rotationsToEnd = 4
  private val count = snapshot.count.modified
  private val entityMovement = Movement.entity(caster)
  private val hitbox = Hitbox.sphere(snapshot.radius.modified, _.team != caster.team)

  private val anglePerProjectile = 360 / count

  (0 until count.toInt).foreach { current =>
    val movement = Movement.orbital(
      () => entityMovement.get + Vector3f.Offset.chest,
      anglePerProjectile * current,
      orbitRadius,
      anglePerTick,
      rotationsToEnd
    )
    val effect = new ArcaneShield.ParticleEffect(movement)
    projectileService.createProjectile(this, movement, hitbox, effect.cancel)
  }
  worldService playSound (caster.location, castSound, soundCategory)

  override def onHit(entity: Entity): Boolean = {
    entityService.damageEntity(entity.id, snapshot.damage)
    worldService.playSound(entity.location, hitSound, soundCategory)
    true
  }

  override def onMiss(entity: Entity): Boolean = false
}

object ArcaneShield {
  class Factory(implicit
      entityService: EntityService,
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
    ): RangedAbility = new ArcaneShield(caster, snapshot)
  }

  class ParticleEffect(movement: Movement)(implicit
      taskService: TaskService,
      particleAdapter: ParticleAdapter
  ) extends Effect(movement.get) {

    private val radius = 0.1f
    private val count = 1
    private val witchParticleSpeed = 0f

    private val particlePurple = Particle.Redstone.color(Particle.Color.Purple)
    private val particleWitch = Particle.SpellWitch.speed(witchParticleSpeed, count)
    private val vectors = EffectVectors.sphere(radius, count)
    private val rendererPurple = EffectRenderer(particlePurple, vectors)
    private val rendererWitch = EffectRenderer(particleWitch, vectors)
    addRenderer(rendererPurple)
    addRenderer(rendererWitch)
  }
}
