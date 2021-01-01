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
import gg.warcraft.legends.api.item.crate.reward.LoginRewardService
import gg.warcraft.legends.api.player.challenge.ChallengeService
import gg.warcraft.legends.api.player.data.LegendsPlayerDataService
import gg.warcraft.legends.api.player.level.LevelService
import gg.warcraft.legends.api.shop.profile.{ShopProfile, ShopProfileService}
import gg.warcraft.legends.api.shop.{DecorationPack, ShopItem, ShopService}
import gg.warcraft.legends.api.util.player._
import gg.warcraft.monolith.api.core.ColorCode._
import gg.warcraft.monolith.api.core.Message
import gg.warcraft.monolith.api.core.command.CommandService
import gg.warcraft.monolith.api.core.data.ServerDataService
import gg.warcraft.monolith.api.entity.attribute.AttributeService
import gg.warcraft.monolith.api.item.{ItemService, ItemType}
import gg.warcraft.monolith.api.menu.Button.{closeMenu, noop, updateMenu}
import gg.warcraft.monolith.api.menu._
import gg.warcraft.monolith.api.player.currency.CurrencyService
import gg.warcraft.monolith.api.player.{Player, PlayerService}

object ShopMenu {
  private final val purchaseFailureMessage = Message.server(
    "Something went wrong unlocking that item. Do you have enough Fragments? Please re-open the shop and try again."
  )

  private final val infoButton = Button.noop(
    ItemType.BOOK,
    "WarCraft: Shop",
    "To go even further beyond in supporting the",
    "server the WarCraft: Shop has 12 different",
    "items for sale every day. These items can",
    "be bought with Fragments which are available",
    s"from the web shop at ${YELLOW}shop.warcraft.gg"
  )

  private def itemButton(
      item: ShopItem,
      player: Player,
      profile: ShopProfile
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
      commandService: CommandService
  ): Button = {
    val unlocked = item match {
      case DecorationPack(_, _, _, _, _, _, _, _, decorations) =>
        profile.unlocked.contains(item.id) ||
          decorations.forall { it => profile.unlocked.contains(it.id) }
      case _ =>
        profile.unlocked.contains(item.id)
    }
    lazy val discountedPrice = item.price -
      shopProfileService.calculateDiscount(item, profile)
    lazy val canBuy = player.fragments >= discountedPrice

    var tooltip = item.typ :: "" :: (
      if (unlocked) "You have unlocked this item"
      else if (!canBuy) s"$RED$discountedPrice Fragments"
      else s"$GREEN[Shift Right Click 2x] To unlock for $discountedPrice Fragments"
    ) :: Nil
    if (discountedPrice != item.price) {
      val discount = 100 - discountedPrice / item.price
      tooltip ::= s"$discount% discounted for duplicate decorations"
    }

    val action = (click: Click) => {
      var counter = 0
      if (!unlocked && canBuy && click.isShiftRight) {
        counter += 1
        if (counter == 2) {
          if (shopProfileService.unlock(item.id, player.id)) {
            val unlockMessage =
              Message.server(s"You successfully unlocked ${item.name}!")
            playerService.sendMessage(player.id, unlockMessage)
            updateMenu(ShopMenu.apply)
          } else closeMenu(purchaseFailureMessage)
        } else noop
      } else noop
    }

    val name = s"${item.rarity.color}${item.name}"
    item.skull match {
      case Some(skull) => Button.skull(skull, name, tooltip: _*)(action)
      case None        => Button(item.icon.get, name, tooltip)(action)
    }
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
      serverDataService: ServerDataService,
      challengeService: ChallengeService,
      leaderboardService: LeaderboardService,
      shopService: ShopService,
      shopProfileService: ShopProfileService,
      itemService: ItemService,
      miniPetFactory: MiniPetFactory,
      commandService: CommandService
  ): Menu = {
    val profile = shopProfileService.profile(player.id)
    val buttons = shopService.availableItems.zipWithIndex.map { case (item, i) =>
      val slot = if (i <= 6) 2 + i else 4 + i
      slot -> itemButton(item, player, profile)
    }
    Menu(
      "WarCraft: Shop",
      MenuSize.TWO_ROWS,
      Map(
        0 -> infoButton,
        9 -> Button.back(MainMenu.apply)
      ) ++ buttons
    )
  }
}
