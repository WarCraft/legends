/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.menu

import gg.warcraft.combat.api.combatclass.CombatClassService
import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.legends.api.ai.MiniPetFactory
import gg.warcraft.legends.api.core.leaderboard.LeaderboardService
import gg.warcraft.legends.api.core.spawn.SpawnService
import gg.warcraft.legends.api.item.equipment.config.EquipmentConfigRepository
import gg.warcraft.legends.api.player.calendar.LoginCalendarService
import gg.warcraft.legends.api.player.challenge.ChallengeService
import gg.warcraft.legends.api.player.data.LegendsPlayerDataService
import gg.warcraft.legends.api.player.level.LevelService
import gg.warcraft.legends.api.shop.ShopService
import gg.warcraft.legends.api.shop.profile.ShopProfileService
import gg.warcraft.monolith.api.core.auth.AuthService
import gg.warcraft.monolith.api.core.command.{Command, CommandService}
import gg.warcraft.monolith.api.core.data.ServerDataService
import gg.warcraft.monolith.api.entity.attribute.AttributeService
import gg.warcraft.monolith.api.entity.team.TeamService
import gg.warcraft.monolith.api.item.ItemService
import gg.warcraft.monolith.api.menu.MenuService
import gg.warcraft.monolith.api.player.PlayerService
import gg.warcraft.monolith.api.player.currency.CurrencyService

class MenuConfig(implicit
    authService: AuthService,
    menuService: MenuService,
    attributeService: AttributeService,
    currencyService: CurrencyService,
    levelService: LevelService,
    playerService: PlayerService,
    combatDataService: CombatDataService,
    combatClassService: CombatClassService,
    legendsDataService: LegendsPlayerDataService,
    loginRewardService: LoginCalendarService,
    serverDataService: ServerDataService,
    challengeService: ChallengeService,
    leaderboardService: LeaderboardService,
    shopService: ShopService,
    shopProfileService: ShopProfileService,
    itemService: ItemService,
    miniPetFactory: MiniPetFactory,
    commandService: CommandService,
    spawnService: SpawnService,
    equipmentConfigRepository: EquipmentConfigRepository,
    teamService: TeamService
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
