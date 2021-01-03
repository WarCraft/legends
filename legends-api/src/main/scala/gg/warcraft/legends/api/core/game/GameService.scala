/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.game

import gg.warcraft.legends.api.LegendsConfig
import gg.warcraft.monolith.api.core.Duration._
import gg.warcraft.monolith.api.core.data.{ServerDataRepository, ServerDataService}
import gg.warcraft.monolith.api.core.event.EventService
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.entity.team.{Team, TeamService}
import gg.warcraft.monolith.api.util.option._
import gg.warcraft.siege.api.objective.ObjectiveService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class GameService(implicit
    eventService: EventService,
    serverDataService: ServerDataService,
    serverDataRepository: ServerDataRepository,
    gameRepository: GameRepository,
    taskService: TaskService,
    teamService: TeamService,
    objectiveService: ObjectiveService
) {
  private final val active_war = "active_war"
  private final val active_skirmish = "active_skirmish"

  private final val tallyTimer = 900.seconds
  private final val skirmishTimer = 1800.seconds
  private final val dailyWinnerMonthlyScore = 5
  private final val skirmishWinnerMonthlyScore = 1

  private var _config: LegendsConfig = _

  private var _activeWar: Option[Game] = _
  def activeWar: Option[Game] = _activeWar
  private var _activeSkirmish: Option[Game] = _
  def activeSkirmish: Option[Game] = _activeSkirmish

  def readConfig(config: LegendsConfig): Unit = {
    _config = config
    serverDataRepository.load(active_war).foreach { id =>
      _activeWar = gameRepository.load(id.toLong)
    }
    serverDataRepository.load(active_skirmish).foreach { id =>
      _activeSkirmish = gameRepository.load(id.toLong)
    }
  }

  private def initGame(game: Game): Unit =
    game.objectives.foreach { case (id, team) =>
      objectiveService.setOccupiers(id, team)
    }

  def tickActiveGame(): Unit =
    _activeSkirmish match {
      case Some(game @ Game(_, _, None, timer, _, _, _)) =>
        if (timer.seconds > 1) {
          _activeSkirmish = game.copy(timer = game.timer - 1.seconds)
        } else endSkirmish()
      case None => _activeWar match {
          case Some(game @ Game(_, _, None, timer, _, _, _)) =>
            if (timer.seconds > 1) {
              _activeWar = game.copy(timer = game.timer - 1.seconds)
            } else {
              runTally()

              val lastScoreDate = // TODO this should probably be triggered from daily tick event
                if (game.monthlyScore.isEmpty) game.startTime else game
              /*
                     runTally();

                     War updatedWar = (War) gameRepository.getActiveGame();
                     SortedMap<String, Map<LegendsTeam, Integer>> dailyPoints = updatedWar.getDailyPoints();
                     String lastScoreDate = dailyPoints.isEmpty()
                             ? updatedWar.getStartTime()
                             : updatedWar.getDailyPoints().lastKey();
                     int lastScoreDay = LocalDateTime.parse(lastScoreDate).getDayOfMonth();
                     int currentDay = LocalDateTime.now().getDayOfMonth();
                     if (lastScoreDay != currentDay) {
                         runScore();

                         if (currentDay == 1) {
                             endWar();
                             startWar();
                         } else {
                             War mostUpToDateWar = (War) gameRepository.getActiveGame();
                             Duration newTimer = timeUtils.createDurationInSeconds(TALLY_TIMER_IN_SECONDS);
                             War newMostUpToDateWar = mostUpToDateWar.getCopyer()
                                     .withTimer(newTimer)
                                     .copy();
                             gameRepository.save(newMostUpToDateWar);
                         }
                     } else {
                         War mostUpToDateWar = (War) gameRepository.getActiveGame();
                         Duration newTimer = timeUtils.createDurationInSeconds(TALLY_TIMER_IN_SECONDS);
                         War newMostUpToDateWar = mostUpToDateWar.getCopyer()
                                 .withTimer(newTimer)
                                 .copy();
                         gameRepository.save(newMostUpToDateWar);
                     }
                     break;
               */
            }
          case None =>
        }
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
          serverDataRepository.save(active_war, id.toString)
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

      val updatedWar = war.copy(dailyPoints = updatedDailyPoints)
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
          serverDataRepository.save(active_skirmish, id.toString)

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
        serverDataRepository.delete(active_skirmish)

        val winners = winningTeams.head._1
        eventService << SkirmishFinishedEvent(updatedSkirmish, winners)

        initGame(_activeWar.get)
      }
    case None => throw new IllegalStateException(
        s"Failed to end skirmish with no active skirmish!"
      )
  }
}
