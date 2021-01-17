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
import gg.warcraft.legends.api.entity.LegendsTeam._
import gg.warcraft.legends.api.player.calendar.LoginCalendarService
import gg.warcraft.legends.api.player.challenge.ChallengeService
import gg.warcraft.legends.api.player.data.LegendsPlayerDataService
import gg.warcraft.legends.api.player.level.LevelService
import gg.warcraft.legends.api.shop.ShopService
import gg.warcraft.legends.api.shop.profile.ShopProfileService
import gg.warcraft.monolith.api.core.ColorCode._
import gg.warcraft.monolith.api.core.command.CommandService
import gg.warcraft.monolith.api.core.data.ServerDataService
import gg.warcraft.monolith.api.entity.attribute.AttributeService
import gg.warcraft.monolith.api.entity.team.{Team, TeamService}
import gg.warcraft.monolith.api.item.{ItemService, ItemType}
import gg.warcraft.monolith.api.menu.Button.noop
import gg.warcraft.monolith.api.menu.{Button, Menu, MenuService, MenuSize}
import gg.warcraft.monolith.api.player.currency.CurrencyService
import gg.warcraft.monolith.api.player.{Player, PlayerService}
import gg.warcraft.monolith.api.util.option._

object FactionMenu {
  private def infoButton(player: Player)(implicit
      teamService: TeamService
  ): Button = {
    val teamName = player.team.map { _.name }.getOrElse(Wayfarers.instance.name)
    Button.noop(
      ItemType.BOOK,
      "Choosing a faction",
      "You are currently fighting for the",
      s"$YELLOW$teamName",
      "",
      "At the moment you can switch factions freely,",
      "but this will be changed at a later time."
    )
    /*
        builder.addTooltip("You can switch teams freely the first");
        builder.addTooltip("3 days of a war (start of the month).");
        builder.addTooltip("Alternatively, if you really want to");
        builder.addTooltip("switch teams right now you can do so");
        builder.addTooltip("for a few Fragments.");
        builder.addTooltip("");
        if (true) {
            builder.addTooltip(GREEN + "Changing teams is currently free");
        } else {
            builder.addTooltip(ORANGE + "Changing teams at this time costs 800 Fragments");
        }
     */
  }

  private final val firstTimeLoginInfoButton = Button.noop(
    ItemType.BOOK,
    "Choosing a faction",
    "Welcome to WarCraft! Before you can",
    "start forging your legend on Aldmore",
    "you need to align yourself with one",
    "of its four factions."
  )

  private def teamButton(player: Player, team: Team, skull: String)(implicit
      playerService: PlayerService
  ): Button = Button.skull(
    skull,
    team.name,
    s"[Shift Right Click] To fight for the s${team.name}"
  ) { click =>
    if (click.isShiftRight) playerService.setTeam(player.id, team)
    noop
  }

  def apply(
      player: Player
  )(implicit
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
      teamService: TeamService
  ): Menu = {
    Menu(
      "Faction",
      MenuSize.ONE_ROW,
      0 -> infoButton(player),
      8 -> Button.back(MainMenu.apply),
      // 1st row
      2 -> teamButton(player, Dwarves.instance, "fishb6nes"),
      3 -> teamButton(player, Elves.instance, "fishb6nes"),
      4 -> teamButton(player, Humans.instance, "fishb6nes"),
      5 -> teamButton(player, Orcs.instance, "fishb6nes")
    )
  }

  def firstTimeLogin(player: Player)(implicit
      playerService: PlayerService,
      teamService: TeamService
  ): Menu = Menu(
    "Welcome to WarCraft!",
    MenuSize.ONE_ROW,
    0 -> firstTimeLoginInfoButton,
    // 1st row
    2 -> teamButton(player, Dwarves.instance, "fishb6nes"),
    3 -> teamButton(player, Elves.instance, "fishb6nes"),
    4 -> teamButton(player, Humans.instance, "fishb6nes"),
    5 -> teamButton(player, Orcs.instance, "fishb6nes")
  )
}
