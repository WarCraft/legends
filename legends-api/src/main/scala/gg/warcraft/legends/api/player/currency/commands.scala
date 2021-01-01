/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.currency

import gg.warcraft.monolith.api.core.Message
import gg.warcraft.monolith.api.core.auth.{AuthService, Principal}
import gg.warcraft.monolith.api.core.command.Command
import gg.warcraft.monolith.api.player.currency.CurrencyService
import gg.warcraft.monolith.api.player.{Player, PlayerService}

import java.util.UUID

class AddCurrencyAdminCommand() extends Command("add-currency")
class RemoveCurrencyAdminCommand() extends Command("remove-currency")
class RevokeCurrencyAdminCommand() extends Command("revoke-currency")

object CurrencyAdminCommand {
  class Handler(implicit
      authService: AuthService,
      currencyService: CurrencyService,
      playerService: PlayerService
  ) extends Command.Handler {
    override def handle(
        executor: Principal,
        command: Command,
        args: String*
    ): Command.Result = executor match {
      case player: Player =>
        if (authService.isAdmin(player)) {
          if (args.length == 3) {
            val amount = args(0).toInt
            val currency = args(1)
            val targetId = UUID.fromString(args(2))
            val message = command.name match {
              case "add-currency" =>
                currencyService.addCurrency(currency, amount, targetId)
                Message.server(s"Added $amount $currency to $targetId")
              case "remove-currency" =>
                currencyService.removeCurrency(currency, amount, targetId)
                Message.server(s"Removed $amount $currency from $targetId")
              case "revoke-currency" =>
                currencyService.revokeCurrency(currency, amount, targetId)
                Message.server(s"Revoked $amount $currency from $targetId")
            }
            playerService.sendMessage(player.id, message)
            Command.success
          } else Command.invalid
        } else Command.invalid
      case _ => Command.playersOnly
    }
  }
}
