/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.ai

import gg.warcraft.monolith.api.combat.CombatSource
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.entity.{Entity, EntityDamageEvent}
import gg.warcraft.monolith.api.entity.status.Status
import gg.warcraft.monolith.api.player.Player

abstract class CombatPetAI(pet: Entity, owner: Player) extends Runnable {
  private final val maxDistance = 60
  private final val maxCombatDistance = 90

  private var combatTarget: Option[Entity] = None
  private var inCombat = false

  override def run(): Unit = {
    if (pet.location.distanceTo(owner.location) > maxDistance) teleportBack()
  }

  protected def teleportBack(): Unit
}

object CombatPetAI {
  class StatusEffect(
      override val source: CombatSource,
      ai: CombatPetAI
  ) extends Status.Effect {
    override final val name = "combat_pet_ai"
    override final val group = "monolith:passive"
    override final val duration = None

    override def handle(event: Event): Unit = event match {
      case EntityDamageEvent(_, damage) => damage.source.entityId match {
        case Some(attacker) => // TODO ai.attackedBy(attacker)
        case None           =>
      }
    }
  }
}
