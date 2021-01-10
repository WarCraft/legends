/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.combat

import gg.warcraft.combat.api.ability.AbilityCastEvent
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.entity.EntityTeamChangedEvent
import gg.warcraft.monolith.api.entity.team.TeamService
import gg.warcraft.monolith.api.player.{
  Player, PlayerConnectEvent, PlayerDisconnectEvent, PlayerService
}
import org.bukkit.Server
import org.bukkit.plugin.Plugin

import java.util.UUID

class PersonalScoreboardHandler(implicit
    server: Server,
    plugin: Plugin,
    taskService: TaskService,
    playerService: PlayerService,
    teamService: TeamService
) extends Event.Handler {
  private var scoreboards: Map[UUID, PersonalScoreboard] = Map.empty

  override def handle(event: Event): Unit = event match {
    case PlayerConnectEvent(player) =>
      scoreboards.values.foreach { _.handle(event) }
      val spigotPlayer = server.getPlayer(player.id)
      val scoreboard = new PersonalScoreboard(spigotPlayer)
      scoreboards += (player.id -> scoreboard)

    case PlayerDisconnectEvent(player) =>
      scoreboards(player.id).cancel()
      scoreboards -= player.id
      scoreboards.values.foreach { _.handle(event) }

    case AbilityCastEvent(ability, caster: Player, snapshot) =>
      scoreboards(caster.id).trackCooldown(ability, snapshot.cooldown)

    case EntityTeamChangedEvent(entity: Player, _, _) =>
      scoreboards.values.foreach { _.handle(event) }

    case _ =>
  }
}
