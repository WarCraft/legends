/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.core

import gg.warcraft.monolith.api.core.Message
import gg.warcraft.monolith.api.core.auth.{AuthService, Principal}
import gg.warcraft.monolith.api.core.command.Command
import gg.warcraft.monolith.api.player.Player
import org.bukkit.Server
import org.bukkit.attribute.Attribute

class PrintAttributesCommand extends Command("print-attributes")
class ClearAttributesCommand extends Command("clear-attributes")

object SpigotAttributeCommand {
  class Handler(implicit
      server: Server,
      authService: AuthService
  ) extends Command.Handler {
    override def handle(
        executor: Principal,
        command: Command,
        args: String*
    ): Command.Result = executor match {
      case player: Player if authService.isMod(player) =>
        val target = server.getPlayer(if (args.isEmpty) player.name else args.head)
        command.name match {
          case "print-attributes" =>
            val attr = target.getAttribute(Attribute.GENERIC_MAX_HEALTH)
            executor.sendMessage(Message.server(s"Base max health: ${attr.getBaseValue}"))
            attr.getModifiers.forEach(it => {
              executor.sendMessage(Message.server(s"modifier: ${it.getName} for ${it.getAmount}"))
            })
          case "clear-attributes" =>
            val attr = target.getAttribute(Attribute.GENERIC_MAX_HEALTH)
            attr.getModifiers.forEach(it => attr.removeModifier(it))
        }
        Command.success
      case _ => Command.invalid
    }
  }
}
