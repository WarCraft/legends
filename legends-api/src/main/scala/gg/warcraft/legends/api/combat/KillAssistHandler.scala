package gg.warcraft.legends.api.combat

import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.entity.{EntityDamageEvent, EntityDeathEvent}

class KillAssistHandler(implicit
    taskService: TaskService,
    killAssistService: KillAssistService
) extends Event.Handler {
  override def handle(event: Event): Unit = event match {
    case EntityDamageEvent(entityId, _, damage) =>
      damage.source.entityId match {
        case Some(attackerId) =>
          killAssistService.registerAssist(entityId, attackerId)
        case None =>
      }
    case EntityDeathEvent(entityId, _, _) =>
      taskService.evalNextTick { killAssistService.resetAssists(entityId) }
    case _ =>
  }
}
