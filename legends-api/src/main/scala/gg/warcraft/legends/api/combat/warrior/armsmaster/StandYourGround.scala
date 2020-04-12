package gg.warcraft.legends.api.combat.warrior.armsmaster

import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.ability.MeleeAbility
import gg.warcraft.combat.api.Movement
import gg.warcraft.monolith.api.combat.CombatSource
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.core.Duration
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.effect.{
  Effect, EffectRenderer, EffectVectors, Particle, ParticleAdapter
}
import gg.warcraft.monolith.api.entity.{Entity, EntityDamageEvent, EntityService}
import gg.warcraft.monolith.api.entity.attribute.AttributeService
import gg.warcraft.monolith.api.entity.status.{Status, StatusService}
import gg.warcraft.monolith.api.world.{Sound, SoundCategory, WorldService}

object StandYourGround {
  class Factory(
      implicit taskService: TaskService,
      entityService: EntityService,
      attributeService: AttributeService,
      statusService: StatusService,
      worldService: WorldService,
      particleAdapter: ParticleAdapter
  ) extends MeleeAbility.Factory {
    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        target: Option[Entity]
    ): MeleeAbility = new StandYourGround(caster, snapshot)
  }

  class ParticleEffect(movement: Movement, duration: Duration)(
      implicit particleAdapter: ParticleAdapter,
      taskService: TaskService
  ) extends Effect(movement.get) {
    private final val radius = 2.5f
    private final val particleCount = 50
    private final val particle = Particle.CRIT_MAGIC

    cancel(duration)

    override def tick(): Unit = {
      clearRenderers()
      val vectors = EffectVectors.dome(radius, particleCount)
      val renderer = EffectRenderer(particle, vectors)
      addRenderer(renderer)
    }
  }

  class StatusEffect(
      override val source: CombatSource,
      override val duration: Option[Duration]
  )(
      implicit entityService: EntityService,
      worldService: WorldService
  ) extends Status.Effect {
    private final val soundCategory = SoundCategory.HOSTILE
    private final val hitSound = Sound.ENTITY_IRON_GOLEM_HURT

    override val name: String = "Stand Your Ground"
    override val group: String = "buff"

    override def handle(event: Event): Unit = event match {
      case EntityDamageEvent(entityId, _, _) =>
        val entity = entityService getEntity entityId
        worldService playSound (entity.location, hitSound, soundCategory, 1, 1)
      case _ =>
    }
  }
}

class StandYourGround(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(
    implicit entityService: EntityService,
    attributeService: AttributeService,
    statusService: StatusService,
    worldService: WorldService,
    particleAdapter: ParticleAdapter,
    taskService: TaskService
) extends MeleeAbility {
  private final val soundCategory = SoundCategory.HOSTILE
  private final val castSound = Sound.ENTITY_IRON_GOLEM_DEATH

  {
    entityService addPotionEffect (caster.id, snapshot.resistance)
    entityService addPotionEffect (caster.id, snapshot.fireResistance)
    entityService addPotionEffect (caster.id, snapshot.slow)

    // TODO
//    AttributeModifier knockbackModifier =
//        attributeCommandService.addAttributeModifier(caster.getId(), GenericAttribute.KNOCKBACK_RESISTANCE, 1);
//    taskService.runLater(() -> attributeCommandService.removeAttributeModifier(caster.getId(), knockbackModifier),
//      slow.getDuration());

    val movement = Movement.entity(caster)
    new StandYourGround.ParticleEffect(movement, snapshot.slow.duration)

    val status = new StandYourGround.StatusEffect(caster, snapshot.slow.duration)
    statusService addEffect (caster.id, status)

    worldService playSound (caster.location, castSound, soundCategory, 1, 1)
  }
}
