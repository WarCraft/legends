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
import gg.warcraft.legends.api.shop.{MiniPet, ShopItem, ShopService}
import gg.warcraft.monolith.api.core.command.CommandService
import gg.warcraft.monolith.api.core.data.ServerDataService
import gg.warcraft.monolith.api.entity.attribute.AttributeService
import gg.warcraft.monolith.api.entity.team.TeamService
import gg.warcraft.monolith.api.item.{ItemService, ItemType}
import gg.warcraft.monolith.api.menu.Button.{closeMenu, noop}
import gg.warcraft.monolith.api.menu.{Button, Menu, MenuService, MenuSize}
import gg.warcraft.monolith.api.player.currency.CurrencyService
import gg.warcraft.monolith.api.player.{Player, PlayerService}
import gg.warcraft.monolith.api.util.ConsoleColor.{GREEN, RED}

object MiniPetsMenu {
  private val infoButton = Button.noop(
    ItemType.BOOK,
    "Mini Pets",
    "Mini pets are cute little creatures that will",
    "follow you around and cheer you on wherever",
    "you go."
  )

  private def miniPetButton(pet: MiniPet, profile: ShopProfile)(implicit
      miniPetFactory: MiniPetFactory
  ): Button = {
    val unlocked = profile.grandfathered ||
      profile.unlocked.contains(pet.id)
    val unlockedText =
      if (unlocked) s"$GREEN[Click] To summon your ${pet.name}"
      else s"${RED}Locked"
    Button.skull(
      pet.skull.get,
      pet.name,
      pet.description ++ List("", unlockedText): _*
    ) { _ =>
      if (unlocked) {
        miniPetFactory.create(pet.entityType, profile.playerId)
        closeMenu
      } else noop
    }
  }

  private def dismissButton(player: Player)(implicit
      miniPetFactory: MiniPetFactory
  ): Button = Button(
    ItemType.BARRIER,
    "Dismiss",
    "[Click] To dismiss your mini pet"
  ) { _ =>
    miniPetFactory.remove(player.id)
    closeMenu
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
      commandService: CommandService,
      spawnService: SpawnService,
      teamService: TeamService
  ): Menu = {
    val pets = shopService.itemsByType(ShopItem.MiniPet)
    val profile = shopProfileService.profile(player.id)
    val rowCount = if (pets.size <= 14) 2 else Math.ceil(pets.size / 7f).toInt
    val menuSize = MenuSize.fromRows(rowCount)
    val buttons = pets.zipWithIndex.map { case (pet, i) =>
      val slot = i + (1 + (i - 1) / 7) * 2
      slot -> miniPetButton(pet.asInstanceOf[MiniPet], profile)
    }
    Menu(
      "Mini Pets",
      menuSize,
      Map(
        0 -> infoButton,
        9 -> Button.back(CollectiblesMenu.apply)
      ) ++ buttons + (16 -> dismissButton(player))
    )
  }
}
