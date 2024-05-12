/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.ai.handler

import gg.warcraft.legends.api.ai.npc.NpcService
import gg.warcraft.monolith.api.core.auth.{AuthService, Principal}
import gg.warcraft.monolith.api.core.command.{Command, CommandService}
import gg.warcraft.monolith.api.entity.EntityService
import org.bukkit.Server

import scala.jdk.CollectionConverters.CollectionHasAsScala

class SpigotNpcCleanUpHandler(implicit
    server: Server,
    authService: AuthService,
    commandService: CommandService,
    npcService: NpcService,
    entityService: EntityService
) extends Command.Handler {
  override def handle(
      executor: Principal,
      command: Command,
      args: String*
  ): Command.Result = executor match {
    case it if authService.isAdmin(it) =>
      val entities = server.getWorlds.asScala
        .flatMap { _.getLivingEntities.asScala }
      // NPCs
      entities
        .filter { it => npcService.npcNames.exists(it.getName.endsWith) }
        .filter { it => !npcService.npcsById.contains(it.getUniqueId) }
        .foreach { it => npcService.despawnNpc(it.getUniqueId) }
      // Pets
      entities
        .filter { _.getName.endsWith(" Pet") }
        .foreach { it => entityService.removeEntity(it.getUniqueId) }
      Command.success
    case _ => Command.invalid
  }
}
