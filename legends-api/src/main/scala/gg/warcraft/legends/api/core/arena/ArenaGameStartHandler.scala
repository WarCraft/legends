package gg.warcraft.legends.api.core.arena

import gg.warcraft.legends.api.LegendsConfig
import gg.warcraft.monolith.api.core.Duration.DurationOps
import gg.warcraft.monolith.api.core.Message
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.entity.EntityService
import gg.warcraft.monolith.api.player.{OfflinePlayer, Player, PlayerService}
import gg.warcraft.monolith.api.world.Location

import java.time.Instant
import java.util.UUID
import scala.collection.mutable

class ArenaGameStartHandler(config: LegendsConfig)(implicit
    service: ArenaService,
    playerService: PlayerService,
    entityService: EntityService,
    taskService: TaskService
) extends Runnable {
  private val QUE_GAME_START =
    "The arena match between %s and %s will start in 10 seconds!"
  private val QUE_PARTNER_OFFLINE =
    "%s is currently offline, you'll stay at the front of the arena queue until they are back."

  private val partnerOfflineCache = mutable.HashMap[UUID, Long]()

  private val spawnPlayer1: Location = config.arenaSpawnPlayer1
  private val spawnPlayer2: Location = config.arenaSpawnPlayer2

  override def run(): Unit =
    if (service.currentGame.isEmpty && service.nextGame.isEmpty) {
      findSoloGame() match {
        case Some(soloListings) =>
          findDuoGame() match {
            case Some(duoListing) => startEarliestGame(soloListings, duoListing)
            case None             => startSoloGame(soloListings)
          }
        case None =>
          findDuoGame() match {
            case Some(duoListing) => startDuoGame(duoListing)
            case None             => // do nothing
          }
      }
    }

  private def findSoloGame(): Option[(SoloQueueListing, SoloQueueListing)] = {
    var waiting: Option[SoloQueueListing] = None
    service._soloQueue.foreach { listing =>
      playerService.getPlayerOption(listing.playerId) match {
        case Some(_) => waiting match {
            case Some(waiting) => return Some(waiting, listing)
            case None          => waiting = Some(listing)
          }
        case None => // continue
      }
    }
    None
  }

  private def findDuoGame(): Option[DuoQueueListing] = {
    service._duoQueue.foreach { listing =>
      playerService.getPlayerOption(listing.playerId1) match {
        case Some(player1) =>
          playerService.getPlayerOption(listing.playerId2) match {
            case Some(_) =>
              return Some(listing)
            case None =>
              val player2 = playerService.getOfflinePlayer(listing.playerId2)
              notifyPartnerOffline(player1, player2)
          }
        case None =>
          playerService.getPlayerOption(listing.playerId2) match {
            case Some(player2) =>
              val player1 = playerService.getOfflinePlayer(listing.playerId1)
              notifyPartnerOffline(player2, player1)
            case None => // continue
          }
      }
    }
    None
  }

  private def notifyPartnerOffline(player: Player, partner: OfflinePlayer): Unit = {
    val now = Instant.now().getEpochSecond
    partnerOfflineCache.get(player.id) match {
      case Some(timestamp) => if (now - timestamp < 30) return
      case None            => // continue
    }

    partnerOfflineCache.put(player.id, now)
    val message = Message.server(QUE_PARTNER_OFFLINE.format(partner.name))
    player.sendMessage(message)
  }

  private def startEarliestGame(
      soloListings: (SoloQueueListing, SoloQueueListing),
      duoListing: DuoQueueListing
  ): Unit = {
    val solosSince = soloListings._1.since
    if (solosSince.isBefore(duoListing.since)) startSoloGame(soloListings)
    else startDuoGame(duoListing)
  }

  private def startSoloGame(listings: (SoloQueueListing, SoloQueueListing)): Unit = {
    service._soloQueue -= listings._1
    service._soloQueue -= listings._2

    val player1 = playerService.getPlayer(listings._1.playerId)
    val player2 = playerService.getPlayer(listings._2.playerId)
    queueGame(player1, player2)
  }

  private def startDuoGame(listing: DuoQueueListing): Unit = {
    service._duoQueue -= listing

    val player1 = playerService.getPlayer(listing.playerId1)
    val player2 = playerService.getPlayer(listing.playerId2)
    queueGame(player1, player2)
  }

  private def queueGame(player1: Player, player2: Player): Unit = {
    val message = Message.server(QUE_GAME_START.format(player1.name, player2.name))
    playerService.getOnlinePlayers.foreach { _.sendMessage(message) }

    service._nextGame = Some(player1.id, player2.id)
    taskService.runLater(10.seconds, startGame)
  }

  private def startGame(): Unit = {
    service._currentGame = service._nextGame
    service._nextGame = None

    service.currentGame match {
      case Some((playerId1, playerId2)) =>
        entityService.teleportEntity(playerId1, spawnPlayer1)
        entityService.teleportEntity(playerId2, spawnPlayer2)
    }
  }
}
