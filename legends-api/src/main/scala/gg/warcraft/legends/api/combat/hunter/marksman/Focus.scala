/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat.hunter.marksman

import gg.warcraft.combat.api.ability.{AbilityPreCastEvent, MeleeAbility}
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.Movement
import gg.warcraft.monolith.api.combat.{CombatSource, CombatValue}
import gg.warcraft.monolith.api.core.{Cancellable, ColorCode, Duration, Message}
import gg.warcraft.monolith.api.core.event.PreEvent
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.core.Duration._
import gg.warcraft.monolith.api.effect.{
  Effect, EffectRenderer, EffectVectors, Particle, ParticleAdapter
}
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.entity.status.{Status, StatusService}
import gg.warcraft.monolith.api.player.PlayerService

object Focus {
  class Factory(implicit
      taskService: TaskService,
      entityService: EntityService,
      playerService: PlayerService,
      statusService: StatusService,
      repository: FocusRepository,
      particleAdapter: ParticleAdapter
  ) extends MeleeAbility.Factory {
    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        target: Option[Entity]
    ): MeleeAbility = new Focus(caster, snapshot)
  }

  class ParticleEffect(movement: Movement)(implicit
      taskService: TaskService,
      particleAdapter: ParticleAdapter
  ) extends Effect(() => movement.get + (0, 0.1f, 0)) {
    private final val radius = 0.5f
    private final val frames = 20

    private final val particle = Particle.Redstone.color(Particle.Color.Red)
    private final val vectors = EffectVectors.ring(radius, frames)

    private val renderer = EffectRenderer.iterative(particle, vectors)
    addRenderer(renderer)
  }

  class StatusEffect(
      override val source: CombatSource,
      override val duration: Option[Duration],
      modifierValue: Float
  ) extends Status.Effect {
    override val name: String = "Focus"
    override val group: String = "buff"

    private val modifier = CombatValue.Modifier.percent(source, modifierValue)

    override def reduce[T <: PreEvent](event: T): T = event match {
      case event @ AbilityPreCastEvent(_, _, damageModifiers, _, _) =>
        event.copy(damageModifiers = modifier :: damageModifiers).asInstanceOf[T]
      case _ => event
    }
  }
}

class Focus(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(implicit
    taskService: TaskService,
    entityService: EntityService,
    playerService: PlayerService,
    statusService: StatusService,
    repository: FocusRepository,
    particleAdapter: ParticleAdapter
) extends MeleeAbility
    with Cancellable {
  private final val maxDuration = Integer.MAX_VALUE.ticks
  private final val text =
    "You're currently focussing and can not move, cast [Shift Left Click] again to cancel."

  private var status: Status.Effect = _
  private var effect: Effect = _

  repository.removeFocus(caster.id) match {
    case Some(focus) => focus.cancel()
    case None =>
      entityService.freezeEntity(caster.id, maxDuration)

      val source = CombatSource(caster.name, Some(caster.id))
      val modifier = snapshot.combatValue("damage-modifier").modified
      status = new Focus.StatusEffect(source, None, modifier)
      statusService.addEffect(caster.id, status)

      val movement = Movement.entity(caster)
      effect = new Focus.ParticleEffect(movement)

      repository.addFocus(this)

      val message = Message(text, s"${ColorCode.YELLOW}$text")
      playerService.sendMessage(caster.id, message)
  }

  override def cancel(): Unit = {
    entityService.freezeEntity(caster.id, 0.ticks)
    statusService.removeEffect(caster.id, status)
    effect.cancel()
  }
}
