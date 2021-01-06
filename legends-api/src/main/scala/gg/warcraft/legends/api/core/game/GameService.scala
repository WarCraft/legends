/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.game

import gg.warcraft.legends.api.LegendsConfig
import gg.warcraft.monolith.api.core.Duration._
import gg.warcraft.monolith.api.core.data.ServerDataService
import gg.warcraft.monolith.api.core.event.EventService
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.entity.team.{Team, TeamService}
import gg.warcraft.monolith.api.util.option._
import gg.warcraft.siege.api.objective.{Objective, ObjectiveService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class GameService(implicit
    eventService: EventService,
    serverDataService: ServerDataService,
    gameRepository: GameRepository,
    taskService: TaskService,
    teamService: TeamService,
    objectiveService: ObjectiveService
) {
  private final val active_war_data = "active_war"
  private final val active_skirmish_data = "active_skirmish"

  private final val tallyTimer = 900.seconds
  private final val skirmishTimer = 1800.seconds
  private final val dailyWinnerMonthlyScore = 5
  private final val skirmishWinnerMonthlyScore = 1

  private var _config: LegendsConfig = _

  private var _activeWar: Option[Game] = _
  def activeWar: Option[Game] = _activeWar
  private var _activeSkirmish: Option[Game] = _
  def activeSkirmish: Option[Game] = _activeSkirmish

  private def activeGame_=(game: Game): Unit =
    if (_activeSkirmish.nonEmpty) _activeSkirmish = game
    else _activeWar = game
  def activeGame: Option[Game] = _activeSkirmish.orElse(_activeWar)

  def readConfig(config: LegendsConfig): Unit = {
    _config = config
    serverDataService.load(active_war_data).foreach { id =>
      _activeWar = gameRepository.load(id.toLong)
    }
    serverDataService.load(active_skirmish_data).foreach { id =>
      _activeSkirmish = gameRepository.load(id.toLong)
    }
  }

  private def initGame(game: Game): Unit =
    game.objectives.foreach { case (id, team) =>
      objectiveService.setOccupiers(id, team)
    }

  private[game] def captureObjective(objective: Objective, team: Team): Unit =
    activeGame match {
      case Some(game) =>
        val updatedObjectives = game.objectives + (objective.id -> team)
        var updatedGame = activeGame.get.copy(objectives = updatedObjectives)

        if (_activeSkirmish.nonEmpty) {
          val objectivePoints = _config.objectiveTallyPoints(objective.id)
          val updatedPoints = updatedGame.dailyPoints.updatedWith(team) {
            case Some(points) => points + objectivePoints
            case None         => objectivePoints
          }
          updatedGame = updatedGame.copy(dailyPoints = updatedPoints)
        }

        activeGame = updatedGame
        gameRepository.save(updatedGame)
      case None =>
    }

  private[game] def neutralizeObjective(objective: Objective): Unit =
    activeGame match {
      case Some(game) =>
        val updatedObjectives = game.objectives - objective.id
        val updatedGame = activeGame.get.copy(objectives = updatedObjectives)
        activeGame = updatedGame
        gameRepository.save(updatedGame)
      case None =>
    }

  private[game] def awardKillingBlow(team: Team): Unit =
    activeGame match {
      case Some(game) =>
        val updatedPoints = game.dailyPoints.updatedWith(team) {
          case Some(points) => points + 1
          case None         => 1
        }
        val updatedGame = game.copy(dailyPoints = updatedPoints)
        activeGame = updatedGame
        gameRepository.save(updatedGame)
      case None =>
    }

  def tickActiveGame(): Unit =
    activeGame match {
      case Some(game) =>
        if (game.timer.seconds > 0) {
          activeGame = game.copy(timer = game.timer - 1.seconds)
        } else if (_activeSkirmish.nonEmpty) {
          endSkirmish()
        } else runTally()
      case None =>
    }

  def startWar(): Unit = _activeWar match {
    case None =>
      val startTime = serverDataService.serverTime
      val defaultObjectives = _config.objectiveDefaultOccupiers
      val war =
        Game(0, startTime, timer = tallyTimer, objectives = defaultObjectives)
      gameRepository.save(war).onComplete {
        case Success(id) =>
          _activeWar = war.copy(id = id)
          serverDataService.save(active_war_data, id.toString)
          taskService.evalNextTick(eventService << WarStartedEvent(_activeWar.get))
        case Failure(ex) => throw ex
      }
    case Some(war) => throw new IllegalStateException(
        s"Failed to start a new war while ${war.id} is still running!"
      )
  }

  def runTally(): Unit = _activeWar match {
    case Some(war) =>
      val updatedDailyPoints = objectiveService.objectives
        .foldLeft(Map.empty[Team, Int]) { case (accumulator, (_, objective)) =>
          objective.occupiers match {
            case Some(team) =>
              val objectivePoints = _config.objectiveTallyPoints(objective.id)
              accumulator.updatedWith(team) {
                case Some(points) => points + objectivePoints
                case None         => objectivePoints
              }
            case None => accumulator
          }
        }

      val updatedWar = war.copy(timer = tallyTimer, dailyPoints = updatedDailyPoints)
      _activeWar = updatedWar
      gameRepository.save(updatedWar)

      eventService << WarTallyEvent(updatedWar, war)
    case None => throw new IllegalStateException(
        s"Failed to run tally with no active war!"
      )
  }

  def runScore(): Unit = _activeWar match {
    case Some(war) =>
      val winningPoints = war.dailyPoints.maxBy { _._2 }._2
      val winningTeams = war.dailyPoints.filter { _._2 == winningPoints }
      if (winningTeams.size == 1) {
        val updatedScore = war.monthlyScore.updatedWith(winningTeams.head._1) {
          case Some(score) => score + dailyWinnerMonthlyScore
          case None        => dailyWinnerMonthlyScore
        }

        // TODO archive daily points?

        val updatedWar =
          war.copy(dailyPoints = Map.empty, monthlyScore = updatedScore)
        _activeWar = updatedWar
        gameRepository.save(updatedWar)
      } else {} // wait with running score until 1 team gets ahead in points
    case None => throw new IllegalStateException(
        s"Failed to run score with no active war!"
      )
  }

  def endWar(): Unit = _activeWar match {
    case Some(war) =>
      val updatedWar = war.copy(endTime = serverDataService.serverTime)
      _activeWar = None
      gameRepository.save(updatedWar)

      val winners = war.dailyPoints.maxBy { _._2 }._1
      eventService << WarFinishedEvent(updatedWar, winners)
    case None => throw new IllegalStateException(
        s"Failed to end war with no active war!"
      )
  }

  def startSkirmish(): Unit = _activeSkirmish match {
    case None =>
      val startTime = serverDataService.serverTime
      val defaultObjectives = _config.objectiveDefaultOccupiers
      val skirmish =
        Game(0, startTime, timer = skirmishTimer, objectives = defaultObjectives)
      gameRepository.save(skirmish).onComplete {
        case Success(id) =>
          _activeSkirmish = skirmish.copy(id = id)
          serverDataService.save(active_skirmish_data, id.toString)

          initGame(_activeSkirmish.get)

          taskService.evalNextTick(
            eventService << SkirmishStartedEvent(_activeSkirmish.get)
          )
        case Failure(ex) => throw ex
      }
    case Some(skirmish) => throw new IllegalStateException(
        s"Failed to start skirmish while ${skirmish.id} is still running!"
      )
  }

  def endSkirmish(): Unit = _activeSkirmish match {
    case Some(skirmish) =>
      val winningPoints = skirmish.dailyPoints.maxBy { _._2 }._2
      val winningTeams = skirmish.dailyPoints.filter { _._2 == winningPoints }
      if (winningTeams.size == 1) {
        _activeWar match {
          case Some(war) =>
            val updatedScore = war.monthlyScore.updatedWith(winningTeams.head._1) {
              case Some(score) => score + skirmishWinnerMonthlyScore
              case None        => skirmishWinnerMonthlyScore
            }
            val updatedWar = war.copy(monthlyScore = updatedScore)
            _activeWar = updatedWar
            gameRepository.save(updatedWar)
          case None =>
        }
      }

      if (winningTeams.size > 1) {
        // wait with running score until 1 team gets ahead in points
      } else {
        // don't delay skirmish end if no one is playing
        val endTime = serverDataService.serverTime
        val updatedSkirmish = skirmish.copy(endTime = endTime)
        _activeSkirmish = None
        gameRepository.save(updatedSkirmish)
        serverDataService.delete(active_skirmish_data)

        val winners = winningTeams.head._1
        eventService << SkirmishFinishedEvent(updatedSkirmish, winners)

        initGame(_activeWar.get)
      }
    case None => throw new IllegalStateException(
        s"Failed to end skirmish with no active skirmish!"
      )
  }
}
