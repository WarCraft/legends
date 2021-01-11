/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.core

import gg.warcraft.legends.api.LegendsConfig
import gg.warcraft.legends.api.core.game.{Game, GameService}
import gg.warcraft.legends.api.entity.LegendsTeam
import gg.warcraft.legends.api.entity.LegendsTeam._
import gg.warcraft.monolith.api.core.ColorCode._
import gg.warcraft.monolith.api.core.Duration
import gg.warcraft.monolith.api.core.FormatCode._
import gg.warcraft.monolith.api.core.data.ServerDataService
import gg.warcraft.monolith.api.entity.team.{Team, TeamService}
import gg.warcraft.siege.api.objective.ObjectiveService
import org.bukkit.Server

import scala.jdk.CollectionConverters._

class SpigotScoreboardHandler(config: LegendsConfig)(implicit
    server: Server,
    teamService: TeamService,
    gameService: GameService,
    objectiveService: ObjectiveService,
    serverDataService: ServerDataService
) extends Runnable {
  private final val footer = """
      |Join your team on the WarCraft: Discord at discord.warcraft.gg!""".stripMargin
  private final val textJson = "{\"text\": \"%s\"}"

  private val dwarves = LegendsTeam.Dwarves.instanceOption
  private val elves = LegendsTeam.Elves.instanceOption
  private val humans = LegendsTeam.Humans.instanceOption
  private val orcs = LegendsTeam.Orcs.instanceOption

  private val factionsHeader = (dwarves, elves, humans, orcs) match {
    case (Some(dwarves), Some(elves), Some(humans), Some(orcs)) =>
      s"${dwarves.chatColor}${dwarves.name} $RESET| " +
        s"${elves.chatColor}${elves.name} $RESET| " +
        s"${humans.chatColor}${humans.name} $RESET| " +
        s"${orcs.chatColor}${orcs.name}"

    case _ => "Factions not configured"
  }

  private val warHeader =
    s"""${GOLD}WarCraft: War
      |Next skirmish in %s
      |$factionsHeader
      |
      |Monthly Score
      |%s$RESET
      |
      |Daily Points
      |%s$RESET
      |
      |Next tally in %s
      |%s
      |""".stripMargin

  private val skirmishHeader =
    s"""${YELLOW}WarCraft: Skirmish
       |$factionsHeader
       |
       |Points
       |%s$RESET
       |
       |Skirmish ends in %s
       |%s
       |""".stripMargin

  private val noGameHeader =
    s"""${GOLD}WarCraft
       |The next game will start shortly!
       |""".stripMargin

  private def formatTimer(timer: Duration): String = {
    val minutes = timer.seconds / 60
    val seconds = timer.seconds % 60
    val paused = if (gameService.paused) " (Paused)" else ""
    f"$minutes%02d:$seconds%02d$paused"
  }

  private def formatSkirmishCountdown(): String = {
    val now = serverDataService.serverTime.withSecond(0).withNano(0)
    val nextSkirmish = config.skirmishTimes
      .find { now.getHour < _ }
      .getOrElse(24 + config.skirmishTimes.head)
    val hours = nextSkirmish - now.getHour - 1
    val minutes = 60 - now.getMinute

    if (hours == 0) {
      if (minutes == 1) "1 minute"
      else s"$minutes minutes"
    } else if (hours == 1) "1 hour"
    else s"$hours hours"
  }

  private def formatNumbers(map: Map[Team, Int]): String =
    (dwarves, elves, humans, orcs) match {
      case (Some(dwarves), Some(elves), Some(humans), Some(orcs)) =>
        val dwarvesNumbers = map.getOrElse(dwarves, 0)
        val elvesNumbers = map.getOrElse(elves, 0)
        val humansNumbers = map.getOrElse(humans, 0)
        val orcsNumbers = map.getOrElse(orcs, 0)
        s"${dwarves.chatColor}$dwarvesNumbers $RESET| " +
          s"${elves.chatColor}$elvesNumbers $RESET| " +
          s"${humans.chatColor}$humansNumbers $RESET| " +
          s"${orcs.chatColor}$orcsNumbers"

      case _ => "Factions not configured"
    }

  private def formatScore(game: Game): String =
    formatNumbers(game.monthlyScore)

  private def formatPoints(game: Game): String =
    formatNumbers(game.dailyPoints)

  private def formatObjectives(game: Game): String = {
    val dwarvesFort = objectiveService.objectives.get("DWARF_FORT")
    val elvesFort = objectiveService.objectives.get("ELF_FORT")
    val humansFort = objectiveService.objectives.get("HUMAN_FORT")
    val orcsFort = objectiveService.objectives.get("ORC_FORT")
    (dwarvesFort, elvesFort, humansFort, orcsFort) match {
      case (Some(dwarvesFort), Some(elvesFort), Some(humansFort), Some(orcsFort)) =>
        s"${dwarvesFort.occupiers.map { _.chatColor }.getOrElse(WHITE)}F $RESET| " +
          s"${elvesFort.occupiers.map { _.chatColor }.getOrElse(WHITE)}F $RESET| " +
          s"${humansFort.occupiers.map { _.chatColor }.getOrElse(WHITE)}F $RESET| " +
          s"${orcsFort.occupiers.map { _.chatColor }.getOrElse(WHITE)}F"

      case _ => "Objectives not configured"
    }
  }

  override def run(): Unit = {
    val header = gameService.activeGame match {
      case Some(game) =>
        val points = formatPoints(game)
        val timer = formatTimer(game.timer)
        val objectives = formatObjectives(game)
        if (gameService.activeSkirmish.nonEmpty) {
          skirmishHeader.format(points, timer, objectives)
        } else if (gameService.activeWar.nonEmpty) {
          val skirmishCountdown = formatSkirmishCountdown()
          val score = formatScore(game)
          warHeader.format(skirmishCountdown, score, points, timer, objectives)
        } else noGameHeader

      case None => noGameHeader
    }

    server.getOnlinePlayers.asScala.foreach { player =>
      player.setPlayerListHeader(header)
      player.setPlayerListFooter(footer)
    }
  }
}
