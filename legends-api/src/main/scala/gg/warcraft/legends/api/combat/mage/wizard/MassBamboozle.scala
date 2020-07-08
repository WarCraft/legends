/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat.mage.wizard

import java.util.UUID

import gg.warcraft.combat.api.{Hitbox, Movement}
import gg.warcraft.combat.api.ability.{AbilityPreCastEvent, RangedAbility}
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.ability.RangedAbility.Flag
import gg.warcraft.combat.api.projectile.ProjectileService
import gg.warcraft.monolith.api.block.Block
import gg.warcraft.monolith.api.core.event.{Event, PreEvent}
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.effect.{
  Effect, EffectRenderer, EffectVectors, Particle, ParticleAdapter
}
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.math.Vector3f
import gg.warcraft.monolith.api.util.typing._
import gg.warcraft.monolith.api.world.{Location, WorldService}

class MassBamboozle(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot,
    blockTarget: Option[Block],
    entityTarget: Option[Entity]
)(implicit
    entityService: EntityService,
    projectileService: ProjectileService,
    worldService: WorldService,
    particleAdapter: ParticleAdapter,
    taskService: TaskService,
    repository: MassBamboozle.Repository,
    adapter: MassBamboozle.Adapter
) extends RangedAbility {
  private val location =
    if (entityTarget.isDefined) entityTarget.get.location
    else blockTarget.get.location.toLocation + (.5f, 1f, .5f)
  private val movement = Movement.oneOff(location)
  private val hitbox = Hitbox.sphere(snapshot.radius.modified, _.team != caster.team)

  private val effectMovement = Movement.stationary(location)
  private val effect = new MassBamboozle.ParticleEffect(effectMovement)
  taskService.evalLater(
    snapshot.countdown,
    projectileService.createProjectile(this, movement, hitbox, effect.stop)
  )

  override def onHit(entity: Entity): Boolean = {
    adapter.disguise(entity.id)
    repository.save(entity.id)
    taskService.runLater(
      snapshot.duration,
      () => {
        adapter.reset(entity.id)
        repository.remove(entity.id)
      }
    )
    false
  }

  override def onMiss(entity: Entity): Boolean = false
}

object MassBamboozle {
  class Factory(implicit
      entityService: EntityService,
      projectileService: ProjectileService,
      worldService: WorldService,
      particleAdapter: ParticleAdapter,
      taskService: TaskService,
      repository: MassBamboozle.Repository,
      adapter: MassBamboozle.Adapter
  ) extends RangedAbility.Factory {
    override val flags: Set[Flag] =
      Set(RangedAbility.TARGET_REQUIRED)

    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        blockTarget: Option[Block],
        entityTarget: Option[Entity]
    ): RangedAbility = new MassBamboozle(caster, snapshot, blockTarget, entityTarget)
  }

  class ParticleEffect(movement: Movement)(implicit
      particleAdapter: ParticleAdapter,
      taskService: TaskService
  ) extends Effect {
    private final val radius = 5f
    private final val particleCount = 80

    private final val particle = Particle.DragonBreath
    private final val vectors = EffectVectors.ring(radius, particleCount)
    private final val renderer = EffectRenderer(particle, vectors)

    override protected val location: () => Location =
      () => movement.get + Vector3f.Offset.feet

    addRenderer(renderer)
  }

  class Repository {
    private var _bamboozled: Set[UUID] = Set.empty

    def bamboozled: Set[UUID] = _bamboozled
    def save(entityId: UUID): Unit = _bamboozled += entityId
    def remove(entityId: UUID): Unit = _bamboozled -= entityId
  }

  class Handler(implicit repository: Repository) extends Event.Handler {
    override def reduce[T <: PreEvent](event: T): T = event match {
      case event @ AbilityPreCastEvent(_, caster, _, _, _) =>
        if (!repository.bamboozled.contains(caster.id)) event.as[T]
        else event.copy(cancelled = true).as[T]
      case _ => event
    }
  }

  trait Adapter {
    def disguise(entityId: UUID): Unit
    def reset(entityId: UUID): Unit
  }
}
