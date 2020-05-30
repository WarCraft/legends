package gg.warcraft.legends.spigot.combat

import gg.warcraft.legends.api.entity.LegendsTeam._
import gg.warcraft.monolith.api.core.{Cancellable, Duration}
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.core.Duration._
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.entity.EntityTeamChangedEvent
import gg.warcraft.monolith.api.player.{
  Player, PlayerConnectEvent, PlayerDisconnectEvent, PlayerService
}
import gg.warcraft.monolith.api.util.chaining._
import gg.warcraft.monolith.spigot.player.SpigotPlayer
import org.bukkit.{ChatColor, Server}
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.Plugin
import org.bukkit.scoreboard.DisplaySlot

class PersonalScoreboard(player: SpigotPlayer)(implicit
    server: Server,
    plugin: Plugin,
    taskService: TaskService,
    playerService: PlayerService
) extends Event.Handler
    with Runnable
    with Cancellable {
  import PersonalScoreboard._

  private val scoreboard = server.getScoreboardManager.getNewScoreboard
  private val task = taskService.runTask(4.ticks, run)

  private var cooldowns: Map[String, Long] = Map.empty

  {
    val scoreboardMetadata = new FixedMetadataValue(plugin, this)
    player.setMetadata(metadataKey, scoreboardMetadata)
    player.setScoreboard(scoreboard)

    initializeTeams()
    initializeHealthDisplay()
  }

  private def initializeTeams(): Unit = {
    (Humans :: Orcs :: Dwarves :: Elves :: Nil).foreach { team =>
      val scoreboardTeam = scoreboard.registerNewTeam(team)
      scoreboardTeam.setPrefix(team.chatColor.toString)
    }

    playerService.getOnlinePlayers.foreach { player =>
      player.team match {
        case Some(team) =>
          val scoreboardTeam = scoreboard.getTeam(team.name)
          scoreboardTeam.addEntry(player.name)
        case None =>
      }
    }
  }

  private def initializeHealthDisplay(): Unit = {
    val healthObjective = scoreboard.registerNewObjective(
      healthObjective,
      healthCriteria,
      healthObjective
    )
    healthObjective.setDisplaySlot(DisplaySlot.BELOW_NAME)
    healthObjective.setDisplayName(s"${ChatColor.RED}❤")
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
          currentCooldowns += (ability -> (remainingCooldown / 1000))
    }

    if (currentCooldowns.nonEmpty) {
      val cooldownObjective = scoreboard.registerNewObjective(
        cooldownObjective,
        dummyCriteria,
        cooldownObjective
      )
      cooldownObjective.setDisplaySlot(DisplaySlot.SIDEBAR)
      currentCooldowns.foreach {
        case (ability, cooldown) =>
          cooldownObjective.getScore(ability).setScore(cooldown)
      }
    }
  }

  override def cancel(): Unit = task.cancel()

  override def handle(event: Event): Unit = event match {
    case PlayerConnectEvent(player) => player.team match {
        case Some(team) => scoreboard.getTeam(team) ?? { _.addEntry(player) }
        case None       =>
      }

    case PlayerDisconnectEvent(player) => player.team match {
        case Some(team) => scoreboard.getTeam(team) ?? { _.removeEntry(player) }
        case None       =>
      }

    case EntityTeamChangedEvent(entity: Player, oldTeam, newTeam) =>
      oldTeam match {
        case Some(team) => scoreboard.getTeam(team) ?? { _.removeEntry(entity) }
        case None       =>
      }
      newTeam match {
        case Some(team) => scoreboard.getTeam(team) ?? { _.addEntry(entity) }
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
