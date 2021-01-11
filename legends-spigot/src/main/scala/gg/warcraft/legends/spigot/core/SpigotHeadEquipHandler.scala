/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.core

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.{PlayerInteractEntityEvent, PlayerInteractEvent}
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.inventory.{EquipmentSlot, ItemStack}

class SpigotHeadEquipHandler extends Listener {
  private val air = new ItemStack(Material.AIR)

  private def onInteract(player: Player): Boolean = {
    val item = player.getInventory.getItemInMainHand
    if (
      item != null && item.getLore != null && !item.getLore.isEmpty &&
      item.getLore.get(0).contains("Equipment") &&
      item.getType.name().startsWith("MUSIC_DISC")
    ) {
      if (player.getEquipment.getHelmet == null) {
        player.getEquipment.setItemInMainHand(air)
        player.getEquipment.setHelmet(item)
        true
      } else false
    } else false
  }

  @EventHandler
  def on(event: PlayerInteractEvent): Unit =
    event.getAction match {
      case Action.RIGHT_CLICK_BLOCK | Action.RIGHT_CLICK_AIR =>
        if (event.getHand == EquipmentSlot.HAND)
          if (onInteract(event.getPlayer))
            event.setCancelled(true)
      case _ =>
    }

  @EventHandler
  def on(event: PlayerInteractEntityEvent): Unit =
    if (event.getHand == EquipmentSlot.HAND)
      if (onInteract(event.getPlayer))
        event.setCancelled(true)
}
