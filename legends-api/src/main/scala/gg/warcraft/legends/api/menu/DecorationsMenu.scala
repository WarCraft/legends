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
import gg.warcraft.legends.api.player.challenge.ChallengeService
import gg.warcraft.legends.api.player.data.LegendsPlayerDataService
import gg.warcraft.legends.api.player.level.LevelService
import gg.warcraft.legends.api.shop.profile.ShopProfileService
import gg.warcraft.legends.api.shop.{DecorationPack, ShopItem, ShopService}
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

import scala.util.chaining.scalaUtilChainingOps

object DecorationsMenu {
  private final val infoButton = Button.noop(
    ItemType.BOOK,
    "Decorations",
    "Did you know you can rent a house in your",
    "faction's capital city? Did you know you can",
    "completely decorate it however you want!",
    "",
    "You can unlock decorations in the /shop or",
    "by opening loot crates."
  )

  private var decorationPackButtons: Map[String, Button] = Map.empty
  private def decorationPackButton(pack: DecorationPack)(implicit
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
  ): Button = decorationPackButtons.get(pack.id) match {
    case Some(button) => button
    case None =>
      val button = Button(
        pack.icon.get,
        pack.name,
        pack.description ++
          List("", s"$GREEN[Click] To view your", s"${pack.name} decorations")
      ) { _ => updateMenu(DecorationPackMenu(pack)) }
      button.tap { it => decorationPackButtons += (pack.id -> it) }
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
    val packs = shopService.itemsByType(ShopItem.DecorationPack)
    val rowCount = if (packs.size <= 14) 2 else Math.ceil(packs.size / 7f).toInt
    val menuSize = MenuSize.fromRows(rowCount)
    val buttons = packs.zipWithIndex.map { case (pack, i) =>
      val slot = i + (1 + i / 7) * 2
      slot -> decorationPackButton(pack.asInstanceOf[DecorationPack])
    }
    Menu(
      "Decorations",
      menuSize,
      Map(
        0 -> infoButton,
        9 -> Button.back(CollectiblesMenu.apply)
      ) ++ buttons
    )
  }
}
