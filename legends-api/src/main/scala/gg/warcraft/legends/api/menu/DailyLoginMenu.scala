/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.menu

import gg.warcraft.combat.api.combatclass.CombatClassService
import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.legends.api.item.crate.LootCrate
import gg.warcraft.legends.api.item.crate.reward.{
  LoginRewardService, LoginRewardsClaimed
}
import gg.warcraft.legends.api.player.data.LegendsPlayerDataService
import gg.warcraft.legends.api.player.level.LevelService
import gg.warcraft.monolith.api.block.variant.GlassVariant
import gg.warcraft.monolith.api.core.ColorCode
import gg.warcraft.monolith.api.core.data.ServerDataService
import gg.warcraft.monolith.api.entity.attribute.AttributeService
import gg.warcraft.monolith.api.item.{ItemType, ItemTypeOrVariant}
import gg.warcraft.monolith.api.menu.{Button, Menu, MenuService, MenuSize}
import gg.warcraft.monolith.api.player.currency.CurrencyService
import gg.warcraft.monolith.api.player.{Player, PlayerService}

object DailyLoginMenu {
  private final val infoButton = Button.noop(
    ItemType.BOOK,
    "Daily Login",
    "Every day you get a reward just for",
    "logging in! The more often you log in, the",
    "greater the rewards. And don't worry; missing",
    "a day or two does not reset your progress."
  )

  private def rewardButton(
      player: Player,
      claimed: LoginRewardsClaimed,
      day: Int,
      reward: LootCrate
  )(implicit
      loginRewardService: LoginRewardService,
      serverDataService: ServerDataService
  ): Button = {
    val plural = if (reward.count > 1) "s" else ""
    val icon: ItemTypeOrVariant =
      if (claimed.counter >= day) {
        if (day % 7 == 0) GlassVariant.BLACK else GlassVariant.BROWN
      } else reward.chestVariant
    val canClaim = serverDataService.today.isAfter(claimed.date) &&
      day == (claimed.counter - 1)
    val claimText =
      if (canClaim) s"${ColorCode.GREEN}[Click] To claim today's reward!"
      else s"${ColorCode.GOLD}Come back tomorrow for another reward."
    Button(
      icon,
      s"Day $day",
      s"${reward.count} ${reward.name}$plural",
      "",
      claimText
    ) { _ => if (canClaim) loginRewardService.claimReward(player.id) }
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
      loginRewardService: LoginRewardService,
      serverDataService: ServerDataService
  ): Menu = {
    val claimed = loginRewardService.rewardsClaimed(player.id)
    Menu(
      "Login Rewards",
      MenuSize.FOUR_ROWS,
      Map(
        0 -> infoButton,
        9 -> Button.back(MainMenu.apply)
      ) ++ loginRewardService.rewards.map { case (day, reward) =>
        val slot = day + (1 + (day - 1) / 7) * 2
        slot -> rewardButton(player, claimed, day, reward)
      }
    )
  }
}
