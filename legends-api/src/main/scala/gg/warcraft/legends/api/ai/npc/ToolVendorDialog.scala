/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.ai.npc

import gg.warcraft.legends.api.player.currency.LegendsCurrency
import gg.warcraft.legends.api.util.player._
import gg.warcraft.monolith.api.core.{ColorCode, Message}
import gg.warcraft.monolith.api.item.variant.{AxeVariant, PickaxeVariant}
import gg.warcraft.monolith.api.item.{ItemService, ItemType, ItemTypeOrVariant}
import gg.warcraft.monolith.api.menu.Button.noop
import gg.warcraft.monolith.api.menu.{Button, Menu, MenuSize}
import gg.warcraft.monolith.api.player.Player
import gg.warcraft.monolith.api.player.currency.CurrencyService

object ToolVendorDialog {
  private final val infoButton: Button = Button.noop(
    ItemType.BOOK,
    "Tool Vendor"
  )

  private def toolButton(typ: ItemTypeOrVariant, player: Player)(implicit
      currencyService: CurrencyService,
      itemService: ItemService
  ): Button = {
    val item = itemService.create(typ)
    Button(
      typ,
      item.name,
      s"${ColorCode.AQUA}[Shift Right Click] To buy a s${item.name}",
      s"${ColorCode.AQUA}for 25 gold"
    ) { click =>
      if (click.isShiftRight) {
        if (player.gold < 25) {
          val message =
            Message.server("You need at least 25 gold to purchase this item.")
          player.sendMessage(message)
        } else if (!player.inventory.hasSpace(1)) {
          val message =
            Message.server(
              "Please make sure you have at least 1 free inventory slot."
            )
          player.sendMessage(message)
        } else {
          currencyService.removeCurrency(LegendsCurrency.Gold, 25, player.id)
          itemService.giveTo(player.id, item)
        }
      }
      noop
    }
  }

  def apply(player: Player)(implicit
      currencyService: CurrencyService,
      itemService: ItemService
  ): Menu = Menu(
    "Tool Vendor",
    MenuSize.ONE_ROW,
    0 -> infoButton,
    2 -> toolButton(AxeVariant.WOOD, player),
    3 -> toolButton(PickaxeVariant.WOOD, player)
  )
}
