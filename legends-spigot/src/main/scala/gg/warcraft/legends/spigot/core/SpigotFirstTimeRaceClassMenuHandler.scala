/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.core

import gg.warcraft.combat.api.combatclass.{
  CombatClassService, PlayerCombatClassChangedEvent
}
import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.combat.api.util.entity._
import gg.warcraft.legends.api.ai.MiniPetFactory
import gg.warcraft.legends.api.core.leaderboard.LeaderboardService
import gg.warcraft.legends.api.item.crate.reward.LoginRewardService
import gg.warcraft.legends.api.menu.{CombatClassMenu, FactionMenu}
import gg.warcraft.legends.api.player.challenge.ChallengeService
import gg.warcraft.legends.api.player.data.LegendsPlayerDataService
import gg.warcraft.legends.api.player.level.LevelService
import gg.warcraft.legends.api.shop.ShopService
import gg.warcraft.legends.api.shop.profile.ShopProfileService
import gg.warcraft.monolith.api.core.Duration._
import gg.warcraft.monolith.api.core.data.ServerDataService
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.entity.attribute.AttributeService
import gg.warcraft.monolith.api.entity.{EntityService, EntityTeamChangedEvent}
import gg.warcraft.monolith.api.item.ItemService
import gg.warcraft.monolith.api.menu.MenuService
import gg.warcraft.monolith.api.player.currency.CurrencyService
import gg.warcraft.monolith.api.player.{Player, PlayerService}
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.{EventHandler, Listener}

import java.util.UUID

class SpigotFirstTimeRaceClassMenuHandler(implicit
    entityService: EntityService,
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
    miniPetFactory: MiniPetFactory
) extends Event.Handler
    with Listener {
  import SpigotRaceRespawnHandler.getSpawn

  private var inTutorial: Set[UUID] = Set.empty

  private def test(player: Player): Unit = {
    if (player.team.isEmpty) {
      menuService.showMenu(player.id, FactionMenu(player))
    } else if (player.combatClass.isEmpty) {
      menuService.showMenu(player.id, CombatClassMenu(player))
    } else {
      inTutorial -= player.id
      entityService.freezeEntity(player.id, 0.ticks)
      val spawnLocation =
        player.location.copy(translation = getSpawn(player.team.get))
      entityService.teleportEntity(player.id, spawnLocation)
    }
  }

  @EventHandler
  def on(event: PlayerJoinEvent): Unit = {
    val player = playerService.getPlayer(event.getPlayer.getUniqueId)
    if (player.team.isEmpty || player.combatClass.isEmpty) {
      inTutorial += event.getPlayer.getUniqueId
      entityService.freezeEntity(player.id, Integer.MAX_VALUE.ticks)
      test(player)
    }
  }

  @EventHandler
  def on(event: InventoryCloseEvent): Unit =
    if (inTutorial.contains(event.getPlayer.getUniqueId))
      test(playerService.getPlayer(event.getPlayer.getUniqueId))

  override def handle(event: Event): Unit = event match {
    case EntityTeamChangedEvent(player: Player, _, _) =>
      if (inTutorial.contains(player.id)) test(player)
    case PlayerCombatClassChangedEvent(player, _, _) =>
      if (inTutorial.contains(player.id)) test(player)
    case _ =>
  }
}
