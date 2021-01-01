/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.core

import org.bukkit.GameMode
import org.bukkit.entity.EntityType
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.{EventHandler, Listener}

class SpigotArmorStandProtectionHandler extends Listener {
  @EventHandler
  def on(event: PlayerInteractAtEntityEvent): Unit =
    if (
      event.getRightClicked.getType == EntityType.ARMOR_STAND
      && event.getPlayer.getGameMode != GameMode.CREATIVE
    ) {
      event.setCancelled(true)
    }
}
