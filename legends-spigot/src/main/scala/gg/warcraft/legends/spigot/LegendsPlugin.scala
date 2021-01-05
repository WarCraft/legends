/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot

import gg.warcraft.combat.spigot.implicits._
import gg.warcraft.legends.api.LegendsConfig
import gg.warcraft.legends.api.combat.CombatConfig
import gg.warcraft.legends.api.combat.hunter.{
  ArrowDistributionHandler, ArrowFriendlyFireHandler, ArrowPickUpHandler,
  HunterCombatPetHandler
}
import gg.warcraft.legends.api.combat.mage.wizard.MassBamboozle
import gg.warcraft.legends.api.core.game.{
  GameRepository, PostgresGameRepository, SqliteGameRepository
}
import gg.warcraft.legends.api.core.handler.{
  BlockBreakPlaceHandler, PrintVoteLinksCommand
}
import gg.warcraft.legends.api.core.spawn.ReturnToSpawnCommand
import gg.warcraft.legends.api.entity.attribute.EquipmentAttributeHandler
import gg.warcraft.legends.api.item.crate.reward.{
  LoginRewardRepository, PostgresLoginRewardRepository, SqliteLoginRewardRepository
}
import gg.warcraft.legends.api.item.equipment.config.EquipmentConfig
import gg.warcraft.legends.api.menu.MenuConfig
import gg.warcraft.legends.api.player.challenge.{
  ChallengeRepository, PostgresChallengeRepository, SqliteChallengeRepository
}
import gg.warcraft.legends.api.player.currency.{
  AddCurrencyAdminCommand, CurrencyAdminCommand, RemoveCurrencyAdminCommand,
  RevokeCurrencyAdminCommand
}
import gg.warcraft.legends.api.player.data.{
  LegendsPlayerDataRepository, PostgresLegendsPlayerDataRepository,
  SqliteLegendsPlayerDataRepository
}
import gg.warcraft.legends.api.player.statistic.{
  PostgresStatisticArchiveRepository, SqliteStatisticArchiveRepository,
  StatisticArchiveRepository
}
import gg.warcraft.legends.api.shop._
import gg.warcraft.legends.api.shop.profile.{
  PostgresUnlockedShopItemRepository, SqliteUnlockedShopItemRepository,
  UnlockedShopItemRepository
}
import gg.warcraft.legends.spigot.combat.PersonalScoreboardHandler
import gg.warcraft.legends.spigot.combat.hunter.HunterOnlyBowHandler
import gg.warcraft.legends.spigot.core.{
  SpigotFirstTimeRaceClassMenuHandler, SpigotFurnaceCoalHandler,
  SpigotRaceRespawnHandler
}
import gg.warcraft.monolith.api.core.Codecs.Circe._
import gg.warcraft.monolith.api.core.DatabaseConfig
import gg.warcraft.monolith.api.item.ItemTypeOrVariant
import gg.warcraft.monolith.spigot.SpigotMonolithPlugin
import gg.warcraft.monolith.spigot.implicits._
import io.circe.Decoder
import io.circe.generic.auto._
import org.bukkit.configuration.file.YamlConfiguration

import java.io.File

class LegendsPlugin extends SpigotMonolithPlugin {
  import implicits._

  override def onLoad(): Unit = {
    super.onLoad()
    implicits.init()
  }

  override def onEnable(): Unit = {
    // TODO parse config implicits
    val config = parseConfig[LegendsConfig](getConfig.saveToString())
    implicit val itemTypeVariantDecoder: Decoder[ItemTypeOrVariant] = itemDataDecoder

    // TODO parse equipment config implicits
    val equipConfigPath =
      new File(s"${getDataFolder.getAbsolutePath}${File.separator}equipment.yml")
    val equipConfigFile = YamlConfiguration.loadConfiguration(equipConfigPath)
    val equipConfig = parseConfig[EquipmentConfig](equipConfigFile.saveToString())

    // TODO parse shop config implicits
    val shopConfigPath =
      new File(s"${getDataFolder.getAbsolutePath}${File.separator}shop.yml")
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

    new CombatConfig().writeConfig()
    new MenuConfig().writeConfig()

    enableHandlers()
    enableCommands()
    enableTasks()
  }

  private def configureRepositories(config: DatabaseConfig): (
      GameRepository,
      ChallengeRepository,
      LoginRewardRepository,
      LegendsPlayerDataRepository,
      StatisticArchiveRepository,
      AvailableShopItemRepository,
      UnlockedShopItemRepository
  ) =
    if (config.embedded) {
      val sqliteConfig = parseDatabaseConfig(config, getDataFolder)
      (
        new SqliteGameRepository(sqliteConfig),
        new SqliteChallengeRepository(sqliteConfig),
        new SqliteLoginRewardRepository(sqliteConfig),
        new SqliteLegendsPlayerDataRepository(sqliteConfig),
        new SqliteStatisticArchiveRepository(sqliteConfig),
        new SqliteAvailableShopItemRepository(sqliteConfig),
        new SqliteUnlockedShopItemRepository(sqliteConfig)
      )
    } else {
      val postgresConfig = parseDatabaseConfig(config, getDataFolder)
      (
        new PostgresGameRepository(postgresConfig),
        new PostgresChallengeRepository(postgresConfig),
        new PostgresLoginRewardRepository(postgresConfig),
        new PostgresLegendsPlayerDataRepository(postgresConfig),
        new PostgresStatisticArchiveRepository(postgresConfig),
        new PostgresAvailableShopItemRepository(postgresConfig),
        new PostgresUnlockedShopItemRepository(postgresConfig)
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
  }

  private def enableHandlers(): Unit = {
    val firstTimeRaceClassMenuHandler = new SpigotFirstTimeRaceClassMenuHandler

    eventService.subscribe(new BlockBreakPlaceHandler)
    eventService.subscribe(new EquipmentAttributeHandler)
    eventService.subscribe(new PersonalScoreboardHandler)
    eventService.subscribe(new ArrowDistributionHandler)
    eventService.subscribe(new ArrowFriendlyFireHandler)
    eventService.subscribe(new ArrowPickUpHandler)
    eventService.subscribe(new HunterCombatPetHandler)
    eventService.subscribe(new MassBamboozle.Handler)
    eventService.subscribe(new SpigotRaceRespawnHandler)
    eventService.subscribe(firstTimeRaceClassMenuHandler)

    subscribe(new OnePointEightPvPInitHandler)
    subscribe(new HunterOnlyBowHandler)
    // TODO subscribe(new SpigotKeepInventoryHandler)
    subscribe(new SpigotFurnaceCoalHandler)
    subscribe(firstTimeRaceClassMenuHandler)
    subscribe(new TempArmorStandHandler)
  }

  private def enableTasks(): Unit = {}

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
