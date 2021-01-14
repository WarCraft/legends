/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.ai.npc

import gg.warcraft.legends.api.LegendsConfig
import gg.warcraft.monolith.api.core.ServerShutdownEvent
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.entity.EntityService
import gg.warcraft.siege.api.objective.{
  ObjectiveCaptureEvent, ObjectiveNeutralizeEvent
}

class NpcPersistenceHandler(config: LegendsConfig)(implicit
    npcService: NpcService,
    entityService: EntityService
) extends Event.Handler {
  override def handle(event: Event): Unit = event match {
    case ObjectiveCaptureEvent(objective, attackers, _) =>
      if (objective.defaultOccupiers.contains(attackers)) {
        config.npcs.find { _.team == objective.defaultOccupiers } match {
          case Some(config) => npcService.spawnNpc(config)
          case None         =>
        }
      }

    case ObjectiveNeutralizeEvent(objective, attackers, _, defenders, _) =>
      if (objective.defaultOccupiers.contains(defenders)) {
        npcService.npcsById
          .find { case (_, npc) => npc.team == objective.defaultOccupiers }
          .foreach { case (id, _) => npcService.despawnNpc(id) }
      }

    case ServerShutdownEvent() =>
      npcService.npcsById.keys.foreach(entityService.removeEntity)

    case _ =>
  }
}
