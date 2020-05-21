package gg.warcraft.legends.api.menu

import gg.warcraft.combat.api.combatclass.CombatClassService
import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.legends.api.player.data.LegendsPlayerDataService
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
        case "me" | "menu" | "monolith" | "help"  => MainMenu(player)
        case "team" | "race"                      => RaceMenu(player)
        case "class" | "kit" | "kits"             => CombatClassMenu(player)
        case "dec" | "decoration" | "decorations" => DecorationsMenu(player)
        case "pet" | "pets" | "minis" | "mp"      => MiniPetsMenu(player)
      }
      menuService.showMenu(player.id, menu)
      Command.success
    case _ => Command.playersOnly
  }
}
