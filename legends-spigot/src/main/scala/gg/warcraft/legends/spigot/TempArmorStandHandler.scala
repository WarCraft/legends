/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot

import org.bukkit.entity.ArmorStand
import org.bukkit.event.player.{PlayerInteractAtEntityEvent, PlayerInteractEvent}
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.inventory.EquipmentSlot

class TempArmorStandHandler extends Listener {
  @EventHandler
  def on(event: PlayerInteractAtEntityEvent): Unit =
    event.getRightClicked match {
      case armorstand: ArmorStand =>
        val items = armorstand.getEquipment
        event.getPlayer.getInventory.addItem(items.getArmorContents: _*)
        event.getPlayer.getInventory.addItem(items.getItemInMainHand)
        event.getPlayer.getInventory.addItem(items.getItemInOffHand)
        event.setCancelled(true)
      case _ =>
    }

  @EventHandler
  def on(event: PlayerInteractEvent): Unit = if (
    event.getHand == EquipmentSlot.HAND &&
    event.getItem != null &&
    event.getItem.getType.name().startsWith("MUSIC_DISC")
  ) {
    event.getPlayer.getEquipment.setHelmet(event.getItem)
  }
}
