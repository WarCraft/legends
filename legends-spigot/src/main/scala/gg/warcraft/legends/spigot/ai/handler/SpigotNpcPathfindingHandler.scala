/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.ai.handler

import com.destroystokyo.paper.event.entity.EntityPathfindEvent
import gg.warcraft.legends.api.ai.npc.NpcService
import org.bukkit.event.{EventHandler, Listener}

class SpigotNpcPathfindingHandler(implicit
    npcService: NpcService
) extends Listener {
  @EventHandler
  def on(event: EntityPathfindEvent): Unit = {
    if (npcService.npcsById.contains(event.getEntity.getUniqueId)) {
      event.setCancelled(true)
    }
  }
}
