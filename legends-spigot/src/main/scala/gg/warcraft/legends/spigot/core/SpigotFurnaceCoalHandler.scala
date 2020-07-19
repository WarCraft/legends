/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.core

import gg.warcraft.monolith.api.util.typing._
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.event.inventory.{FurnaceBurnEvent, InventoryMoveItemEvent}
import org.bukkit.inventory.FurnaceInventory
import org.bukkit.Material

class SpigotFurnaceCoalHandler extends Listener {
  @EventHandler
  def on(event: FurnaceBurnEvent): Unit =
    event.getFuel.setAmount(event.getFuel.getMaxStackSize)

  @EventHandler
  def on(event: InventoryMoveItemEvent): Unit = {
    val isFurnaceEvent = event.getSource.is[FurnaceInventory] ||
      event.getDestination.is[FurnaceInventory]
    if (isFurnaceEvent && event.getItem.getType == Material.COAL)
      event.setCancelled(true)
  }
}
