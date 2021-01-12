/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.core

import org.bukkit.Material
import org.bukkit.event.inventory.{FurnaceBurnEvent, InventoryMoveItemEvent}
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.inventory.FurnaceInventory

class SpigotFurnaceCoalHandler extends Listener {
  @EventHandler
  def on(event: FurnaceBurnEvent): Unit =
    event.getFuel.setAmount(event.getFuel.getMaxStackSize)

  @EventHandler
  def on(event: InventoryMoveItemEvent): Unit = {
    val isFurnaceEvent = event.getSource.isInstanceOf[FurnaceInventory] ||
      event.getDestination.isInstanceOf[FurnaceInventory]
    if (isFurnaceEvent && event.getItem.getType == Material.COAL)
      event.setCancelled(true)
  }
}
