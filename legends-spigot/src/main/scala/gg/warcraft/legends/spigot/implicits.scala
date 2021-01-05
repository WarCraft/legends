/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot

import gg.warcraft.combat.spigot.implicits._
import gg.warcraft.legends.api.LegendsConfig
import gg.warcraft.legends.api.ai.{CombatPetFactory, MiniPetFactory}
import gg.warcraft.legends.api.combat.hunter.marksman.{
  FocusRepository, SneakRepository
}
import gg.warcraft.legends.api.combat.mage.wizard.MassBamboozle
import gg.warcraft.legends.api.core.game.{GameRepository, GameService}
import gg.warcraft.legends.api.core.leaderboard.LeaderboardService
import gg.warcraft.legends.api.core.spawn.SpawnService
import gg.warcraft.legends.api.item.crate.LootCrateService
import gg.warcraft.legends.api.item.crate.reward.LoginRewardRepository
import gg.warcraft.legends.api.item.equipment.EquipmentGenerator
import gg.warcraft.legends.api.item.equipment.config.EquipmentConfigRepository
import gg.warcraft.legends.api.item.equipment.upgrade.EquipmentUpgradeGenerator
import gg.warcraft.legends.api.player.challenge.{
  ChallengeRepository, ChallengeService
}
import gg.warcraft.legends.api.player.data.{
  LegendsPlayerDataRepository, LegendsPlayerDataService
}
import gg.warcraft.legends.api.player.level.LevelService
import gg.warcraft.legends.api.player.statistic.{
  StatisticArchiveRepository, StatisticArchiveService
}
import gg.warcraft.legends.api.shop.profile.{
  ShopProfileService, UnlockedShopItemRepository
}
import gg.warcraft.legends.api.shop.{AvailableShopItemRepository, ShopService}
import gg.warcraft.legends.spigot.ai.{SpigotCombatPetFactory, SpigotMiniPetFactory}
import gg.warcraft.legends.spigot.combat.mage.wizard.MassBamboozleAdapter
import gg.warcraft.monolith.api.core.command.CommandService
import gg.warcraft.monolith.api.core.event.EventService
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.spigot.implicits._
import org.bukkit.Server
import org.bukkit.plugin.Plugin

import java.util.logging.Logger

object implicits {
  private var _config: LegendsConfig = _

  private var _server: Server = _
  private var _plugin: Plugin = _
  private var _logger: Logger = _

  private var _commandService: CommandService = _
  private var _eventService: EventService = _
  private var _taskService: TaskService = _

  private[spigot] def init()(implicit
      server: Server,
      plugin: Plugin,
      logger: Logger,
      commandService: CommandService,
      eventService: EventService,
      taskService: TaskService
  ): Unit = {
    _server = server
    _plugin = plugin
    _logger = logger
    _commandService = commandService
    _eventService = eventService
    _taskService = taskService
  }

  private var _gameRepository: GameRepository = _
  private var _challengeRepository: ChallengeRepository = _
  private var _loginRewardRepository: LoginRewardRepository = _
  private var _legendsPlayerDataRepository: LegendsPlayerDataRepository = _
  private var _statisticArchiveRepository: StatisticArchiveRepository = _
  private var _availableShopItemRepository: AvailableShopItemRepository = _
  private var _unlockedShopItemRepository: UnlockedShopItemRepository = _

  private[spigot] def configure(
      config: LegendsConfig,
      repositories: (
          GameRepository,
          ChallengeRepository,
          LoginRewardRepository,
          LegendsPlayerDataRepository,
          StatisticArchiveRepository,
          AvailableShopItemRepository,
          UnlockedShopItemRepository
      )
  ): Unit = {
    _config = config
    _gameRepository = repositories._1
    _challengeRepository = repositories._2
    _loginRewardRepository = repositories._3
    _legendsPlayerDataRepository = repositories._4
    _statisticArchiveRepository = repositories._5
    _availableShopItemRepository = repositories._6
    _unlockedShopItemRepository = repositories._7
  }

  // Spigot
  private implicit lazy val server: Server = _server
  private implicit lazy val plugin: Plugin = _plugin
  private implicit lazy val logger: Logger = _logger

  // Monolith
  private implicit lazy val commandService: CommandService = _commandService
  private implicit lazy val eventService: EventService = _eventService
  private implicit lazy val taskService: TaskService = _taskService

  // Persistence
  private implicit lazy val gameRepository: GameRepository =
    _gameRepository
  private implicit lazy val challengeRepository: ChallengeRepository =
    _challengeRepository
  private implicit lazy val loginRewardRepository: LoginRewardRepository =
    _loginRewardRepository
  private implicit lazy val legendsPlayerDataRepo: LegendsPlayerDataRepository =
    _legendsPlayerDataRepository
  private implicit lazy val statisticArchiveRepository: StatisticArchiveRepository =
    _statisticArchiveRepository
  private implicit lazy val availableShopItemRepo: AvailableShopItemRepository =
    _availableShopItemRepository
  private implicit lazy val unlockedShopItemRepository: UnlockedShopItemRepository =
    _unlockedShopItemRepository

  // AI
  implicit lazy val combatPetFactory: CombatPetFactory =
    new SpigotCombatPetFactory
  implicit lazy val miniPetFactory: MiniPetFactory =
    new SpigotMiniPetFactory

  // Combat
  implicit lazy val focusRepository: FocusRepository =
    new FocusRepository
  implicit lazy val sneakRepository: SneakRepository =
    new SneakRepository

  implicit lazy val massBamboozleRepository: MassBamboozle.Repository =
    new MassBamboozle.Repository
  implicit lazy val massBamboozleAdapter: MassBamboozle.Adapter =
    new MassBamboozleAdapter

  // Core
  implicit lazy val gameService: GameService =
    new GameService
  implicit lazy val leaderboardService: LeaderboardService =
    new LeaderboardService
  implicit lazy val spawnService: SpawnService =
    new SpawnService

  // Item
  implicit lazy val equipmentConfigRepository: EquipmentConfigRepository =
    new EquipmentConfigRepository
  implicit lazy val equipmentGenerator: EquipmentGenerator =
    new EquipmentGenerator
  implicit lazy val equipmentUpgradeGenerator: EquipmentUpgradeGenerator =
    new EquipmentUpgradeGenerator
  implicit lazy val lootCrateService: LootCrateService =
    new LootCrateService

  // Player
  implicit lazy val legendsPlayerDataService: LegendsPlayerDataService =
    new LegendsPlayerDataService
  implicit lazy val challengeService: ChallengeService =
    new ChallengeService
  implicit lazy val levelService: LevelService =
    new LevelService
  implicit lazy val statisticArchiveService: StatisticArchiveService =
    new StatisticArchiveService

  // Shop
  implicit lazy val shopService: ShopService =
    new ShopService
  implicit lazy val shopProfileService: ShopProfileService =
    new ShopProfileService
}
