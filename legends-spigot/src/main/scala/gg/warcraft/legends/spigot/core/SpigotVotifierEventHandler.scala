/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.core

import com.vexsoftware.votifier.model.VotifierEvent
import gg.warcraft.legends.api.item.crate.LootCrateService
import gg.warcraft.monolith.api.core.Message
import gg.warcraft.monolith.api.player.PlayerService
import org.bukkit.Server
import org.bukkit.event.{EventHandler, Listener}

class SpigotVotifierEventHandler(implicit
    server: Server,
    playerService: PlayerService,
    lootCrateService: LootCrateService
) extends Listener {
  private final val successMessage = Message.server("You received a vote crate.")

  private val voteCrate = lootCrateService.crates("VOTE_CRATE")

  @EventHandler
  def on(event: VotifierEvent): Unit = {
    val player = server.getPlayer(event.getVote.getUsername)
    if (player != null) {
      playerService.giveItem(player.getUniqueId, voteCrate)
      playerService.sendMessage(player.getUniqueId, successMessage)
    }
  }
}
