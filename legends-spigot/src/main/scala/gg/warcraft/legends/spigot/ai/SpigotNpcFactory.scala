/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.ai

import gg.warcraft.legends.api.NpcConfig
import gg.warcraft.legends.api.ai.NpcFactory
import gg.warcraft.monolith.api.entity.team.TeamService
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.player.PlayerService
import org.bukkit.Server
import org.bukkit.entity.Villager

import java.util.UUID

class SpigotNpcFactory(implicit
    server: Server,
    playerService: PlayerService,
    entityService: EntityService,
    teamService: TeamService
) extends NpcFactory {
  override def create(config: NpcConfig): UUID = {
    import config._
    val npcId = entityService.spawnEntity(Entity.Type.VILLAGER, location, team)

    val spigotNPC = server.getEntity(npcId).asInstanceOf[Villager]
    spigotNPC.setRemoveWhenFarAway(false)
    spigotNPC.setInvulnerable(true)
    spigotNPC.setAgeLock(true)

    val nameColor = team.map { _.chatColor.toString }.getOrElse("")
    spigotNPC.setCustomName(s"$nameColor$name")

    npcId
  }
}
