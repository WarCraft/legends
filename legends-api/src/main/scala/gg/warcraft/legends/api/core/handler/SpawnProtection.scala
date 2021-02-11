/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.handler

import gg.warcraft.monolith.api.combat.{CombatSource, CombatValue}
import gg.warcraft.monolith.api.core.Duration
import gg.warcraft.monolith.api.core.Duration._
import gg.warcraft.monolith.api.core.event.{Event, PreEvent}
import gg.warcraft.monolith.api.entity.EntityPreDamageEvent
import gg.warcraft.monolith.api.entity.status.{Status, StatusService}
import gg.warcraft.monolith.api.player.PlayerRespawnEvent
import gg.warcraft.monolith.api.util.option._

object SpawnProtection {
  class Handler(implicit statusService: StatusService) extends Event.Handler {
    override def handle(event: Event): Unit = event match {
      case PlayerRespawnEvent(player, _) =>
        val source = CombatSource(player.name, player.id)
        statusService.addEffect(player.id, new Status(source))
      case _ =>
    }
  }

  class Status(override val source: CombatSource) extends Status.Effect {
    override val name = "Spawn Protection"
    override val group = "legends"
    override val duration: Option[Duration] = 10.seconds

    private val modifier = CombatValue.Modifier.`override`(source, 0)

    override def reduce[T <: PreEvent](event: T): T = event match {
      case it @ EntityPreDamageEvent(_, damage, _, _) =>
        val updatedDamage = damage.copy(modifiers = modifier :: damage.modifiers)
        it.copy(damage = updatedDamage).asInstanceOf[T]
      case _ => event
    }
  }
}
