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
      // NPCs
      val names = npcService.npcNames
      val ids = npcService.npcsById
      server.getWorlds.asScala
        .flatMap { _.getLivingEntities.asScala }
        .filter { it => names.exists(it.getName.endsWith) }
        .filter { it => !ids.contains(it.getUniqueId) }
        .foreach { it => npcService.despawnNpc(it.getUniqueId) }
      // Pets
      server.getWorlds.asScala
        .flatMap { _.getLivingEntities.asScala }
        .filter { _.getName.endsWith(" Pet") }
        .foreach { it => entityService.removeEntity(it.getUniqueId) }
      Command.success
    case _ => Command.invalid
  }
}
