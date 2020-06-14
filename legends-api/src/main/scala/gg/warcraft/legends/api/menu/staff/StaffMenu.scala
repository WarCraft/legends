/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.menu.staff

import gg.warcraft.legends.api.item.equipment.EquipmentGenerator
import gg.warcraft.monolith.api.item.variant.ChestplateVariant
import gg.warcraft.monolith.api.menu.{Button, Menu, MenuService, MenuSize}
import gg.warcraft.monolith.api.player.{Player, PlayerService}

object StaffMenu {
  def equipmentGeneratorButton(implicit
      menuService: MenuService,
      playerService: PlayerService,
      equipmentGenerator: EquipmentGenerator
  ): Button = Button(
    ChestplateVariant.DIAMOND,
    "Equipment Generator",
    Nil
  ) { click =>
    menuService.showMenu(click.player.id, EquipmentGeneratorMenu.apply)
  }

  def apply(player: Player)(implicit
      menuService: MenuService,
      playerService: PlayerService,
      equipmentGenerator: EquipmentGenerator
  ): Menu = Menu(
    "Staff Menu",
    MenuSize.ONE_ROW,
    Map(
      0 -> equipmentGeneratorButton
    )
  )
}
