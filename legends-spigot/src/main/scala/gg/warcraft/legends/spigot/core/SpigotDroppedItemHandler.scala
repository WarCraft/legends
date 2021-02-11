/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.core

import gg.warcraft.monolith.api.core.Message
import gg.warcraft.monolith.api.player.PlayerService
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.inventory.ItemStack

class SpigotDroppedItemHandler(implicit
    playerService: PlayerService
) extends Listener {
  private final val allowed: List[String] =
    "Blueprint" :: "Consumable" :: "Equipment" :: "Resource" :: "Tool" :: Nil

  private final val disallowedMessage = Message.server("You can't drop this item.")

  private def canDrop(item: ItemStack): Boolean = {
    val meta = item.getItemMeta
    if (meta != null) {
      val lore = meta.getLore
      if (lore != null && !lore.isEmpty) {
        val itemType = lore.get(0)
        allowed.exists { itemType.contains(_) }
      } else false
    } else false
  }

  @EventHandler
  def on(event: PlayerDropItemEvent): Unit =
    if (!canDrop(event.getItemDrop.getItemStack)) {
      event.setCancelled(true)
      playerService.sendMessage(event.getPlayer.getUniqueId, disallowedMessage)
    }

  @EventHandler
  def on(event: PlayerDeathEvent): Unit = {
    val drops = event.getDrops.iterator()
    while (drops.hasNext) {
      val item = drops.next
      if (!canDrop(item)) {
        drops.remove()
        event.getItemsToKeep.add(item)
      }
    }
  }
}
