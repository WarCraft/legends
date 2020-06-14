/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.menu

import gg.warcraft.combat.api.combatclass.CombatClassService
import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.legends.api.item.equipment.EquipmentGenerator
import gg.warcraft.legends.api.player.data.LegendsPlayerDataService
import gg.warcraft.legends.api.player.level.LevelService
import gg.warcraft.monolith.api.core.auth.AuthService
import gg.warcraft.monolith.api.core.command.{Command, CommandService}
import gg.warcraft.monolith.api.entity.attribute.AttributeService
import gg.warcraft.monolith.api.menu.MenuService
import gg.warcraft.monolith.api.player.PlayerService
import gg.warcraft.monolith.api.player.currency.CurrencyService

class MenuConfig(implicit
    commandService: CommandService,
    authService: AuthService,
    menuService: MenuService,
    playerService: PlayerService,
    attributeService: AttributeService,
    currencyService: CurrencyService,
    levelService: LevelService,
    combatDataService: CombatDataService,
    combatClassService: CombatClassService,
    legendsPlayerDataService: LegendsPlayerDataService,
    equipmentGenerator: EquipmentGenerator
) {
  def writeConfig(): Unit = {
    val handler = new MenuCommandHandler

    val main = Command("Menu", "me" :: "monolith" :: "help" :: Nil)
    commandService.registerCommand(main, handler)

    val team = Command("Race", "team" :: Nil)
    commandService.registerCommand(team, handler)

    val combatClass = Command("Class", "kit" :: "kits" :: Nil)
    commandService.registerCommand(combatClass, handler)

    val decorations = Command("Decorations", "decoration" :: "dec" :: Nil)
    commandService.registerCommand(decorations, handler)

    val miniPets = Command("Minipets", "pets" :: "pet" :: "minis" :: "mp" :: Nil)
    commandService.registerCommand(miniPets, handler)

    val staff = Command("Staff", Nil)
    commandService.registerCommand(staff, handler)
  }
}
