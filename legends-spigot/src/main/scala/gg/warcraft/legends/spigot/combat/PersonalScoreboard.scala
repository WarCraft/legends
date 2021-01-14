/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.combat

import gg.warcraft.monolith.api.core.Duration._
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.core.{Cancellable, Duration, FormatCode}
import gg.warcraft.monolith.api.entity.EntityTeamChangedEvent
import gg.warcraft.monolith.api.entity.team.TeamService
import gg.warcraft.monolith.api.player.{
  Player, PlayerConnectEvent, PlayerDisconnectEvent, PlayerService
}
import gg.warcraft.monolith.api.util.chaining._
import gg.warcraft.monolith.spigot.player.SpigotPlayer
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.Plugin
import org.bukkit.scoreboard.{DisplaySlot, NameTagVisibility}
import org.bukkit.{ChatColor, Server}

class PersonalScoreboard(player: SpigotPlayer)(implicit
    server: Server,
    plugin: Plugin,
    taskService: TaskService,
    playerService: PlayerService,
    teamService: TeamService
) extends Event.Handler
    with Runnable
    with Cancellable {
  import PersonalScoreboard._

  private val scoreboard = server.getScoreboardManager.getNewScoreboard
  private val task = taskService.runTask(4.ticks, () => run())

  private var cooldowns: Map[String, Long] = Map.empty

  {
    val scoreboardMetadata = new FixedMetadataValue(plugin, this)
    player.setMetadata(metadataKey, scoreboardMetadata)
    player.setScoreboard(scoreboard)

    initializeTeams
    initializeHealthDisplay()
  }

  private def initializeTeams(implicit teamService: TeamService): Unit = {
    teamService.teams.values.foreach { team =>
      val scoreboardTeam = scoreboard.registerNewTeam(team.id)
      scoreboardTeam.setAllowFriendlyFire(false)
      scoreboardTeam.setNameTagVisibility(NameTagVisibility.NEVER)
      scoreboardTeam.setColor(ChatColor.getByChar(team.chatColor.code(1)))
      scoreboardTeam.setPrefix(team.chatColor.toString)
      scoreboardTeam.setSuffix(FormatCode.RESET.code)
    }

    playerService.getOnlinePlayers.foreach { player =>
      player.team match {
        case Some(team) =>
          val scoreboardTeam = scoreboard.getTeam(team.id)
          scoreboardTeam.addEntry(player.name)
        case None =>
      }
    }
  }

  private def initializeHealthDisplay(): Unit = {
    val objective = scoreboard.registerNewObjective(
      healthObjective,
      healthCriteria,
      healthObjective
    )
    objective.setDisplaySlot(DisplaySlot.BELOW_NAME)
    objective.setDisplayName(s"${ChatColor.RED}❤")
  }

  def trackCooldown(ability: String, cooldown: Duration): Unit =
    cooldowns += (ability -> (System.currentTimeMillis + cooldown.millis))

  override def run(): Unit = {
    scoreboard.getObjectives.forEach { objective =>
      if (objective.getDisplayName == cooldownObjective) objective.unregister()
    }

    var currentCooldowns: Map[String, Int] = Map.empty
    cooldowns.foreach {
      case (ability, cooldown) =>
        val remainingCooldown = cooldown - System.currentTimeMillis
        if (remainingCooldown > 0)
          currentCooldowns += (ability -> ((remainingCooldown / 1000).toInt + 1))
    }

    if (currentCooldowns.nonEmpty) {
      val objective = scoreboard.registerNewObjective(
        cooldownObjective,
        dummyCriteria,
        cooldownObjective
      )
      objective.setDisplaySlot(DisplaySlot.SIDEBAR)
      currentCooldowns.foreach {
        case (ability, cooldown) =>
          objective.getScore(ability).setScore(cooldown)
      }
    }
  }

  override def cancel(): Unit = task.cancel()

  override def handle(event: Event): Unit = event match {
    case PlayerConnectEvent(player) => player.team match {
        case Some(team) => scoreboard.getTeam(team.id) ?? { _.addEntry(player) }
        case None       =>
      }

    case PlayerDisconnectEvent(player) => player.team match {
        case Some(team) => scoreboard.getTeam(team.id) ?? { _.removeEntry(player) }
        case None       =>
      }

    case EntityTeamChangedEvent(entity: Player, oldTeam, newTeam) =>
      oldTeam match {
        case Some(team) => scoreboard.getTeam(team.id) ?? { _.removeEntry(entity) }
        case None       =>
      }
      newTeam match {
        case Some(team) => scoreboard.getTeam(team.id) ?? { _.addEntry(entity) }
        case None       =>
      }

    case _ =>
  }
}

object PersonalScoreboard {
  final val metadataKey = getClass.getCanonicalName
  final val cooldownObjective = "Cooldowns"
  final val dummyCriteria = "dummy"
  final val healthObjective = "Health"
  final val healthCriteria = "health"
}
