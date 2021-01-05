/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.challenge

import gg.warcraft.legends.api.LegendsConfig
import gg.warcraft.legends.api.player.currency.LegendsCurrency
import gg.warcraft.monolith.api.core.Message
import gg.warcraft.monolith.api.player.currency.CurrencyService
import gg.warcraft.monolith.api.player.{Player, PlayerService}
import gg.warcraft.monolith.api.util.option._

import java.util.UUID
import java.util.logging.Logger
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Random, Success}

class ChallengeService(implicit
    repository: ChallengeRepository,
    currencyService: CurrencyService,
    playerService: PlayerService,
    context: ExecutionContext,
    logger: Logger
) {
  private var _challenges: Map[String, Challenge] = Map.empty
  private var dailies: List[Challenge] = Nil
  private var monthlies: List[Challenge] = Nil
  def challengesById: Map[String, Challenge] = _challenges

  private var _activeDailies: List[Challenge] = Nil
  private var _activeMonthlies: List[Challenge] = Nil
  def activeDailies: List[Challenge] = _activeDailies
  def activeMonthlies: List[Challenge] = _activeMonthlies

  private var _completedChallenges: Map[UUID, Map[String, Challenge]] = Map.empty
  def completedChallenges: Map[UUID, Map[String, Challenge]] = _completedChallenges

  def readConfig(config: LegendsConfig): Unit = {
    config.challenges.foreach { it =>
      _challenges += (it.id -> it)
      it match {
        case it if it.id.startsWith("DAILY")   => dailies ::= it
        case it if it.id.startsWith("MONTHLY") => monthlies ::= it
      }
    }

    val activeChallenges = repository.loadActive()
    _activeDailies = activeChallenges.filter { _.isDaily }
    if (_activeDailies.size != 3) rotateDailies()
    _activeMonthlies = activeChallenges.filter { _.isMonthly }
    if (_activeMonthlies.size != 10) rotateMonthlies()
  }

  def rotateDailies(): Unit = {
    _activeDailies = Nil
    repository.deleteActiveDailies().onComplete {
      case Success(_) =>
        _activeDailies = Random.shuffle(dailies).take(3)
        repository.saveActive(_activeDailies)
      case Failure(exception) =>
        logger.severe { s"Failed to rotate dailies: ${exception.getMessage}" }
    }
  }

  def rotateMonthlies(): Unit = {
    _activeMonthlies = Nil
    repository.deleteActiveMonthlies().onComplete {
      case Success(_) =>
        _activeMonthlies = Random.shuffle(monthlies).take(10)
        repository.saveActive(_activeMonthlies)
      case Failure(exception) =>
        logger.severe { s"Failed to rotate monthlies: ${exception.getMessage}" }
    }
  }

  def completeChallenge(player: Player, challenge: Challenge): Unit = {
    repository.saveCompleted(player.id, challenge)
    _completedChallenges = _completedChallenges.updatedWith(player.id) {
      case Some(challenges) => challenges + (challenge.id -> challenge)
      case None             => Map.empty + (challenge.id -> challenge)
    }

    currencyService.addCurrency(LegendsCurrency.Gold, challenge.reward, player.id)
    val completionMessage =
      Message.server(s"You have completed the ${challenge.name} challenge!")
    playerService.sendMessage(player.id, completionMessage)
    val rewardMessage =
      Message.server(s"You gained ${challenge.reward} favor.")
    playerService.sendMessage(player.id, rewardMessage)
  }
}
