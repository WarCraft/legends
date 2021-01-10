/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.handler

import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.combat.api.util.entity._
import gg.warcraft.legends.api.entity.LegendsTeam.TeamOps
import gg.warcraft.legends.api.player.level.LevelService
import gg.warcraft.legends.api.util.player._
import gg.warcraft.monolith.api.core.Message
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.entity.EntityInteractEvent
import gg.warcraft.monolith.api.player.{Player, PlayerService}

class PlayerInspectHandler(implicit
    playerService: PlayerService,
    combatDataService: CombatDataService,
    levelService: LevelService
) extends Event.Handler {
  override def handle(event: Event): Unit = event match {
    case EntityInteractEvent(inspectedPlayer: Player, inspectingPlayer, _) =>
      if (inspectingPlayer.isSneaking) {
        val name = inspectedPlayer.name
        val level = inspectedPlayer.level
        val team = inspectedPlayer.team
          .map { _.kind }
          .getOrElse("Wayfarer")
        val combatClass = inspectedPlayer.combatClass
          .map { _.name }
          .getOrElse("Beginner")
        val text =
          s"You are looking at $name, a level ${level.value} $team $combatClass."
        val message = Message.server(text)
        playerService.sendMessage(inspectingPlayer.id, message)
      }
    case _ =>
  }
}
