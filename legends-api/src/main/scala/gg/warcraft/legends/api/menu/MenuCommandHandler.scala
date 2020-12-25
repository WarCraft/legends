/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.menu

import gg.warcraft.combat.api.combatclass.CombatClassService
import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.legends.api.item.equipment.EquipmentGenerator
import gg.warcraft.legends.api.menu.staff.StaffMenu
import gg.warcraft.legends.api.player.data.LegendsPlayerDataService
import gg.warcraft.legends.api.player.level.LevelService
import gg.warcraft.monolith.api.core.auth.{AuthService, Principal}
import gg.warcraft.monolith.api.core.command.Command
import gg.warcraft.monolith.api.entity.attribute.AttributeService
import gg.warcraft.monolith.api.menu.MenuService
import gg.warcraft.monolith.api.player.currency.CurrencyService
import gg.warcraft.monolith.api.player.{Player, PlayerService}

class MenuCommandHandler(implicit
    menuService: MenuService,
    authService: AuthService,
    playerService: PlayerService,
    attributeService: AttributeService,
    levelService: LevelService,
    currencyService: CurrencyService,
    combatDataService: CombatDataService,
    combatClassService: CombatClassService,
    legendsPlayerDataService: LegendsPlayerDataService,
    equipmentGenerator: EquipmentGenerator
) extends Command.Handler {
  override def handle(
      executor: Principal,
      command: Command,
      args: String*
  ): Command.Result = executor match {
    case player: Player =>
      val menu = command.name match {
        case "Menu"        => Some(MainMenu(player))
        case "Race"        => Some(FactionMenu(player))
        case "Class"       => Some(CombatClassMenu(player))
        case "Decorations" => Some(DecorationsMenu(player))
        case "Minipets"    => Some(MiniPetsMenu(player))
        case "Staff" =>
          if (authService.isStaff(player)) Some(StaffMenu(player))
          else None
      }
      menu.foreach { menuService.showMenu(player.id, _) }
      Command.success
    case _ => Command.playersOnly
  }
}
