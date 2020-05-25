package gg.warcraft.legends.api.combat.warrior.guardian

import gg.warcraft.combat.api.ability.MeleeAbility
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.Movement
import gg.warcraft.legends.api.combat.warrior.guardian.Hamstring.ParticleEffect
import gg.warcraft.monolith.api.core.Duration
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.effect.{
  Effect, EffectRenderer, EffectVectors, Particle, ParticleAdapter
}
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.math.Vector3f
import gg.warcraft.monolith.api.world.{Sound, SoundCategory, WorldService}

object Hamstring {
  class Factory(implicit
      taskService: TaskService,
      particleAdapter: ParticleAdapter,
      entityService: EntityService,
      worldService: WorldService
  ) extends MeleeAbility.Factory {
    override val flags: Set[MeleeAbility.Flag] =
      Set(MeleeAbility.TARGET_REQUIRED)

    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        target: Option[Entity]
    ): MeleeAbility = new Hamstring(caster, snapshot)
  }

  class ParticleEffect(movement: Movement, duration: Duration)(implicit
      taskService: TaskService,
      particleAdapter: ParticleAdapter
  ) extends Effect(() => movement.get + Vector3f.Offset.feet) {
    private final val radius = 0.5f
    private final val frames = 20

    private val particle = Particle.SpellMob
    private val vectors = EffectVectors.ring(radius, frames)
    private val renderer = EffectRenderer.iterative(particle, vectors)
    addRenderer(renderer)

    cancel(duration)
  }
}

class Hamstring(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(implicit
    taskService: TaskService,
    particleAdapter: ParticleAdapter,
    entityService: EntityService,
    worldService: WorldService
) extends MeleeAbility {
  private final val soundCategory = SoundCategory.HOSTILE
  private final val hitSound = Sound.ENTITY_SILVERFISH_DEATH
  private final val missSound = Sound.ENTITY_FISHING_BOBBER_THROW

  override def onHit(entity: Entity): Unit = {
    entityService.damageEntity(entity.id, snapshot.damage)
    entityService.addPotionEffect(entity.id, snapshot.slow)
    new ParticleEffect(Movement.entity(entity), snapshot.slow.duration)
    worldService.playSound(entity.location, hitSound, soundCategory)
  }

  override def onMiss(entity: Entity): Unit = {
    worldService.playSound(entity.location, missSound, soundCategory)
  }
}
