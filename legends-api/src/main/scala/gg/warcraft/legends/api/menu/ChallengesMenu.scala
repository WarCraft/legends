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
import gg.warcraft.legends.api.player.calendar.LoginCalendarService
import gg.warcraft.legends.api.player.challenge.{Challenge, ChallengeService}
import gg.warcraft.legends.api.player.data.LegendsPlayerDataService
import gg.warcraft.legends.api.player.level.LevelService
import gg.warcraft.legends.api.shop.ShopService
import gg.warcraft.legends.api.shop.profile.ShopProfileService
import gg.warcraft.monolith.api.core.ColorCode._
import gg.warcraft.monolith.api.core.command.CommandService
import gg.warcraft.monolith.api.core.data.ServerDataService
import gg.warcraft.monolith.api.entity.attribute.AttributeService
import gg.warcraft.monolith.api.entity.team.TeamService
import gg.warcraft.monolith.api.item.{ItemService, ItemType}
import gg.warcraft.monolith.api.menu.Button.updateMenu
import gg.warcraft.monolith.api.menu.{Button, Menu, MenuService, MenuSize}
import gg.warcraft.monolith.api.player.currency.CurrencyService
import gg.warcraft.monolith.api.player.{Player, PlayerService}

import java.time.temporal.ChronoUnit

object ChallengesMenu {
  private final val infoButton = Button.noop(
    ItemType.BOOK,
    "Challenges",
    "Repeatable tasks that reward",
    "you for helping your faction's",
    "efforts on Aldmore."
  )

  private def dailyChallengesButton(implicit
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
  ): Button = Button(
    ItemType.GUNPOWDER,
    "Daily Challenges",
    "Smaller tasks that can be completed every day.",
    s"Time left: $dailyChallengesTimeLeft",
    "",
    "[Click] To view your daily challenges"
  ) { _ => updateMenu(DailyChallengesMenu.apply) }

  private[menu] def dailyChallengesTimeLeft(implicit
      serverDataService: ServerDataService
  ): String = {
    val serverTime = serverDataService.serverTime
    val until = serverTime.plusDays(1).truncatedTo(ChronoUnit.DAYS)
    val hoursLeft = serverTime.until(until, ChronoUnit.HOURS)
    if (hoursLeft > 0) s"$hoursLeft hours"
    else s"${serverTime.until(until, ChronoUnit.MINUTES)} minutes"
  }

  private def monthlyChallengesButton(implicit
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
  ): Button = Button(
    ItemType.TNT,
    "Monthly Challenges",
    "Larger tasks that can be completed every war.",
    s"Time left: $monthlyChallengesTimeLeft",
    "",
    "[Click] To view your monthly challenges"
  ) { _ => updateMenu(MonthlyChallengesMenu.apply) }

  private[menu] def monthlyChallengesTimeLeft(implicit
      serverDataService: ServerDataService
  ): String = {
    val serverTime = serverDataService.serverTime
    val until = serverTime.plusMonths(1)
      .truncatedTo(ChronoUnit.DAYS)
      .withDayOfMonth(1)
    val daysLeft = serverTime.until(until, ChronoUnit.DAYS)
    if (daysLeft > 0) s"$daysLeft days"
    else dailyChallengesTimeLeft
  }

  private[menu] def challengeButton(player: Player, challenge: Challenge): Button = {
    val playerStatistic = player.statistics.get(challenge.statisticId)
    val progress = Math.min(challenge.requirement, playerStatistic)
    val color = if (progress == challenge.requirement) GREEN else YELLOW
    Button.noop(
      if (challenge.isDaily) ItemType.GUNPOWDER else ItemType.TNT,
      challenge.name,
      challenge.description,
      "",
      s"$color$progress/${challenge.requirement}"
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
    "Challenges",
    MenuSize.ONE_ROW,
    0 -> infoButton,
    2 -> dailyChallengesButton,
    3 -> monthlyChallengesButton,
    8 -> Button.back(MainMenu.apply)
  )
}

object DailyChallengesMenu {
  private def infoButton(implicit
      serverDataService: ServerDataService
  ) = Button.noop(
    ItemType.BOOK,
    "Daily Challenges",
    "Smaller tasks that can be completed every day.",
    s"Time left: ${ChallengesMenu.dailyChallengesTimeLeft}"
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
  ): Menu = {
    var challenges: Map[Int, Button] = Map.empty
    challengeService.activeDailies.foreach { challenge =>
      val button = ChallengesMenu.challengeButton(player, challenge)
      challenges += (challenges.size + 2 -> button)
    }
    Menu(
      "Daily Challenges",
      MenuSize.ONE_ROW,
      Map(
        0 -> infoButton,
        8 -> Button.back(ChallengesMenu.apply)
      ) ++ challenges
    )
  }
}

object MonthlyChallengesMenu {
  private def infoButton(implicit
      serverDataService: ServerDataService
  ) = Button.noop(
    ItemType.BOOK,
    "Monthly Challenges",
    "Larger tasks that can be completed every war.",
    s"Time left: ${ChallengesMenu.monthlyChallengesTimeLeft}"
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
  ): Menu = {
    var challenges: Map[Int, Button] = Map.empty
    challengeService.activeDailies.foreach { challenge =>
      val slot = challenges.size + (if (challenges.size < 7) 2 else 4)
      val button = ChallengesMenu.challengeButton(player, challenge)
      challenges += (slot -> button)
    }
    Menu(
      "Monthly Challenges",
      MenuSize.TWO_ROWS,
      Map(
        0 -> infoButton,
        9 -> Button.back(ChallengesMenu.apply)
      ) ++ challenges
    )
  }
}
