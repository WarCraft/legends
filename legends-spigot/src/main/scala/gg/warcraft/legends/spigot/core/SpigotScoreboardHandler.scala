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
  private final val footer =
    "\\nJoin your team on the WarCraft: Discord at discord.warcraft.gg!"
  private final val textJson = "{\"text\": \"%s\"}"

  private val dwarves = LegendsTeam.Dwarves.instance
  private val elves = LegendsTeam.Elves.instance
  private val humans = LegendsTeam.Humans.instance
  private val orcs = LegendsTeam.Orcs.instance

  private val factionsHeader =
    s"${dwarves.chatColor}${dwarves.name} $RESET| " +
      s"${elves.chatColor}${elves.name} $RESET| " +
      s"${humans.chatColor}${humans.name} $RESET| " +
      s"${orcs.chatColor}${orcs.name}"

  private val warHeader =
    s"""
      |${GOLD}WarCraft: War
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
    s"""
       |${YELLOW}WarCraft: Skirmish
       |$factionsHeader
       |
       |Points
       |%s$RESET
       |
       |Skirmish ends in %s
       |%s
       |""".stripMargin

  private val noGameHeader =
    s"""
       |${GOLD}WarCraft
       |The next game will start shortly!
       |""".stripMargin

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

  private def formatNumbers(map: Map[Team, Int]): String = {
    val dwarvesNumbers = map.getOrElse(dwarves, 0)
    val elvesNumbers = map.getOrElse(elves, 0)
    val humansNumbers = map.getOrElse(humans, 0)
    val orcsNumbers = map.getOrElse(orcs, 0)
    s"${dwarves.chatColor}$dwarvesNumbers $RESET| " +
      s"${elves.chatColor}$elvesNumbers $RESET| " +
      s"${humans.chatColor}$humansNumbers $RESET| " +
      s"${orcs.chatColor}$orcsNumbers"
  }

  private def formatScore(game: Game): String =
    formatNumbers(game.monthlyScore)

  private def formatPoints(game: Game): String =
    formatNumbers(game.dailyPoints)

  private def formatObjectives(game: Game): String = {
    val dwarvesFort = objectiveService.objectives("DWARF_FORT")
    val elvesFort = objectiveService.objectives("ELF_FORT")
    val humansFort = objectiveService.objectives("HUMAN_FORT")
    val orcsFort = objectiveService.objectives("ORC_FORT")
    s"${dwarvesFort.occupiers.map { _.chatColor }.getOrElse(WHITE)}F $RESET| " +
      s"${elvesFort.occupiers.map { _.chatColor }.getOrElse(WHITE)}F $RESET| " +
      s"${humansFort.occupiers.map { _.chatColor }.getOrElse(WHITE)}F $RESET| " +
      s"${orcsFort.occupiers.map { _.chatColor }.getOrElse(WHITE)}F"
  }

  override def run(): Unit = {
    val header = gameService.activeGame match {
      case Some(game) =>
        val points = formatPoints(game)
        val timer = ""
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
