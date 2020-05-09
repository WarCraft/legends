package gg.warcraft.legends.api.core

import java.util.UUID

import gg.warcraft.legends.api.LegendsConfig
import gg.warcraft.monolith.api.combat.PotionEffect
import gg.warcraft.monolith.api.combat.PotionEffect._
import gg.warcraft.monolith.api.core.Duration._
import gg.warcraft.monolith.api.core.Message
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.entity.EntityService
import gg.warcraft.monolith.api.entity.team.Team
import gg.warcraft.monolith.api.player.{Player, PlayerService}
import gg.warcraft.monolith.api.world.Location

class SpawnService(implicit
    taskService: TaskService,
    entityService: EntityService,
    playerService: PlayerService
) {
  private final val warpDuration = 10.seconds
  private final val warpEffect = PotionEffect(CONFUSION, 0, warpDuration + 4.seconds)
  private final val warpCooldown = (20 * 60).seconds
  private final val warpMessage = Message.server(
    "You can feel The Monolith's energy reaching out for you as it pulls you into The Realm's Weave."
  )

  private var spawnLocations: Map[Team, Location] = Map.empty
  private var playerCooldowns: Map[UUID, Long] = Map.empty

  def readConfig(config: LegendsConfig): Unit = {
    // TODO populate spawn points
    ???
  }

  def warpToSpawn(playerId: UUID): Boolean = {
    if (playerCooldowns.getOrElse(playerId, 0) <= System.currentTimeMillis) {
      entityService.addPotionEffect(playerId, warpEffect)
      entityService.freezeEntity(playerId, warpEffect.duration)
      playerService.sendMessage(playerId, warpMessage)

      taskService.evalLater(
        warpDuration, {
          playerService.getPlayerOption(playerId) match {
            case player: Player =>
              player.team match {
                case Some(team) =>
                  val spawnLocation = spawnLocations(team)
                  entityService.teleportEntity(playerId, spawnLocation)
                case None =>
              }
            case _ =>
          }
        }
      )

      val cooldown = System.currentTimeMillis() + warpCooldown.millis
      playerCooldowns += (playerId -> cooldown)
      true
    } else false
  }
}
