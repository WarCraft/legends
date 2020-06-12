/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot

import java.util.logging.Logger

import gg.warcraft.legends.api.combat.hunter.marksman.{
  FocusRepository, SneakRepository
}
import gg.warcraft.legends.api.combat.mage.wizard.MassBamboozle
import gg.warcraft.legends.api.core.types.DatabaseContext
import gg.warcraft.legends.api.item.equipment.config.EquipmentConfigRepository
import gg.warcraft.legends.api.item.equipment.EquipmentGenerator
import gg.warcraft.legends.api.player.data.LegendsPlayerDataService
import gg.warcraft.legends.api.player.level.LevelService
import gg.warcraft.legends.spigot.combat.mage.elementalist.MassBamboozleAdapter
import gg.warcraft.monolith.api.core.event.EventService
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.spigot.implicits._
import org.bukkit.plugin.Plugin
import org.bukkit.Server

import scala.concurrent.ExecutionContext

object implicits {
  private var _server: Server = _
  private var _plugin: Plugin = _
  private var _logger: Logger = _

  private var _database: DatabaseContext = _
  private var _eventService: EventService = _
  private var _taskService: TaskService = _

  private[spigot] def init()(implicit
      server: Server,
      plugin: Plugin,
      logger: Logger,
      database: DatabaseContext,
      eventService: EventService,
      taskService: TaskService
  ): Unit = {
    _server = server
    _plugin = plugin
    _logger = logger
    _database = database
    _eventService = eventService
    _taskService = taskService
  }

  // Spigot
  private implicit lazy val server: Server = _server
  private implicit lazy val plugin: Plugin = _plugin
  private implicit lazy val logger: Logger = _logger

  // Core
  private implicit final val context: ExecutionContext = ExecutionContext.global
  private implicit lazy val database: DatabaseContext = _database
  private implicit lazy val eventService: EventService = _eventService
  private implicit lazy val taskService: TaskService = _taskService

  // Marksman
  implicit lazy val focusRepository: FocusRepository =
    new FocusRepository
  implicit lazy val sneakRepository: SneakRepository =
    new SneakRepository

  // Wizard
  implicit lazy val massBamboozleRepository: MassBamboozle.Repository =
    new MassBamboozle.Repository
  implicit lazy val massBamboozleAdapter: MassBamboozle.Adapter =
    new MassBamboozleAdapter

  // Equipment
  implicit lazy val equipmentConfigRepository: EquipmentConfigRepository =
    new EquipmentConfigRepository
  implicit lazy val equipmentGenerator: EquipmentGenerator =
    new EquipmentGenerator

  // Player
  implicit lazy val legendsPlayerDataService: LegendsPlayerDataService =
    new LegendsPlayerDataService
  implicit lazy val levelService: LevelService =
    new LevelService
}
