/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.core

import gg.warcraft.monolith.api.core.Message
import gg.warcraft.monolith.api.player.PlayerService
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.{EventHandler, Listener}

class SpigotDroppedItemHandler(implicit
    playerService: PlayerService
) extends Listener {
  private final val allowed: List[String] =
    "Blueprint" :: "Consumable" :: "Equipment" :: "Resource" :: "Tool" :: Nil

  private final val disallowedMessage = Message.server("You can't drop this item.")

  @EventHandler
  def on(event: PlayerDropItemEvent): Unit = {
    val meta = event.getItemDrop.getItemStack.getItemMeta
    if (meta != null) {
      val lore = meta.getLore
      if (lore != null && !lore.isEmpty) {
        val itemType = lore.get(0)
        if (allowed.forall { !itemType.contains(_) }) {
          event.setCancelled(true)
          playerService.sendMessage(event.getPlayer.getUniqueId, disallowedMessage)
        }
      }
    }
  }
}
