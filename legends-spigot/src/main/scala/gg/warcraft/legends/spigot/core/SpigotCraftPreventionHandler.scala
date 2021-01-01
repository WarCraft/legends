/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.core

import org.bukkit.event.enchantment.EnchantItemEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.{EventHandler, Listener}

class SpigotCraftPreventionHandler extends Listener {
  @EventHandler
  def on(event: CraftItemEvent): Unit =
    event.setCancelled(true)

  @EventHandler
  def on(event: EnchantItemEvent): Unit =
    event.setCancelled(true)
}
