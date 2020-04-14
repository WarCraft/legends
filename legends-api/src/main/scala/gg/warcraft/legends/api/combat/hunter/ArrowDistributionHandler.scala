package gg.warcraft.legends.api.combat.hunter

import gg.warcraft.combat.api.specialization.PlayerChangeSpecializationEvent
import gg.warcraft.monolith.api.combat.{Projectile, ProjectileLaunchEvent}
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.item.Arrow
import gg.warcraft.monolith.api.item.variant.ArrowVariant
import gg.warcraft.monolith.api.player.PlayerService

class ArrowDistributionHandler(
    implicit taskService: TaskService,
    playerService: PlayerService
) extends Event.Handler {
  private final val arrow = Arrow(ArrowVariant.NORMAL, "Arrow", Nil, 1, Set(), true)

  override def handle(event: Event): Unit = event match {
    case it: ProjectileLaunchEvent =>
      if (it.projectileType == Projectile.ARROW) {
        it.shooterId flatMap playerService.getPlayerOption match {
          case Some(player) =>
            if (player.inventory.hasSpaceFor(arrow))
              playerService giveItem (player.id, arrow)
          case None =>
        }
      }

    case it: PlayerChangeSpecializationEvent =>
      it.current map { _.permission } match {
        case Some("warcraft.marksman") | Some("warcraft.ranger") =>
          val player = playerService getPlayer it.playerId
          if (!player.inventory.contains(arrow))
            taskService runNextTick { () =>
              playerService giveItem (player.id, arrow)
            }

        case None =>
      }

    case _ =>
  }
}
