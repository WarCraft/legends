/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot

import gg.warcraft.combat.spigot.implicits._
import gg.warcraft.legends.api.LegendsConfig
import gg.warcraft.legends.api.ai.npc.{NpcInteractHandler, NpcPersistenceHandler}
import gg.warcraft.legends.api.combat.CombatConfig
import gg.warcraft.legends.api.combat.hunter.marksman.{
  FocusCancelHandler, SneakCancelHandler
}
import gg.warcraft.legends.api.combat.hunter.{
  ArrowDistributionHandler, ArrowPickUpHandler, HunterCombatPetHandler
}
import gg.warcraft.legends.api.combat.mage.wizard.MassBamboozle
import gg.warcraft.legends.api.combat.passive.PassiveAssignmentHandler
import gg.warcraft.legends.api.core.data.{
  LegendsServerDataRepository, PostgresLegendsServerDataRepository,
  SqliteLegendsServerDataRepository
}
import gg.warcraft.legends.api.core.game._
import gg.warcraft.legends.api.core.game.handler.{
  DailySkirmishHandler, GameRewardHandler, GameTickHandler
}
import gg.warcraft.legends.api.core.handler._
import gg.warcraft.legends.api.core.leaderboard.handler.{
  LeaderboardRewardHandler, LeaderboardUpdateHandler
}
import gg.warcraft.legends.api.core.spawn.ReturnToSpawnCommand
import gg.warcraft.legends.api.entity.attribute.EquipmentAttributeHandler
import gg.warcraft.legends.api.item.crate.LootCrateUseHandler
import gg.warcraft.legends.api.item.equipment.config.EquipmentConfig
import gg.warcraft.legends.api.item.equipment.upgrade.EquipmentUpgradeUseHandler
import gg.warcraft.legends.api.menu.MenuConfig
import gg.warcraft.legends.api.player.calendar.{
  LoginCalendarHandler, LoginCalendarRepository, PostgresLoginCalendarRepository,
  SqliteLoginCalendarRepository
}
import gg.warcraft.legends.api.player.challenge.handler.{
  ChallengeCompletionHandler, ChallengeRotateHandler
}
import gg.warcraft.legends.api.player.challenge.{
  ChallengeRepository, PostgresChallengeRepository, SqliteChallengeRepository
}
import gg.warcraft.legends.api.player.currency._
import gg.warcraft.legends.api.player.data.{
  LegendsPlayerDataCacheHandler, LegendsPlayerDataRepository,
  PostgresLegendsPlayerDataRepository, SqliteLegendsPlayerDataRepository
}
import gg.warcraft.legends.api.player.handler.PlayerInspectHandler
import gg.warcraft.legends.api.player.level.handler.{
  PlayerLevelUpHandler, PlayerLeveledUpHandler
}
import gg.warcraft.legends.api.player.statistic.handler._
import gg.warcraft.legends.api.shop._
import gg.warcraft.legends.api.shop.handler.{
  MiniPetSpawnHandler, ShopItemRotateHandler
}
import gg.warcraft.legends.api.shop.profile.{
  PostgresUnlockedShopItemRepository, ShopProfileCacheHandler,
  SqliteUnlockedShopItemRepository, UnlockedShopItemRepository
}
import gg.warcraft.legends.spigot.ai.handler.SpigotNpcPathfindingHandler
import gg.warcraft.legends.spigot.combat.PersonalScoreboardHandler
import gg.warcraft.legends.spigot.combat.hunter.HunterOnlyBowHandler
import gg.warcraft.legends.spigot.core._
import gg.warcraft.monolith.api.block.variant.ChestVariant
import gg.warcraft.monolith.api.core.DatabaseConfig
import gg.warcraft.monolith.api.core.Duration.DurationOps
import gg.warcraft.monolith.api.entity.Equipment
import gg.warcraft.monolith.spigot.SpigotMonolithPlugin
import gg.warcraft.monolith.spigot.implicits._
import io.circe.generic.auto._
import org.bukkit.configuration.file.YamlConfiguration

import java.io.File

class LegendsPlugin extends SpigotMonolithPlugin {
  import gg.warcraft.chat.spigot.implicits._
  import gg.warcraft.siege.spigot.implicits._
  import implicits._

  override def onLoad(): Unit = {
    super.onLoad()
    implicits.init()
  }

  override def onEnable(): Unit = {
    import gg.warcraft.combat.api.util.codecs.combat._
    import gg.warcraft.legends.api.util.codecs.legends.{challengeDecoder => _, _}
    import gg.warcraft.monolith.api.util.codecs.circe._
    import gg.warcraft.monolith.api.util.codecs.monolith._

    // Config
    val config = {
      implicit val chestDecoder: String => ChestVariant = ChestVariant.valueOf
      parseConfig[LegendsConfig](getConfig.saveToString())
    }

    // Config: equipment
    val equipConfigPath =
      new File(s"${getDataFolder.getAbsolutePath}${File.separator}equipment.yml")
    if (!equipConfigPath.exists) saveResource("equipment.yml", false)
    val equipConfigFile = YamlConfiguration.loadConfiguration(equipConfigPath)
    val equipConfig = parseConfig[EquipmentConfig](equipConfigFile.saveToString())

    // Config: shop
    implicit val equipSlotDecoder: String => Equipment.Slot = Equipment.withName

    val shopConfigPath =
      new File(s"${getDataFolder.getAbsolutePath}${File.separator}shop.yml")
    if (!equipConfigPath.exists) saveResource("shop.yml", false)
    val shopConfigFile = YamlConfiguration.loadConfiguration(shopConfigPath)
    val shopConfig = parseConfig[ShopConfig](shopConfigFile.saveToString())

    upgradeDatabase(config.database, getDataFolder, getClassLoader)
    val repositories = configureRepositories(config.database)
    implicits.configure(config, repositories)

    // Shop
    shopService.readConfig(shopConfig)

    // Equipment
    equipmentConfigRepository.readConfig(equipConfig)

    // Legends
    gameService.readConfig(config)
    leaderboardService.readConfig(config)
    lootCrateService.readConfig(config)
    levelService.readConfig(config)
    loginRewardService.readConfig(config)
    challengeService.readConfig(config)
    npcService.readConfig(config)

    new CombatConfig().writeConfig()
    new MenuConfig().writeConfig()

    val firstTimeRaceClassMenuHandler = new SpigotFirstTimeRaceClassMenuHandler()

    enableHandlers(config, firstTimeRaceClassMenuHandler)
    enableCommands()
    enableTasks(config, firstTimeRaceClassMenuHandler)
  }

  private def configureRepositories(config: DatabaseConfig): (
      GameRepository,
      DailyPointsArchiveRepository,
      ChallengeRepository,
      LoginCalendarRepository,
      LegendsPlayerDataRepository,
      AvailableShopItemRepository,
      UnlockedShopItemRepository,
      LegendsServerDataRepository
  ) =
    if (config.embedded) {
      val sqliteConfig = parseDatabaseConfig(config, getDataFolder)
      (
        new SqliteGameRepository(sqliteConfig),
        new SqliteDailyPointsArchiveRepository(sqliteConfig),
        new SqliteChallengeRepository(sqliteConfig),
        new SqliteLoginCalendarRepository(sqliteConfig),
        new SqliteLegendsPlayerDataRepository(sqliteConfig),
        new SqliteAvailableShopItemRepository(sqliteConfig),
        new SqliteUnlockedShopItemRepository(sqliteConfig),
        new SqliteLegendsServerDataRepository(sqliteConfig)
      )
    } else {
      val postgresConfig = parseDatabaseConfig(config, getDataFolder)
      (
        new PostgresGameRepository(postgresConfig),
        new PostgresDailyPointsArchiveRepository(postgresConfig),
        new PostgresChallengeRepository(postgresConfig),
        new PostgresLoginCalendarRepository(postgresConfig),
        new PostgresLegendsPlayerDataRepository(postgresConfig),
        new PostgresAvailableShopItemRepository(postgresConfig),
        new PostgresUnlockedShopItemRepository(postgresConfig),
        new PostgresLegendsServerDataRepository(postgresConfig)
      )
    }

  private def enableCommands(): Unit = {
    // Currency
    val currencyCommandHandler = new CurrencyAdminCommand.Handler
    commandService.registerCommand(
      new AddCurrencyAdminCommand,
      currencyCommandHandler
    )
    commandService.registerCommand(
      new RemoveCurrencyAdminCommand,
      currencyCommandHandler
    )
    commandService.registerCommand(
      new RevokeCurrencyAdminCommand,
      currencyCommandHandler
    )

    // Game
    val gameCommandHandler = new GameCommand.Handler
    commandService.registerCommand(
      new StartWarCommand,
      gameCommandHandler
    )
    commandService.registerCommand(
      new EndWarCommand,
      gameCommandHandler
    )

    commandService.registerCommand(
      new StartSkirmishCommand,
      gameCommandHandler
    )
    commandService.registerCommand(
      new EndSkirmishCommand,
      gameCommandHandler
    )

    commandService.registerCommand(
      new PauseGameCommand,
      gameCommandHandler
    )
    commandService.registerCommand(
      new ResumeGameCommand,
      gameCommandHandler
    )

    // Player
    commandService.registerCommand(
      new PrintVoteLinksCommand,
      new PrintVoteLinksCommand.Handler
    )
    commandService.registerCommand(
      new ReturnToSpawnCommand,
      new ReturnToSpawnCommand.Handler
    )

    // Shop
    commandService.registerCommand(
      new CycleShopItemsCommand,
      new CycleShopItemsCommand.Handler
    )

    val spigotAttributeCommandHandler = new SpigotAttributeCommand.Handler
    commandService.registerCommand(
      new PrintAttributesCommand,
      spigotAttributeCommandHandler
    )
    commandService.registerCommand(
      new ClearAttributesCommand,
      spigotAttributeCommandHandler
    )
  }

  private def enableHandlers(
      config: LegendsConfig,
      firstTimeRaceClassMenuHandler: SpigotFirstTimeRaceClassMenuHandler
  ): Unit = {
    // Core
    eventService.subscribe(firstTimeRaceClassMenuHandler)
    eventService.subscribe(new BlockBreakPlaceHandler)
    eventService.subscribe(new LaunchRewardsDistributionHandler)
    eventService.subscribe(new SpigotRaceRespawnHandler)
    eventService.subscribe(new TutorialPvpHandler)

    // Combat
    eventService.subscribe(new CombatLoggingHandler)
    eventService.subscribe(new PassiveAssignmentHandler)
    eventService.subscribe(new PersonalScoreboardHandler)
    // Hunter
    eventService.subscribe(new ArrowDistributionHandler)
    eventService.subscribe(new ArrowPickUpHandler)
    eventService.subscribe(new HunterCombatPetHandler)
    eventService.subscribe(new FocusCancelHandler)
    eventService.subscribe(new SneakCancelHandler)
    // Mage
    eventService.subscribe(new MassBamboozle.Handler)

    // Challenge
    eventService.subscribe(new ChallengeCompletionHandler(config))
    eventService.subscribe(new ChallengeRotateHandler)

    // Game
    eventService.subscribe(new CaptureAndKillRewardHandler(config))
    eventService.subscribe(new GameTickHandler)
    eventService.subscribe(new GameRewardHandler(config))
    eventService.subscribe(new LeaderboardUpdateHandler)
    eventService.subscribe(new LeaderboardRewardHandler(config))

    // Item
    eventService.subscribe(new EquipmentAttributeHandler)
    eventService.subscribe(new EquipmentUpgradeUseHandler)

    // NPC
    eventService.subscribe(new NpcInteractHandler)
    eventService.subscribe(new NpcPersistenceHandler(config))

    // Player
    eventService.subscribe(new PlayerInspectHandler)
    eventService.subscribe(new PlayerLevelUpHandler)
    eventService.subscribe(new PlayerLeveledUpHandler)
    eventService.subscribe(new LegendsCurrencyEventMapper)
    eventService.subscribe(new LegendsPlayerDataCacheHandler)

    // Shop
    eventService.subscribe(new ShopItemRotateHandler)
    eventService.subscribe(new ShopProfileCacheHandler)
    eventService.subscribe(new LoginCalendarHandler)
    eventService.subscribe(new LootCrateUseHandler)
    eventService.subscribe(new MiniPetSpawnHandler)

    // Statistic
    eventService.subscribe(new CrateStatisticHandler)
    eventService.subscribe(new CurrencyStatisticHandler)
    eventService.subscribe(new DamageStatisticHandler)
    eventService.subscribe(new KillStatisticHandler)
    eventService.subscribe(new ObjectiveStatisticHandler)
    eventService.subscribe(new ResourceStatisticHandler)

    // Spigot
    subscribe(new OnePointEightPvPInitHandler)
    subscribe(new HunterOnlyBowHandler)
    subscribe(new SpigotArmorStandProtectionHandler)
    subscribe(new SpigotCraftPreventionHandler)
    subscribe(new SpigotDroppedItemHandler)
    subscribe(new SpigotFurnaceCoalHandler)
    // TODO subscribe(new SpigotKeepInventoryHandler)
    subscribe(new SpigotHeadEquipHandler)
    subscribe(new SpigotNpcPathfindingHandler)

    if (_server.getPluginManager.isPluginEnabled("Votifier")) {
      subscribe(new SpigotVotifierEventHandler)
    } else {
      _logger.warning(
        "Could not register votifier event handler, dependency is missing!"
      )
    }
  }

  private def enableTasks(
      config: LegendsConfig,
      firstTimeRaceClassMenuHandler: SpigotFirstTimeRaceClassMenuHandler
  ): Unit = {
    taskService.runTask(6.seconds, () => firstTimeRaceClassMenuHandler.run())

    val scoreboardHandler = new SpigotScoreboardHandler(config)
    taskService.runTask(1.seconds, () => scoreboardHandler.run())

    val skirmishHandler = new DailySkirmishHandler
    taskService.runTask(1.seconds, () => skirmishHandler.run())

    val playtimeStatisticHandler = new PlaytimeStatisticHandler
    taskService.runTask(10.seconds, () => playtimeStatisticHandler.run())

    val gameTickHandler = new GameTickHandler
    taskService.runTask(1.seconds, () => gameTickHandler.run())
  }
}

/* TODO remove

    void initializeTutorial() {
        // Commands
        CommandCommandService commandCommandService = injector.getInstance(CommandCommandService.class);
        EventService eventService = injector.getInstance(EventService.class);

        TutorialStepHandler tutorialStepHandler = injector.getInstance(TutorialStepHandler.class);
        commandCommandService.createCommand("next", Lists.newArrayList("end"), tutorialStepHandler);
        eventService.subscribe(tutorialStepHandler);

        TutorialCommandHandler tutorialCommandHandler = injector.getInstance(TutorialCommandHandler.class);
        commandCommandService.createCommand("setracetutorial", Collections.emptyList(), tutorialCommandHandler);
        commandCommandService.createCommand("setclasstutorial", Collections.emptyList(), tutorialCommandHandler);
        commandCommandService.createCommand("skiptutorial", Lists.newArrayList("skip"), tutorialCommandHandler);

        // Siege weapon
        SiegeWeaponFactory siegeWeaponFactory = injector.getInstance(SiegeWeaponFactory.class);
        SiegeWeaponRepository siegeWeaponRepository = injector.getInstance(SiegeWeaponRepository.class);
        WorldService worldService = injector.getInstance(WorldService.class);

        Block loadBlock = worldService.getBlock(World.THE_END, 294, 164, -193);
        Block turnLeftBlock = worldService.getBlock(World.THE_END, 293, 165, -198);
        Block turnRightBlock = worldService.getBlock(World.THE_END, 295, 165, -198);
        Block chargeBlock = worldService.getBlock(World.THE_END, 294, 165, -197);
        Block fireBlock = worldService.getBlock(World.THE_END, 294, 166, -197);
        Block projectileOriginBlock = worldService.getBlock(World.THE_END, 294, 176, -206);

        BoundingBlockBoxFactory boundingBlockBoxFactory = injector.getInstance(BoundingBlockBoxFactory.class);
        BoundingBlockBox tutorialTrebuchetBoundingBox = boundingBlockBoxFactory.createBoundingBlockBox(
                World.THE_END, new Vector3i(291, 164, -206), new Vector3i(296, 176, -193));

        SiegeWeapon tutorialTrebuchet = siegeWeaponFactory.createTrebuchet(Direction.NORTH,
                tutorialTrebuchetBoundingBox.stream().collect(Collectors.toSet()),
                new ArrayList<>(), loadBlock, turnLeftBlock, turnRightBlock, chargeBlock, fireBlock,
                projectileOriginBlock);
        SiegeWeapon persistentTutorialTrebuchet = new PersistentSiegeWeaponDecorator(tutorialTrebuchet);
        siegeWeaponRepository.save(persistentTutorialTrebuchet);
    }
 */
