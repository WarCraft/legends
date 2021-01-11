/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.menu.staff

import gg.warcraft.combat.api.combatclass.CombatClassService
import gg.warcraft.legends.api.item.crate.{LootCrate, LootCrateService}
import gg.warcraft.legends.api.item.equipment.config.EquipmentConfigRepository
import gg.warcraft.monolith.api.entity.team.TeamService
import gg.warcraft.monolith.api.item.ItemService
import gg.warcraft.monolith.api.menu.Button.noop
import gg.warcraft.monolith.api.menu.{Button, Menu, MenuService, MenuSize}
import gg.warcraft.monolith.api.player.{Player, PlayerService}

object LootCrateGeneratorMenu {
  private def lootCrateButton(
      crate: LootCrate.Type
  )(implicit
      playerService: PlayerService
  ): Button = Button(
    crate.variant,
    crate.name,
    s"[Click] to generate a ${crate.name}"
  ) { click =>
    if (click.isLeft) {
      playerService.giveItem(click.player.id, crate)
      noop
    } else noop
  }

  def apply(player: Player)(implicit
      menuService: MenuService,
      playerService: PlayerService,
      itemService: ItemService,
      equipmentConfigRepository: EquipmentConfigRepository,
      combatClassService: CombatClassService,
      teamService: TeamService,
      lootCrateService: LootCrateService
  ): Menu = {
    val buttons = lootCrateService.crates.values.zipWithIndex
      .map { case (crate, i) => i -> lootCrateButton(crate) }
    Menu(
      "Loot Crate Generator",
      MenuSize.ONE_ROW,
      Map(
        8 -> Button.back(StaffMenu.apply)
      ) ++ buttons
    )
  }
}
