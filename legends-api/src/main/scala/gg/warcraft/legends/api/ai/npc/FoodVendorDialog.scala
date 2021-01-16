/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.ai.npc

import gg.warcraft.legends.api.player.currency.LegendsCurrency
import gg.warcraft.legends.api.util.player._
import gg.warcraft.monolith.api.core.{ColorCode, FormatCode, Message}
import gg.warcraft.monolith.api.item.{ItemService, ItemType}
import gg.warcraft.monolith.api.menu.Button.noop
import gg.warcraft.monolith.api.menu.{Button, Menu, MenuSize}
import gg.warcraft.monolith.api.player.Player
import gg.warcraft.monolith.api.player.currency.CurrencyService

object FoodVendorDialog {
  private final val infoButton: Button = Button.noop(
    ItemType.BOOK,
    "Food Vendor"
  )

  private def breadButton(player: Player)(implicit
      currencyService: CurrencyService,
      itemService: ItemService
  ): Button = Button(
    ItemType.BREAD,
    "Bread",
    s"${ColorCode.AQUA}[Shift Right Click] To buy 1 bread",
    s"${ColorCode.AQUA}for 3 gold"
  ) { click =>
    if (click.isShiftRight) {
      if (player.gold < 3) {
        val message =
          Message.server("You need at least 3 gold to purchase this item.")
        player.sendMessage(message)
      } else if (!player.inventory.hasSpace(1)) {
        val message =
          Message.server("Please make sure you have at least 1 free inventory slot.")
        player.sendMessage(message)
      } else {
        currencyService.removeCurrency(LegendsCurrency.Gold, 3, player.id)
        val bread = itemService.create(ItemType.BREAD)
          .withTooltip(s"s${FormatCode.RESET}${ColorCode.GRAY}Consumable")
        itemService.giveTo(player.id, bread)
      }
    }
    noop
  }

  def apply(player: Player)(implicit
      currencyService: CurrencyService,
      itemService: ItemService
  ): Menu = Menu(
    "Food Vendor",
    MenuSize.ONE_ROW,
    0 -> infoButton,
    2 -> breadButton(player)
  )
}
