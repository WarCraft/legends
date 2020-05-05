package gg.warcraft.legends.api.player.handler

import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.combat.api.util.combatplayer._
import gg.warcraft.legends.api.entity.LegendsTeam
import gg.warcraft.legends.api.util.legendsplayer._
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.core.Message
import gg.warcraft.monolith.api.entity.EntityInteractEvent
import gg.warcraft.monolith.api.player.PlayerService

class PlayerInspectHandler(implicit
    playerService: PlayerService,
    combatDataService: CombatDataService
) extends Event.Handler {
  override def handle(event: Event): Unit = event match {
    case EntityInteractEvent(entityId, _, playerId, _, _, _, _) =>
      val inspectingPlayer = playerService.getPlayer(playerId)
      if (inspectingPlayer.isSneaking) {
        val inspectedPlayer = playerService.getPlayer(entityId)
        val name = inspectedPlayer.name
        val level = inspectedPlayer.level
        val team = inspectedPlayer.team
          .map { _.asInstanceOf[LegendsTeam].kind }
          .getOrElse("Wayfarer")
        val combatClass = inspectedPlayer.combatClass
          .map { _.name }
          .getOrElse("Beginner")
        val text = s"You are looking at $name, a level $level $team $combatClass."
        val message = Message.server(text)
        playerService.sendMessage(playerId, message)
      }
    case _ =>
  }
}
