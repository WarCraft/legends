package gg.warcraft.legends.api.core.arena

import gg.warcraft.legends.api.LegendsConfig
import gg.warcraft.monolith.api.block.box.BlockBox
import gg.warcraft.monolith.api.core.Message
import gg.warcraft.monolith.api.core.event.{Event, PreEvent}
import gg.warcraft.monolith.api.entity.{EntityDeathEvent, EntityPreRespawnEvent}
import gg.warcraft.monolith.api.player.{OfflinePlayer, Player, PlayerService}
import gg.warcraft.monolith.api.world.Location

import java.util.UUID
import scala.collection.mutable

class ArenaGameEndHandler(config: LegendsConfig)(implicit
    service: ArenaService,
    playerService: PlayerService
) extends Runnable
    with Event.Handler {

  private val ARENA_WIN_DEFEAT =
    "%s has defeated %s in the arena!"
  private val ARENA_WIN_DISCONNECT =
    "%1$s has defeated %2$s in the arena by forfeit! %2$s disconnected."
  private val ARENA_WIN_FORFEIT =
    "%1$s has defeated %2$s in the arena by forfeit! %2$s fell out of the ring."

  private val ARENA_DRAW_DISCONNECT =
    "Somehow %s and %s drew in the arena! They both disconnected."
  private val ARENA_DRAW_FORFEIT =
    "Somehow %s and %s drew in the arena! They both fell out of the ring."

  private val arenaGameArea: BlockBox = config.arenaGameArea
  private val arenaSpawnLocation: Location = config.arenaSpawnLocation
  private val respawnInArena: mutable.Set[UUID] = mutable.Set.empty

  override def run(): Unit = {
    service.currentGame match {
      case Some((playerId1, playerId2)) =>
        playerService.getPlayerOption(playerId1) match {
          case Some(player1) =>
            playerService.getPlayerOption(playerId2) match {
              case Some(player2) =>
                attemptEndGameByForfeit(player1, player2)

              case None =>
                val player2 = playerService.getOfflinePlayer(playerId2)
                winGameByDisconnect(player1, player2)
            }

          case None =>
            playerService.getPlayerOption(playerId2) match {
              case Some(player2) =>
                val player1 = playerService.getOfflinePlayer(playerId1)
                winGameByDisconnect(player2, player1)

              case None =>
                val player1 = playerService.getOfflinePlayer(playerId1)
                val player2 = playerService.getOfflinePlayer(playerId2)
                drawGameByDisconnect(player1, player2)
            }
        }

      case None => // do nothing
    }
  }

  private def winGameByDefeat(victor: Player, loser: Player): Unit = {
    val forfeit = ARENA_WIN_DEFEAT.format(victor.name, loser.name)
    val message = Message.server(forfeit)
    endGame(message)
  }

  private def winGameByDisconnect(victor: Player, loser: OfflinePlayer): Unit = {
    val forfeit = ARENA_WIN_DISCONNECT.format(victor.name, loser.name)
    val message = Message.server(forfeit)
    endGame(message)
  }

  private def drawGameByDisconnect(
      loser1: OfflinePlayer,
      loser2: OfflinePlayer
  ): Unit = {
    val forfeit = ARENA_DRAW_DISCONNECT.format(loser1.name, loser2.name)
    val message = Message.server(forfeit)
    endGame(message)
  }

  private def attemptEndGameByForfeit(player1: Player, player2: Player): Unit = {
    val player1OutOfBounds = !arenaGameArea.test(player1.location.toBlockLocation)
    val player2OutOfBounds = !arenaGameArea.test(player2.location.toBlockLocation)

    if (player1OutOfBounds) {
      if (player2OutOfBounds) {
        drawGameByForfeit(player1, player2)
      } else {
        winGameByForfeit(player2, player1)
      }
    } else {
      if (player2OutOfBounds) {
        winGameByForfeit(player1, player2)
      }
    }
  }

  private def winGameByForfeit(victor: Player, loser: Player): Unit = {
    val forfeit = ARENA_WIN_FORFEIT.format(victor.name, loser.name)
    val message = Message.server(forfeit)
    endGame(message)
  }

  private def drawGameByForfeit(loser1: Player, loser2: Player): Unit = {
    val forfeit = ARENA_DRAW_FORFEIT.format(loser1.name, loser2.name)
    val message = Message.server(forfeit)
    endGame(message)
  }

  private def endGame(message: Message): Unit = {
    playerService.getOnlinePlayers.foreach { _.sendMessage(message) }
    service._currentGame = None
  }

  override def handle(event: Event): Unit = event match {
    case EntityDeathEvent(entity, _) =>
      if (arenaGameArea.test(entity.location)) {
        respawnInArena.add(entity.id)
      }

      service.currentGame match {
        case Some((playerId1, playerId2)) =>
          lazy val player1 = playerService.getPlayer(playerId1)
          lazy val player2 = playerService.getPlayer(playerId2)
          entity.id match {
            case `playerId1` => winGameByDefeat(player2, player1)
            case `playerId2` => winGameByDefeat(player1, player2)
            case _           =>
          }

        case None =>
      }

    case _ =>
  }

  override def reduce[T <: PreEvent](event: T): T = event match {
//    case event @ EntityPreDeathEvent(entity, _) =>
//      service.currentGame match {
//        case Some((playerId1, playerId2)) =>
//          if (entity.id == playerId1 || entity.id == playerId2) {
//            event.copy(drops = List.empty).asInstanceOf[T]
//          } else event.asInstanceOf[T]
//
//        case None => event.asInstanceOf[T]
//      }

    case event @ EntityPreRespawnEvent(entity, _, _, _) =>
      if (respawnInArena.remove(entity.id)) {
        event.copy(location = arenaSpawnLocation).asInstanceOf[T]
      } else event.asInstanceOf[T]

    case _ => event
  }
}
