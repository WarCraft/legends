/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.core

import gg.warcraft.legends.api.entity.LegendsTeam._
import gg.warcraft.monolith.api.core.event.{Event, PreEvent}
import gg.warcraft.monolith.api.math.Vector3f
import gg.warcraft.monolith.api.player.PlayerPreRespawnEvent
import gg.warcraft.monolith.api.util.typing._

class SpigotRaceRespawnHandler extends Event.Handler {
  // TODO move this into Monolith configuration, maybe all Team settings into there
  private final val dwarfTranslation = Vector3f(-144, 41, -141)
  private final val elfTranslation = Vector3f(115, 58, 199)
  private final val humanTranslation = Vector3f(-69, 67, 208)
  private final val orcTranslation = Vector3f(139, 57, 48)

  override def reduce[T <: PreEvent](event: T): T = event match {
    case event @ PlayerPreRespawnEvent(player, _) =>
      val spawnLocation = player.team match {
        case Some(Dwarves) => event.location.copy(translation = dwarfTranslation)
        case Some(Elves)   => event.location.copy(translation = elfTranslation)
        case Some(Humans)  => event.location.copy(translation = humanTranslation)
        case Some(Orcs)    => event.location.copy(translation = orcTranslation)
        case None          => event.location
      }
      event.copy(location = spawnLocation).as[T]

    case _ => event
  }
}
