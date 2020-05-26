/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat.hunter

import gg.warcraft.combat.api.combatclass.PlayerCombatClassChangedEvent
import gg.warcraft.monolith.api.combat.{Projectile, ProjectileLaunchEvent}
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.item.Arrow
import gg.warcraft.monolith.api.item.variant.ArrowVariant
import gg.warcraft.monolith.api.player.PlayerService

class ArrowDistributionHandler(implicit
    taskService: TaskService,
    playerService: PlayerService
) extends Event.Handler {
  private final val arrow = Arrow(ArrowVariant.NORMAL, "Arrow", Nil, 1, Set(), true)

  override def handle(event: Event): Unit = event match {
    case ProjectileLaunchEvent(_, projectileType, shooterId) =>
      if (projectileType == Projectile.ARROW) {
        shooterId.flatMap { playerService.getPlayerOption } match {
          case Some(player) =>
            if (player.inventory.hasSpaceFor(arrow))
              playerService.giveItem(player.id, arrow)
          case None =>
        }
      }

    case PlayerCombatClassChangedEvent(player, _, current) =>
      current.map { _.permission } match {
        case Some("warcraft.marksman") | Some("warcraft.ranger") =>
          if (!player.inventory.contains(arrow))
            taskService.evalNextTick(playerService.giveItem(player.id, arrow))
        case _ =>
      }

    case _ =>
  }
}
