/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.core

import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.event.entity.PlayerDeathEvent

class SpigotKeepInventoryHandler extends Listener {
  @EventHandler(ignoreCancelled = true)
  def on(event: PlayerDeathEvent): Unit =
    event.setKeepInventory(false)
}
