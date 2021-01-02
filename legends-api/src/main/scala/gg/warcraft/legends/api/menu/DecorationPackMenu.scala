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
import gg.warcraft.legends.api.item.crate.reward.LoginRewardService
import gg.warcraft.legends.api.player.challenge.ChallengeService
import gg.warcraft.legends.api.player.data.LegendsPlayerDataService
import gg.warcraft.legends.api.player.level.LevelService
import gg.warcraft.legends.api.shop.profile.{ShopProfile, ShopProfileService}
import gg.warcraft.legends.api.shop.{Decoration, DecorationPack, ShopService}
import gg.warcraft.monolith.api.core.command.CommandService
import gg.warcraft.monolith.api.core.data.ServerDataService
import gg.warcraft.monolith.api.entity.attribute.AttributeService
import gg.warcraft.monolith.api.item.{ItemService, ItemType}
import gg.warcraft.monolith.api.menu.Button.noop
import gg.warcraft.monolith.api.menu.{Button, Menu, MenuService, MenuSize}
import gg.warcraft.monolith.api.player.currency.CurrencyService
import gg.warcraft.monolith.api.player.{Player, PlayerService}
import gg.warcraft.monolith.api.util.ConsoleColor.{GREEN, RED}

import scala.util.chaining.scalaUtilChainingOps

object DecorationPackMenu {
  private var infoButtons: Map[String, Button] = Map.empty
  private def infoButton(pack: DecorationPack): Button =
    infoButtons.get(pack.id) match {
      case Some(button) => button
      case None =>
        val button = Button.noop(
          ItemType.BOOK,
          pack.name,
          pack.tooltip: _*
        )
        button.tap { it => infoButtons += (pack.id -> it) }
    }

  private def decorationButton(
      decoration: Decoration,
      profile: ShopProfile
  )(implicit
      itemService: ItemService
  ): Button = {
    val unlocked = profile.grandfathered ||
      profile.unlocked.contains(decoration.pack) ||
      profile.unlocked.contains(decoration.id)
    val unlockedText =
      if (unlocked) s"$GREEN[Click] To take out a stack of ${decoration.name}"
      else s"${RED}Locked"
    Button(
      decoration.icon.get,
      decoration.name,
      decoration.tooltip ++ List("", unlockedText)
    ) { _ =>
      if (unlocked) itemService.giveTo(profile.playerId, decoration.stack)
      noop
    }
  }

  def apply(pack: DecorationPack)(player: Player)(implicit
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
    val rowCount =
      if (pack.decorations.size <= 14) 2
      else Math.ceil(pack.decorations.size / 7f).toInt
    val menuSize = MenuSize.fromRows(rowCount)
    val shopProfile = shopProfileService.profile(player.id)
    val buttons = pack.decorations.zipWithIndex.map { case (decoration, i) =>
      val slot = i + (1 + (i - 1) / 7) * 2
      slot -> decorationButton(decoration, shopProfile)
    }
    Menu(
      pack.name,
      menuSize,
      Map(
        0 -> infoButton(pack),
        9 -> Button.back(DecorationsMenu.apply)
      ) ++ buttons
    )
  }
}
