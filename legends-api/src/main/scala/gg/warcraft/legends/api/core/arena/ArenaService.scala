package gg.warcraft.legends.api.core.arena

import gg.warcraft.monolith.api.core.Message
import gg.warcraft.monolith.api.player.{Player, PlayerService}

import java.time.Instant
import java.util.UUID
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

private[arena] case class SoloQueueListing(
    playerId: UUID,
    since: Instant
)

private[arena] case class DuoQueueListing(
    playerId1: UUID,
    playerId2: UUID,
    since: Instant
)

class ArenaService(implicit
    playerService: PlayerService
) {
  private val QUE_SOLO_JOIN =
    Message.server("You have joined the queue to fight in the arena.")
  private val QUE_SOLO_LEAVE =
    Message.server("You have left the queue to fight in the arena.")

  private val QUE_DUO_CHALLENGE =
    "You have challenged %s to a fight in the arena. When they accept you will both be entered into the queue."
  private val QUE_DUO_CHALLENGED =
    "%s has challenged you to a fight in the arena. [Shift Right Click] on them to open their player card and accept."

  private val QUE_DUO_ACCEPT =
    "You have accepted %s's challenge. You have joined the queue to fight in the arena."
  private val QUE_DUO_ACCEPTED =
    "%s has accepted your challenge. You have joined the queue to fight in the arena."

  private val QUE_DUO_FORFEIT =
    "%s has forfeited their challenge to fight in the arena."
  private val QUE_DUO_LEFT =
    "%s has left the queue to fight in the arena. You have been removed from the queue."

  private[arena] val _soloQueue: ListBuffer[SoloQueueListing] = ListBuffer.empty
  private[arena] val _duoQueue: ListBuffer[DuoQueueListing] = ListBuffer.empty
  private[arena] val _challenges: mutable.HashMap[UUID, UUID] = mutable.HashMap.empty

  private[arena] var _currentGame: Option[(UUID, UUID)] = None
  def currentGame: Option[(UUID, UUID)] = _currentGame

  private[arena] var _nextGame: Option[(UUID, UUID)] = None
  def nextGame: Option[(UUID, UUID)] = _nextGame

  def soloQueueSize: Int = _soloQueue.size
  def duoQueueSize: Int = _duoQueue.size * 2

  def isSignedUp(player: Player): Boolean =
    _soloQueue.exists { _.playerId == player.id } ||
      _duoQueue.exists { it =>
        it.playerId1 == player.id || it.playerId2 == player.id
      }

  def approximateFightsBefore(player: Player): Int = {
    val since = _soloQueue.find { _.playerId == player.id } match {
      case Some(listing) => listing.since
      case None => _duoQueue.find { it =>
          it.playerId1 == player.id || it.playerId2 == player.id
        } match {
          case Some(listing) => listing.since
          case None          => Instant.now()
        }
    }
    _soloQueue.count { _.since.isBefore(since) } / 2 +
      _duoQueue.count { _.since.isBefore(since) }
  }

  def joinSoloQueue(player: Player): Unit = {
    _soloQueue += SoloQueueListing(player.id, Instant.now())
    player.sendMessage(QUE_SOLO_JOIN)
  }

  def leaveSoloQueue(player: Player): Unit = {
    val index = _soloQueue.indexWhere { _.playerId == player.id }
    _soloQueue.remove(index)

    player.sendMessage(QUE_SOLO_LEAVE)
  }

  def joinDuoQueue(player: Player, other: Player): Unit = {
    _challenges.get(other.id) match {
      case Some(challengedId) =>
        if (player.id == challengedId) {
          _duoQueue += DuoQueueListing(challengedId, player.id, Instant.now())
          _challenges.remove(challengedId)

          val acceptMsg = Message.server(QUE_DUO_ACCEPT.format(other.name))
          player.sendMessage(acceptMsg)
          val acceptedMsg = Message.server(QUE_DUO_ACCEPTED.format(player.name))
          other.sendMessage(acceptedMsg)
        } else {
          playerService.getPlayerOption(challengedId) match {
            case Some(challenged) =>
              val forfeitMsg = Message.server(QUE_DUO_FORFEIT.format(player.name))
              challenged.sendMessage(forfeitMsg)

            case None => // do nothing
          }
        }

      case None => // do below
    }
    _challenges += (player.id -> other.id)

    val challengeMsg = Message.server(QUE_DUO_CHALLENGE.format(other.name))
    player.sendMessage(challengeMsg)
    val challengedMsg = Message.server(QUE_DUO_CHALLENGED.format(player.name))
    other.sendMessage(challengedMsg)
  }

  def leaveDuoQueue(player: Player): Unit = {
    val index = _duoQueue.indexWhere { it =>
      it.playerId1 == player.id || it.playerId2 == player.id
    }
    if (index >= 0) {
      _duoQueue.remove(index) match {
        case DuoQueueListing(playerId1, playerId2, _) =>
          val otherPlayerId = if (player.id != playerId1) playerId1 else playerId2
          playerService.getPlayerOption(otherPlayerId) match {
            case Some(otherPlayer) =>
              val leftMsg = Message.server(QUE_DUO_LEFT.format(player.name))
              otherPlayer.sendMessage(leftMsg)

            case None => // do nothing
          }
      }

      player.sendMessage(QUE_SOLO_LEAVE)
    }
  }
}
