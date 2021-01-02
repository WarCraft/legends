/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.spawn

import gg.warcraft.monolith.api.combat.PotionEffect
import gg.warcraft.monolith.api.combat.PotionEffect._
import gg.warcraft.monolith.api.core.ColorCode.RED
import gg.warcraft.monolith.api.core.Duration._
import gg.warcraft.monolith.api.core.Message
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.entity.EntityService
import gg.warcraft.monolith.api.player.PlayerService

import java.util.UUID

class SpawnService(implicit
    taskService: TaskService,
    entityService: EntityService,
    playerService: PlayerService
) {
  private final val warpDuration = 10.seconds
  private final val warpEffect = PotionEffect(CONFUSION, 0, warpDuration + 4.seconds)
  private final val warpCooldown = (20 * 60).seconds
  private final val warpMessage = Message.server(
    "You can feel The Monolith's energy reaching out as it pulls you into The Realm's Weave."
  )

  private var _cooldowns: Map[UUID, Long] = Map.empty
  def cooldowns: Map[UUID, Long] = _cooldowns

  def warpToSpawn(playerId: UUID): Boolean = {
    val cooldown = _cooldowns.getOrElse(playerId, 0L)
    if (cooldown <= System.currentTimeMillis) {
      entityService.addPotionEffect(playerId, warpEffect)
      entityService.freezeEntity(playerId, warpEffect.duration)
      playerService.sendMessage(playerId, warpMessage)

      taskService.evalLater(warpDuration, teleportToSpawn(playerId))
      val cooldown = System.currentTimeMillis() + warpCooldown.millis
      _cooldowns += (playerId -> cooldown)
      true
    } else {
      val delta = cooldown - System.currentTimeMillis
      val cooldownText =
        if (delta < 60_000) s"$RED${delta % 60} seconds"
        else s"$RED${delta % 60_000} minutes"
      val message = Message.server(s"You can warp to spawn in $cooldownText")
      playerService.sendMessage(playerId, message)
      false
    }
  }

  def teleportToSpawn(playerId: UUID): Unit =
    playerService.getPlayerOption(playerId) match {
      case Some(player) => player.team match {
          case Some(team) => entityService.teleportEntity(playerId, team.spawn.get)
          case None       =>
        }
      case _ =>
    }
}
