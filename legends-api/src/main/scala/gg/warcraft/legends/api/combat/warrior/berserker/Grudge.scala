/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat.warrior.berserker

import gg.warcraft.combat.api.ability.MeleeAbility
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.Movement
import gg.warcraft.legends.api.combat.warrior.berserker.Grudge.{
  ParticleEffect, StatusEffect
}
import gg.warcraft.monolith.api.combat.{CombatSource, CombatValue}
import gg.warcraft.monolith.api.core.Duration
import gg.warcraft.monolith.api.core.Duration._
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.effect.{
  Effect, EffectRenderer, EffectVectors, Particle, ParticleAdapter
}
import gg.warcraft.monolith.api.entity.{Entity, EntityDamageEvent, EntityService}
import gg.warcraft.monolith.api.entity.status.{Status, StatusService}
import gg.warcraft.monolith.api.math.Vector3f
import gg.warcraft.monolith.api.util.option._
import gg.warcraft.monolith.api.world.{Sound, SoundCategory, WorldService}

object Grudge {
  class Factory(implicit
      taskService: TaskService,
      particleAdapter: ParticleAdapter,
      entityService: EntityService,
      statusService: StatusService,
      worldService: WorldService
  ) extends MeleeAbility.Factory {
    override val flags: Set[MeleeAbility.Flag] =
      Set(MeleeAbility.TARGET_REQUIRED)

    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        target: Option[Entity]
    ): MeleeAbility = new Grudge(caster, snapshot)
  }

  class ParticleEffect(movement: Movement, duration: Duration)(implicit
      taskService: TaskService,
      particleAdapter: ParticleAdapter
  ) extends Effect(() => movement.get + Vector3f.Offset.aboveHead, 5.ticks) {
    private val particle = Particle.VillagerAngry
    private val vectors = EffectVectors.origin()
    private val renderer = EffectRenderer(particle, vectors)
    addRenderer(renderer)

    cancel(duration)
  }

  class StatusEffect(
      override val source: CombatSource,
      override val duration: Option[Duration],
      heal: CombatValue
  )(implicit
      entityService: EntityService
  ) extends Status.Effect {
    override val name: String = "Grudge"
    override val group: String = "debuff"

    private val casterId = source.entityId.get

    override def handle(event: Event): Unit = event match {
      case EntityDamageEvent(_, damage) =>
        damage.source.entityId match {
          case Some(entityId) =>
            if (entityId == casterId) entityService.healEntity(casterId, heal)
          case None =>
        }
      case _ =>
    }
  }
}

class Grudge(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(implicit
    taskService: TaskService,
    particleAdapter: ParticleAdapter,
    statusService: StatusService,
    worldService: WorldService,
    entityService: EntityService
) extends MeleeAbility {
  private final val soundCategory = SoundCategory.HOSTILE
  private final val hitSound = Sound.ENTITY_WITHER_SPAWN
  private final val missSound = Sound.ENTITY_FISHING_BOBBER_THROW

  override def onHit(entity: Entity): Unit = {
    val source = CombatSource(caster.name, Some(caster.id))
    val statusEffect = new StatusEffect(source, snapshot.duration, snapshot.heal)
    statusService.addEffect(entity.id, statusEffect)

    new ParticleEffect(Movement.entity(entity), snapshot.duration)
    worldService.playSound(entity.location, hitSound, soundCategory)
  }

  override def onMiss(entity: Entity): Unit =
    worldService.playSound(entity.location, missSound, soundCategory)
}
