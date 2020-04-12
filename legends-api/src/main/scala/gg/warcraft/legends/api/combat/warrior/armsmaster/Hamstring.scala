package gg.warcraft.legends.api.combat.warrior.armsmaster

import gg.warcraft.combat.api.ability.MeleeAbility
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.Movement
import gg.warcraft.monolith.api.core.Duration
import gg.warcraft.monolith.api.effect.{
  EffectRenderer, EffectVectors, Particle, ParticleAdapter
}
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.math.Vector3f
import gg.warcraft.monolith.api.world.{Sound, SoundCategory, WorldService}

object Hamstring {
  class Factory(
      implicit entityService: EntityService,
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

  class Effect(movement: Movement, duration: Duration)( // TODO maybe implement Effect and Status as a method that returns anon impl?
      implicit particleAdapter: ParticleAdapter
  ) extends gg.warcraft.monolith.api.effect.Effect() {
    private final val radius = 0.5f
    private final val frames = 20
    // TODO make VectorUtils with vectors for HEAD_OFFSET, CHEST_OFFSET, HEEL_OFFSET etc
    private final val offset = Vector3f(0, 0.1f, 0) // TODO add to movement

    private val particle = Particle.SPELL_MOB.color(Particle.Color.WHITE)
    private val vectors = EffectVectors.ring(radius, frames)
    private val renderer = EffectRenderer.iterative(particle, vectors)
    addRenderer(renderer)

    cancel(duration)
  }
}

class Hamstring(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(
    implicit entityService: EntityService,
    worldService: WorldService
) extends MeleeAbility {
  private final val soundCategory = SoundCategory.HOSTILE
  private final val hitSound = Sound.ENTITY_SILVERFISH_DEATH
  private final val missSound = Sound.ENTITY_FISHING_BOBBER_THROW

  override def onHit(entity: Entity): Unit = {
    entityService damageEntity (entity.id, snapshot.damage)
    entityService addPotionEffect (entity.id, snapshot.slow)
    // TODO warriorEffectFactory.createHamstringEffect(entity, slow.getDuration());
    worldService playSound (entity.location, hitSound, soundCategory, 1, 1)
  }

  override def onMiss(entity: Entity): Unit = {
    worldService playSound (entity.location, missSound, soundCategory, 1, 1)
  }
}
