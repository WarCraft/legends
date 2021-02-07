/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.ai.npc

import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.legends.api.ai.{AiConfig, NpcConfig, NpcFactory}
import gg.warcraft.monolith.api.core.Duration.DurationOps
import gg.warcraft.monolith.api.core.Message
import gg.warcraft.monolith.api.core.command.CommandService
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.entity.EntityService
import gg.warcraft.monolith.api.item.ItemService
import gg.warcraft.monolith.api.menu.MenuService
import gg.warcraft.monolith.api.player.PlayerService
import gg.warcraft.monolith.api.player.currency.CurrencyService
import gg.warcraft.siege.api.objective.ObjectiveService

import java.util.UUID

class NpcService(implicit
    playerService: PlayerService,
    menuService: MenuService,
    entityService: EntityService,
    objectiveService: ObjectiveService,
    npcFactory: NpcFactory,
    currencyService: CurrencyService,
    itemService: ItemService,
    commandService: CommandService,
    taskService: TaskService,
    combatDataService: CombatDataService
) {
  private final val MSG_NPC_UNINTERESTED =
    Message.server("This NPC is not interested in you.")

  private var _npcsById: Map[UUID, Npc] = Map.empty
  def npcsById: Map[UUID, Npc] = _npcsById

  private var _npcNames: Set[String] = Set.empty
  def npcNames: Set[String] = _npcNames

  def readConfig(config: AiConfig): Unit = {
    config.npcs.foreach(spawnNpc)
    taskService.evalLater(6.seconds, commandService.executeCommand("clean-npcs"))
  }

  def spawnNpc(config: NpcConfig): Unit = {
    val npcId = npcFactory.create(config)
    val npc = Npc(npcId, config.typ, config.team, config.name)
    _npcsById += (npcId -> npc)

    _npcNames += config.name
  }

  def despawnNpc(npcId: UUID): Unit = {
    entityService.removeEntity(npcId)
    _npcsById -= npcId
  }

  def openDialog(npcId: UUID, playerId: UUID): Unit = {
    val npc = _npcsById(npcId)
    val player = playerService.getPlayer(playerId)
    if (npc.team == player.team) {
      val menu = npc.typ match {
        case Npc.Type.BLUEPRINT_VENDOR => BlueprintVendorDialog(player)
        case Npc.Type.FOOD_VENDOR      => FoodVendorDialog(player)
        case Npc.Type.TOOL_VENDOR      => ToolVendorDialog(player)
        case Npc.Type.WEAPON_VENDOR    => WeaponVendorDialog(player)
      }
      menuService.showMenu(player.id, menu)
    } else player.sendMessage(MSG_NPC_UNINTERESTED)
  }
}
