/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.ai.npc

import gg.warcraft.legends.api.player.currency.LegendsCurrency
import gg.warcraft.legends.api.util.player._
import gg.warcraft.monolith.api.core.{ColorCode, Message}
import gg.warcraft.monolith.api.item.{ItemService, ItemType}
import gg.warcraft.monolith.api.menu.Button.noop
import gg.warcraft.monolith.api.menu.{Button, Menu, MenuSize}
import gg.warcraft.monolith.api.player.Player
import gg.warcraft.monolith.api.player.currency.CurrencyService
import gg.warcraft.siege.api.plot.item.SiegePlotBlueprintFactory

object BlueprintVendorDialog {
  private final val infoButton: Button = Button.noop(
    ItemType.BOOK,
    "Blueprint Vendor",
    "Blueprints are schematics that allow",
    "you to place siege plots. Once placed",
    "a siege plot requires certain resources",
    "to build the weapon of the blueprint."
  )

  private def blueprintButton(player: Player)(implicit
      currencyService: CurrencyService,
      itemService: ItemService
  ): Button = Button(
    ItemType.PAPER,
    "Trebuchet Blueprint",
    "This blueprint allows you to place a",
    "trebuchet plot. The trebuchet is a long",
    "distance siege weapon that can destroy",
    "enemy fortifications with ease.",
    "",
    s"${ColorCode.AQUA}[Shift Right Click] To buy a trebuchet",
    s"${ColorCode.AQUA}blueprint for 0 favor"
  ) { click =>
    if (click.isShiftRight) {
      if (player.favor < 0) {
        val message =
          Message.server("You need at least 0 favor to purchase this item.")
        player.sendMessage(message)
      } else if (!player.inventory.hasSpace(1)) {
        val message =
          Message.server("Please make sure you have at least 1 free inventory slot.")
        player.sendMessage(message)
      } else {
        currencyService.removeCurrency(LegendsCurrency.Favor, 0, player.id)
        val blueprint = SiegePlotBlueprintFactory("Trebuchet Blueprint")
        itemService.giveTo(player.id, blueprint)
      }
    }
    noop
  }

  def apply(player: Player)(implicit
      currencyService: CurrencyService,
      itemService: ItemService
  ): Menu = Menu(
    "Blueprint Vendor",
    MenuSize.ONE_ROW,
    0 -> infoButton,
    2 -> blueprintButton(player)
  )
}
