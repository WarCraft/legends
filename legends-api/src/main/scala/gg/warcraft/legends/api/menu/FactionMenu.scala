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
import gg.warcraft.legends.api.item.crate.reward.LoginRewardService
import gg.warcraft.legends.api.player.challenge.ChallengeService
import gg.warcraft.legends.api.player.data.LegendsPlayerDataService
import gg.warcraft.legends.api.player.level.LevelService
import gg.warcraft.legends.api.shop.ShopService
import gg.warcraft.legends.api.shop.profile.ShopProfileService
import gg.warcraft.monolith.api.core.ColorCode._
import gg.warcraft.monolith.api.core.command.CommandService
import gg.warcraft.monolith.api.core.data.ServerDataService
import gg.warcraft.monolith.api.entity.attribute.AttributeService
import gg.warcraft.monolith.api.item.{ItemService, ItemType}
import gg.warcraft.monolith.api.menu.Button.noop
import gg.warcraft.monolith.api.menu.{Button, Menu, MenuService, MenuSize}
import gg.warcraft.monolith.api.player.currency.CurrencyService
import gg.warcraft.monolith.api.player.{Player, PlayerService}
import gg.warcraft.monolith.api.util.option._

object FactionMenu {
  private def infoButton(player: Player): Button = {
    val teamName = player.team.map { _.name }.getOrElse(Wayfarers.name)
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

    /*
        List<String> createSwitchView(Team team, boolean canSwitch, String cooldown) {
        List<String> view = new ArrayList<>();
        if (canSwitch) {
            if (cooldown == null) {
                view.add(GREEN + "[SHIFT RIGHT CLICK] To switch to the " + ((LegendsTeam) team).getPlural());
            } else {
                view.add(RED + "You can change teams in " + cooldown);
            }
        } else {
            view.add("This is your current team");
        }
        return view;
    }
     */
  }

  private def humanButton(player: Player)(implicit
      playerService: PlayerService
  ): Button = Button.skull(
    "fishb6nes",
    Humans.name,
    "[Shift Right Click] To fight for the Humans"
  ) { click =>
    if (click.isShiftRight) playerService.setTeam(player.id, Humans); noop
  }

  private def orcButton(player: Player)(implicit
      playerService: PlayerService
  ): Button = Button.skull(
    "fishb6nes",
    Orcs.name,
    "[Shift Right Click] To fight for the Orcs"
  ) { click => if (click.isShiftRight) playerService.setTeam(player.id, Orcs); noop }

  private def dwarfButton(player: Player)(implicit
      playerService: PlayerService
  ): Button = Button.skull(
    "fishb6nes",
    Dwarves.name,
    "[Shift Right Click] To fight for the Dwarves"
  ) { click =>
    if (click.isShiftRight) playerService.setTeam(player.id, Dwarves); noop
  }

  private def elfButton(player: Player)(implicit
      playerService: PlayerService
  ): Button = Button.skull(
    "fishb6nes",
    Elves.name,
    "[Shift Right Click] To fight for the Elves"
  ) { click =>
    if (click.isShiftRight) playerService.setTeam(player.id, Elves); noop
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
      loginRewardService: LoginRewardService,
      serverDataService: ServerDataService,
      challengeService: ChallengeService,
      leaderboardService: LeaderboardService,
      shopService: ShopService,
      shopProfileService: ShopProfileService,
      itemService: ItemService,
      miniPetFactory: MiniPetFactory,
      commandService: CommandService,
      spawnService: SpawnService
  ): Menu = {
    Menu(
      "Faction",
      MenuSize.ONE_ROW,
      0 -> infoButton(player),
      8 -> Button.back(MainMenu.apply),
      // 1st row
      2 -> humanButton(player),
      3 -> orcButton(player),
      4 -> dwarfButton(player),
      5 -> elfButton(player)
    )
  }
}
