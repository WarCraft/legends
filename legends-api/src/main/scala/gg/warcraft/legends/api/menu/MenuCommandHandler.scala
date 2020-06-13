/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.menu

import gg.warcraft.combat.api.combatclass.CombatClassService
import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.legends.api.player.data.LegendsPlayerDataService
import gg.warcraft.legends.api.player.level.LevelService
import gg.warcraft.monolith.api.core.auth.Principal
import gg.warcraft.monolith.api.core.command.Command
import gg.warcraft.monolith.api.entity.attribute.AttributeService
import gg.warcraft.monolith.api.menu.MenuService
import gg.warcraft.monolith.api.player.{Player, PlayerService}
import gg.warcraft.monolith.api.player.currency.CurrencyService

class MenuCommandHandler(implicit
    menuService: MenuService,
    playerService: PlayerService,
    attributeService: AttributeService,
    levelService: LevelService,
    currencyService: CurrencyService,
    combatDataService: CombatDataService,
    combatClassService: CombatClassService,
    legendsPlayerDataService: LegendsPlayerDataService
) extends Command.Handler {
  override def handle(
      executor: Principal,
      command: Command,
      args: String*
  ): Command.Result = executor match {
    case player: Player =>
      val menu = command.name match {
        case "Menu"        => MainMenu(player)
        case "Race"        => RaceMenu(player)
        case "Class"       => CombatClassMenu(player)
        case "Decorations" => DecorationsMenu(player)
        case "Minipets"    => MiniPetsMenu(player)
      }
      menuService.showMenu(player.id, menu)
      Command.success
    case _ => Command.playersOnly
  }
}
