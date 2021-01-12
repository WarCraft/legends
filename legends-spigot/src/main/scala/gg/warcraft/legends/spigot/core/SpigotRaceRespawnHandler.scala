/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.core

import gg.warcraft.legends.api.entity.LegendsTeam._
import gg.warcraft.monolith.api.core.event.{Event, PreEvent}
import gg.warcraft.monolith.api.entity.team.Team
import gg.warcraft.monolith.api.math.Vector3f
import gg.warcraft.monolith.api.player.PlayerPreRespawnEvent

class SpigotRaceRespawnHandler extends Event.Handler {
  import SpigotRaceRespawnHandler.getSpawn

  override def reduce[T <: PreEvent](event: T): T = event match {
    case event @ PlayerPreRespawnEvent(player, _) =>
      val spawnLocation = player.team match {
        case None       => event.location
        case Some(team) => event.location.copy(translation = getSpawn(team))
      }
      event.copy(location = spawnLocation).asInstanceOf[T]

    case _ => event
  }
}

object SpigotRaceRespawnHandler {
  // TODO move this into Monolith configuration, maybe all Team settings into there
  private final val dwarfTranslation = Vector3f(-144, 41, -141)
  private final val elfTranslation = Vector3f(115, 58, 199)
  private final val humanTranslation = Vector3f(-69, 67, 208)
  private final val orcTranslation = Vector3f(139, 57, 48)

  def getSpawn(team: Team): Vector3f = team.id match {
    case Dwarves => dwarfTranslation
    case Elves   => elfTranslation
    case Humans  => humanTranslation
    case Orcs    => orcTranslation
    case _       => throw new IllegalArgumentException
  }
}
