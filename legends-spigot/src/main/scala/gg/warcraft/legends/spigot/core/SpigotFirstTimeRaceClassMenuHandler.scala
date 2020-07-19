/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.core

import java.util.UUID

import gg.warcraft.combat.api.combatclass.{
  CombatClassService, PlayerCombatClassChangedEvent
}
import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.combat.api.util.combatplayer._
import gg.warcraft.legends.api.menu.{CombatClassMenu, RaceMenu}
import gg.warcraft.legends.api.player.data.LegendsPlayerDataService
import gg.warcraft.legends.api.player.level.LevelService
import gg.warcraft.monolith.api.core.Duration._
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.entity.{EntityService, EntityTeamChangedEvent}
import gg.warcraft.monolith.api.entity.attribute.AttributeService
import gg.warcraft.monolith.api.menu.MenuService
import gg.warcraft.monolith.api.player.{Player, PlayerService}
import gg.warcraft.monolith.api.player.currency.CurrencyService
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerJoinEvent

class SpigotFirstTimeRaceClassMenuHandler(implicit
    entityService: EntityService,
    playerService: PlayerService,
    combatDataService: CombatDataService,
    menuService: MenuService,
    combatClassService: CombatClassService,
    attributeService: AttributeService,
    currencyService: CurrencyService,
    levelService: LevelService,
    legendsDataService: LegendsPlayerDataService
) extends Event.Handler
    with Listener {
  import SpigotRaceRespawnHandler.getSpawn

  private var inTutorial: Set[UUID] = Set.empty

  private def test(player: Player): Unit = {
    if (player.team.isEmpty) {
      menuService.showMenu(player.id, RaceMenu(player))
    } else if (player.combatClass.isEmpty) {
      menuService.showMenu(player.id, CombatClassMenu(player))
    } else {
      inTutorial -= player.id
      entityService.freezeEntity(player.id, 0.ticks)
      val spawnLocation = player.location.copy(translation = getSpawn(player.team.get))
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
