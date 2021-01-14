/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.ai.npc

import gg.warcraft.legends.api.ai.NpcFactory
import gg.warcraft.legends.api.{LegendsConfig, NpcConfig}
import gg.warcraft.monolith.api.core.Message
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
    itemService: ItemService
) {
  private final val MSG_NPC_UNINTERESTED =
    Message.server("This NPC is not interested in you.")

  private var _npcsById: Map[UUID, Npc] = Map.empty
  def npcsById: Map[UUID, Npc] = _npcsById

  def readConfig(config: LegendsConfig): Unit =
    config.npcs.foreach { npc =>
      objectiveService.objectives.values.find { _.occupiers == npc.team } match {
        case Some(_) => spawnNpc(npc)
        case None    =>
      }
    }

  def spawnNpc(config: NpcConfig): Unit = {
    val npcId = npcFactory.create(config)
    val npc = Npc(npcId, config.typ, config.team, config.name)
    _npcsById += (npcId -> npc)
  }

  def despawnNpc(npcId: UUID): Unit = {
    entityService.removeEntity(npcId)
    _npcsById -= npcId
  }

  def openDialog(npcId: UUID, playerId: UUID): Unit = {
    val npc = _npcsById(npcId)
    val player = playerService.getPlayer(playerId)
    if (npc.team == player.team) {
      npc.typ match {
        case Npc.Type.BLUEPRINT_VENDOR =>
          val menu = BlueprintVendorDialog(player)
          menuService.showMenu(player.id, menu)
        /*
          case CLASS_TRAINER:
                  Menu classTrainerMenu = classTrainerDialogFactory.createClassTrainerDialog(playerId, npc.getName());
                  menuCommandService.showMenu(classTrainerMenu, playerId);
         */
      }
    } else player.sendMessage(MSG_NPC_UNINTERESTED)
  }
}
