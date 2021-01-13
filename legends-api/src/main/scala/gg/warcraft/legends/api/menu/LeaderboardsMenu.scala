/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.menu

import gg.warcraft.combat.api.combatclass.CombatClassService
import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.legends.api.ai.MiniPetFactory
import gg.warcraft.legends.api.core.leaderboard.{Leaderboard, LeaderboardService}
import gg.warcraft.legends.api.core.spawn.SpawnService
import gg.warcraft.legends.api.player.calendar.LoginCalendarService
import gg.warcraft.legends.api.player.challenge.ChallengeService
import gg.warcraft.legends.api.player.data.LegendsPlayerDataService
import gg.warcraft.legends.api.player.level.LevelService
import gg.warcraft.legends.api.shop.ShopService
import gg.warcraft.legends.api.shop.profile.ShopProfileService
import gg.warcraft.monolith.api.block.variant.BannerVariant
import gg.warcraft.monolith.api.core.ColorCode._
import gg.warcraft.monolith.api.core.command.CommandService
import gg.warcraft.monolith.api.core.data.ServerDataService
import gg.warcraft.monolith.api.entity.attribute.AttributeService
import gg.warcraft.monolith.api.entity.team.TeamService
import gg.warcraft.monolith.api.item.{ItemService, ItemType}
import gg.warcraft.monolith.api.menu.Button.{noop, updateButton, updateMenu}
import gg.warcraft.monolith.api.menu.{Button, Menu, MenuService, MenuSize}
import gg.warcraft.monolith.api.player.currency.CurrencyService
import gg.warcraft.monolith.api.player.{Player, PlayerService}

object LeaderboardsMenu {
  private final val infoButton = Button.noop(
    ItemType.BOOK,
    "Leaderboards",
    "Your actions on the server do not go unnoticed.",
    "The Titans keep track of all the legends forged",
    "on Aldmore and reward those that are the greatest!"
  )

  private def dailyLeaderboardsButton(implicit
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
  ) = Button(
    BannerVariant.YELLOW,
    "Daily Leaderboards",
    "[Click] To view the daily leaderboards"
  ) { _ => updateMenu(DailyLeaderboardsMenu.apply) }

  private def monthlyLeaderboardsButton(implicit
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
  ) = Button(
    BannerVariant.ORANGE,
    "Monthly Leaderboards",
    "[Click] To view the monthly leaderboards"
  ) { _ => updateMenu(MonthlyLeaderboardsMenu.apply) }

  private def alltimeLeaderboardsButton(implicit
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
  ) = Button(
    BannerVariant.RED,
    "All-time Leaderboards",
    "[Click] To view the all-time leaderboards"
  ) { _ => updateMenu(AlltimeLeaderboardsMenu.apply) }

  private[menu] def leaderboardButton(
      leaderboard: Leaderboard,
      player: Player,
      page: Int
  ): Button = {
    val icon = leaderboard match {
      case _ if leaderboard.isDaily   => BannerVariant.YELLOW
      case _ if leaderboard.isMonthly => BannerVariant.ORANGE
      case _                          => BannerVariant.RED
    }
    val pages = leaderboard.players.size / 10
    val playerRank = leaderboard.players.get(player.id)
      .map { it => f"${it + 1}%3s" }
      .getOrElse("???")
    val score = player.statistics.get(leaderboard.statistic)
    val cursor = page * 10
    val rankings = leaderboard.rankings
      .slice(cursor, cursor + 10)
      .zipWithIndex
      .map { case ((player, score), i) =>
        val rank = f"${cursor + i + 1}%3s"
        s"$YELLOW$rank. ${player.name} $score"
      }
    Button(
      icon,
      leaderboard.name,
      List(
        s"$YELLOW$playerRank. ${player.name} $score",
        ""
      ) ++ rankings ++ List(
        "",
        s"[Click] Previous  (Page ${page + 1}/${pages + 1})  Next [Right Click]"
      )
    ) { click =>
      if (click.isLeft && page > 0) {
        updateButton(leaderboardButton(leaderboard, player, page + 1))
      } else if (click.isRight && page < pages) {
        updateButton(leaderboardButton(leaderboard, player, page - 1))
      } else noop
    }
  }

  private[menu] def leaderboardMenu(
      name: String,
      filter: Leaderboard => Boolean,
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
    val buttons = leaderboardService.leaderboards.values
      .filter(filter)
      .zipWithIndex
      .map { case (leaderboard, index) =>
        val slot = index + (1 + index / 7) * 2
        slot -> LeaderboardsMenu.leaderboardButton(leaderboard, player, 0)
      }
    Menu(
      name,
      MenuSize.TWO_ROWS,
      Map(
        0 -> infoButton,
        9 -> Button.back(MainMenu.apply)
      ) ++ buttons
    )
  }

  def apply(player: Player)(implicit
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
  ): Menu = Menu(
    "Leaderboards",
    MenuSize.ONE_ROW,
    0 -> infoButton,
    2 -> dailyLeaderboardsButton,
    3 -> monthlyLeaderboardsButton,
    4 -> alltimeLeaderboardsButton,
    8 -> Button.back(MainMenu.apply)
  )
}

object DailyLeaderboardsMenu {
  private final val infoButton = Button.noop(
    ItemType.BOOK,
    "Daily Leaderboards",
    "Your actions on the server do not go unnoticed.",
    "The Titans keep track of all the legends forged",
    "on Aldmore and reward those that are the greatest!"
  )

  def apply(player: Player)(implicit
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
  ): Menu = LeaderboardsMenu.leaderboardMenu(
    "Daily Leaderboard",
    { _.isDaily },
    player
  )
}

object MonthlyLeaderboardsMenu {
  private final val infoButton = Button.noop(
    ItemType.BOOK,
    "MonthlyLeaderboards",
    "Your actions on the server do not go unnoticed.",
    "The Titans keep track of all the legends forged",
    "on Aldmore and reward those that are the greatest!"
  )

  def apply(player: Player)(implicit
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
  ): Menu = LeaderboardsMenu.leaderboardMenu(
    "Monthly Leaderboard",
    { _.isMonthly },
    player
  )
}

object AlltimeLeaderboardsMenu {
  private final val infoButton = Button.noop(
    ItemType.BOOK,
    "All-time Leaderboards",
    "Your actions on the server do not go unnoticed.",
    "The Titans keep track of all the legends forged",
    "on Aldmore and reward those that are the greatest!"
  )

  def apply(player: Player)(implicit
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
  ): Menu = LeaderboardsMenu.leaderboardMenu(
    "All-time Leaderboard",
    { it => !it.isDaily && !it.isMonthly },
    player
  )
}
