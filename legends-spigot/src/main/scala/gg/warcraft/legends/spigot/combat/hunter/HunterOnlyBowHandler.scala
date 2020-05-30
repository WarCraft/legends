/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.combat.hunter

import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.combat.api.util.combatplayer._
import gg.warcraft.legends.api.combat.LegendsCombatClass
import gg.warcraft.monolith.api.core.Message
import gg.warcraft.monolith.api.player.PlayerService
import gg.warcraft.monolith.spigot.player.SpigotPlayer
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.Material

class HunterOnlyBowHandler(implicit
    playerService: PlayerService,
    combatDataService: CombatDataService
) extends Listener {
  private final val bowClasses: List[String] =
    LegendsCombatClass.Hunter.name :: LegendsCombatClass.Marksman.name :: Nil
  private final val message = Message.server(
    "You clumsily attempt to fire the bow, but failed (Bows can only be used by Ranger classes)."
  )

  @EventHandler
  def on(event: EntityShootBowEvent): Unit = event.getEntity match {
    case spigotPlayer: SpigotPlayer =>
      val player = playerService.getPlayer(spigotPlayer.getUniqueId)
      player.combatClass match {
        case Some(combatClass) if bowClasses.contains(combatClass.name) => // allowed
        case _ =>
          playerService.sendMessage(player.id, message)
          spigotPlayer.getInventory.remove(Material.ARROW)
          event.setCancelled(true)
      }
    case _ =>
  }
}
