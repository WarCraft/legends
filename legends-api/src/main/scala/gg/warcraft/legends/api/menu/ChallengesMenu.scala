/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.menu

import gg.warcraft.combat.api.combatclass.CombatClassService
import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.legends.api.player.challenge.{Challenge, ChallengeService}
import gg.warcraft.legends.api.player.data.LegendsPlayerDataService
import gg.warcraft.legends.api.player.level.LevelService
import gg.warcraft.monolith.api.core.data.ServerDataService
import gg.warcraft.monolith.api.entity.attribute.AttributeService
import gg.warcraft.monolith.api.item.ItemType
import gg.warcraft.monolith.api.menu.{Button, Menu, MenuService, MenuSize}
import gg.warcraft.monolith.api.player.currency.CurrencyService
import gg.warcraft.monolith.api.player.{Player, PlayerService}
import gg.warcraft.monolith.api.util.ConsoleColor._

import java.time.temporal.ChronoUnit

object ChallengesMenu {
  private final val infoButton = Button.noop(
    ItemType.BOOK,
    "Challenges",
    List(
      "Repeatable tasks that reward you for helping",
      "your faction increase their influence on Aldmore."
    )
  )

  private def dailyChallengesButton(player: Player)(implicit
      menuService: MenuService,
      attributeService: AttributeService,
      currencyService: CurrencyService,
      levelService: LevelService,
      playerService: PlayerService,
      combatDataService: CombatDataService,
      combatClassService: CombatClassService,
      legendsDataService: LegendsPlayerDataService,
      challengeService: ChallengeService,
      serverDataService: ServerDataService
  ): Button = Button(
    ItemType.GUNPOWDER,
    "Daily Challenges",
    List(
      "Smaller tasks that can be completed every day.",
      s"Time left: $dailyChallengesTimeLeft",
      "",
      "[Click] To view your daily challenges"
    )
  ) { _ => menuService.showMenu(player.id, DailyChallengesMenu(player)) }

  private[menu] def dailyChallengesTimeLeft(implicit
      serverDataService: ServerDataService
  ): String = {
    val serverTime = serverDataService.serverTime
    val until = serverTime.plusDays(1).truncatedTo(ChronoUnit.DAYS)
    val hoursLeft = serverTime.until(until, ChronoUnit.HOURS)
    if (hoursLeft > 0) s"$hoursLeft hours"
    else s"${serverTime.until(until, ChronoUnit.MINUTES)} minutes"
  }

  private def monthlyChallengesButton(player: Player)(implicit
      menuService: MenuService,
      attributeService: AttributeService,
      currencyService: CurrencyService,
      levelService: LevelService,
      playerService: PlayerService,
      combatDataService: CombatDataService,
      combatClassService: CombatClassService,
      legendsDataService: LegendsPlayerDataService,
      challengeService: ChallengeService,
      serverDataService: ServerDataService
  ): Button = Button(
    ItemType.TNT,
    "Monthly Challenges",
    List(
      "Larger tasks that can be completed every war.",
      s"Time left: $monthlyChallengesTimeLeft",
      "",
      "[Click] To view your monthly challenges"
    )
  ) { _ => menuService.showMenu(player.id, MonthlyChallengesMenu(player)) }

  private[menu] def monthlyChallengesTimeLeft(implicit
      serverDataService: ServerDataService
  ): String = {
    val serverTime = serverDataService.serverTime
    val until = serverTime.plusMonths(1).truncatedTo(ChronoUnit.MONTHS)
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
      List(
        challenge.description,
        "",
        s"$color$progress/${challenge.requirement}"
      )
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
      challengeService: ChallengeService,
      serverDataService: ServerDataService
  ): Menu = Menu(
    "Challenges",
    MenuSize.ONE_ROW,
    Map(
      0 -> infoButton,
      2 -> dailyChallengesButton(player),
      3 -> monthlyChallengesButton(player),
      // TODO 4 -> puzzleChallengesButton(player),
      8 -> Button.back(MainMenu.apply)
    )
  )
}

object DailyChallengesMenu {
  private def infoButton(implicit
      serverDataService: ServerDataService
  ) = Button.noop(
    ItemType.BOOK,
    "Daily Challenges",
    List(
      "Smaller tasks that can be completed every day.",
      s"Time left: ${ChallengesMenu.dailyChallengesTimeLeft}"
    )
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
      challengeService: ChallengeService,
      serverDataService: ServerDataService
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
    List(
      "Larger tasks that can be completed every war.",
      s"Time left: ${ChallengesMenu.monthlyChallengesTimeLeft}"
    )
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
      challengeService: ChallengeService,
      serverDataService: ServerDataService
  ): Menu = {
    var challenges: Map[Int, Button] = Map.empty
    challengeService.activeDailies.foreach { challenge =>
      val slot = challenges.size + (if (challenges.size < 7) 2 else 4)
      val button = ChallengesMenu.challengeButton(player, challenge)
      challenges += (slot -> button)
    }
    Menu(
      "Monthly Challenges",
      MenuSize.ONE_ROW,
      Map(
        0 -> infoButton,
        9 -> Button.back(ChallengesMenu.apply)
      ) ++ challenges
    )
  }
}
