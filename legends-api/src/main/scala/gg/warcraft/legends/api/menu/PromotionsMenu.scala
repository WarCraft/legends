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
import gg.warcraft.legends.api.item.equipment.EquipmentBuilder
import gg.warcraft.legends.api.player.calendar.LoginCalendarService
import gg.warcraft.legends.api.player.challenge.ChallengeService
import gg.warcraft.legends.api.player.data.LegendsPlayerDataService
import gg.warcraft.legends.api.player.level.LevelService
import gg.warcraft.legends.api.shop.profile.{ShopProfile, ShopProfileService}
import gg.warcraft.legends.api.shop.{Promotion, ShopItem, ShopService}
import gg.warcraft.monolith.api.core.Message
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

object PromotionsMenu {
  private final val infoButton = Button.noop(
    ItemType.BOOK,
    "Promotions",
    "Unique items that commemorate a special",
    "occasion."
  )

  private final val noHelmetMessage =
    Message.server("Please remove your current equipment first.")
  private def promotionButton(
      promotion: Promotion,
      profile: ShopProfile,
      player: Player
  )(implicit
      itemService: ItemService,
      playerService: PlayerService
  ): Button = {
    val unlocked = profile.grandfathered || profile.unlocked.contains(promotion.id)
    val unlockedText = if (unlocked) s"$GREEN[Click] To equip" else s"${RED}Locked"
    Button(
      promotion.icon.get,
      promotion.name,
      promotion.description ++ List("", unlockedText): _*
    ) { _ =>
      if (unlocked) {
        if (player.equipment.get(promotion.equipmentSlot).nonEmpty) {
          val item = new EquipmentBuilder(
            promotion.itemType,
            promotion.rarity,
            promotion.name
          ).withAttributes(promotion.attributes)
            .withFlavorText(promotion.description)
            .build()
          playerService.setEquipment(player.id, promotion.equipmentSlot, item)
          closeMenu
        } else closeMenu(noHelmetMessage)
      } else noop
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
    val promotions = shopService.itemsByType(ShopItem.Promotion)
    val profile = shopProfileService.profile(player.id)
    val rowCount =
      if (promotions.size <= 14) 2
      else Math.ceil(promotions.size / 7f).toInt
    val menuSize = MenuSize.fromRows(rowCount)
    val buttons = promotions.zipWithIndex.map { case (promotion, i) =>
      val slot = i + (1 + (i - 1) / 7) * 2
      slot -> promotionButton(promotion.asInstanceOf[Promotion], profile, player)
    }
    Menu(
      "Promotions",
      menuSize,
      Map(
        0 -> infoButton,
        9 -> Button.back(CollectiblesMenu.apply)
      ) ++ buttons
    )
  }
}
