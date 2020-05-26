/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat

import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.entity.{EntityDamageEvent, EntityDeathEvent}

class KillAssistHandler(implicit
    taskService: TaskService,
    killAssistService: KillAssistService
) extends Event.Handler {
  override def handle(event: Event): Unit = event match {
    case EntityDamageEvent(entity, damage) =>
      damage.source.entityId match {
        case Some(attackerId) =>
          killAssistService.registerAssist(entity.id, attackerId)
        case None =>
      }
    case EntityDeathEvent(entity, _) =>
      taskService.evalNextTick { killAssistService.resetAssists(entity.id) }
    case _ =>
  }
}
