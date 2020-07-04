/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot

import java.io.File

import gg.warcraft.combat.spigot.implicits._
import gg.warcraft.legends.api.combat.CombatConfig
import gg.warcraft.legends.api.combat.hunter.{ ArrowDistributionHandler, ArrowFriendlyFireHandler, ArrowPickUpHandler }
import gg.warcraft.legends.api.combat.mage.wizard.MassBamboozle
import gg.warcraft.legends.api.core.types.DatabaseContext
import gg.warcraft.legends.api.entity.TeamConfig
import gg.warcraft.legends.api.item.equipment.config.EquipmentConfig
import gg.warcraft.legends.api.menu.MenuConfig
import gg.warcraft.legends.api.player.level.Level
import gg.warcraft.legends.api.LegendsConfig
import gg.warcraft.legends.api.core.handler.BlockBreakPlaceHandler
import gg.warcraft.legends.api.entity.attribute.EquipmentAttributeHandler
import gg.warcraft.legends.api.util.circe._
import gg.warcraft.legends.spigot.combat.hunter.HunterOnlyBowHandler
import gg.warcraft.legends.spigot.combat.PersonalScoreboardHandler
import gg.warcraft.monolith.api.core.Codecs.Circe._
import gg.warcraft.monolith.api.core.ColorCode
import gg.warcraft.monolith.api.item.ItemTypeOrVariant
import gg.warcraft.monolith.spigot.SpigotMonolithPlugin
import gg.warcraft.monolith.spigot.implicits._
import io.circe.Decoder
import io.circe.generic.auto._
import io.getquill.{ SnakeCase, SqliteDialect }
import org.bukkit.configuration.file.YamlConfiguration

class LegendsPlugin extends SpigotMonolithPlugin {
  import implicits._

  override def onLoad(): Unit = {
    super.onLoad()

    implicit val databaseContext: DatabaseContext =
      initDatabase(SqliteDialect, SnakeCase, getDataFolder)
    upgradeDatabase(getDataFolder, getClassLoader)

    implicits.init()
  }

  override def onEnable(): Unit = {
    //  TODO val config = parseConfig[LegendsConfig](getConfig.saveToString())
    val config = LegendsConfig(
      Map.empty,
      Level(1, ColorCode.WHITE, 0, Nil) :: Nil,
      Map.empty,
      Map.empty,
      0,
      0,
      0,
      Nil,
      Nil,
      Nil
    )

    implicit val itemTypeVariantDecoder: Decoder[ItemTypeOrVariant] = itemDataDecoder

    val equipmentConfigFile =
      new File(s"${getDataFolder.getAbsolutePath}${File.separator}equipment.yml")
    val equipmentConfig: EquipmentConfig = parseConfig[EquipmentConfig](
      YamlConfiguration.loadConfiguration(equipmentConfigFile).saveToString
    )

    levelService.readConfig(config)
    equipmentConfigRepository.readConfig(equipmentConfig)

    new CombatConfig().writeConfig()
    new TeamConfig().writeConfig()
    new MenuConfig().writeConfig()

    enableHandlers()
    enableTasks()
  }

  private def enableHandlers(): Unit = {
    eventService.subscribe(new BlockBreakPlaceHandler)
    eventService.subscribe(new EquipmentAttributeHandler)
    eventService.subscribe(new PersonalScoreboardHandler)
    eventService.subscribe(new ArrowDistributionHandler)
    eventService.subscribe(new ArrowFriendlyFireHandler)
    eventService.subscribe(new ArrowPickUpHandler)
    eventService.subscribe(new MassBamboozle.Handler)

    subscribe(new OnePointEightPvPInitHandler)
    subscribe(new HunterOnlyBowHandler)
  }

  private def enableTasks(): Unit = {}
}

/*
public class LegendsPlugin extends JavaPlugin {
    private Injector injector;
    private EventService eventService;
    private TaskService taskService;
    private TimeUtils timeUtils;

    void loadServerConfiguration() {
        MonolithPluginUtils pluginUtils = injector.getInstance(MonolithPluginUtils.class);
        LegendsMapperModule legendsMapperModule = new LegendsMapperModule();

        ServerConfiguration serverConfiguration = pluginUtils.loadConfiguration("REMOTE", "legends/server.yml", "",
                ServerConfiguration.class, legendsMapperModule);

        SpawnRepository spawnRepository = injector.getInstance(SpawnRepository.class);
        serverConfiguration.getSpawnLocations().forEach((team, location) ->
                spawnRepository.save(team, location.toLocation()));

        RankCommandService rankCommandService = injector.getInstance(RankCommandService.class);
        rankCommandService.initializeRanks(LegendsTeam.HUMAN, serverConfiguration.getRankFavorTable(),
                serverConfiguration.getRankPrefixes(), serverConfiguration.getHumanRankSuffixes());
        rankCommandService.initializeRanks(LegendsTeam.ORC, serverConfiguration.getRankFavorTable(),
                serverConfiguration.getRankPrefixes(), serverConfiguration.getOrcRankSuffixes());

        RankRepository rankRepository = injector.getInstance(RankRepository.class);
        rankRepository.save(serverConfiguration.getRankUpRewards());

        GameCommandService gameCommandService = injector.getInstance(GameCommandService.class);
        gameCommandService.readConfiguration(serverConfiguration);

        LootCrateCommandService lootCrateCommandService = injector.getInstance(LootCrateCommandService.class);
        lootCrateCommandService.readConfiguration(serverConfiguration);

        ServerRepository serverRepository = injector.getInstance(ServerRepository.class);
        serverRepository.read(serverConfiguration);
    }

    void loadNpcsConfiguration() {
        MonolithPluginUtils pluginUtils = injector.getInstance(MonolithPluginUtils.class);
        LegendsMapperModule legendsMapperModule = new LegendsMapperModule();

        NpcsConfiguration npcsConfiguration = pluginUtils.loadConfiguration("REMOTE", "legends/npcs.yml", "",
                NpcsConfiguration.class, legendsMapperModule);
        NpcCommandService npcCommandService = injector.getInstance(NpcCommandService.class);
        npcsConfiguration.getNpcs().forEach(npcCommandService::readConfiguration);
    }

    void loadShopConfiguration() {
        MonolithPluginUtils pluginUtils = injector.getInstance(MonolithPluginUtils.class);
        LegendsMapperModule legendsMapperModule = new LegendsMapperModule();

        ShopConfiguration shopConfiguration = pluginUtils.loadConfiguration("REMOTE", "legends/shop.yml", "",
                ShopConfiguration.class, legendsMapperModule);
        ShopCommandService shopCommandService = injector.getInstance(ShopCommandService.class);
        shopCommandService.readConfiguration(shopConfiguration);
    }

    void loadEquipmentConfiguration() {
        MonolithPluginUtils pluginUtils = injector.getInstance(MonolithPluginUtils.class);
        LegendsMapperModule legendsMapperModule = new LegendsMapperModule();

        EquipmentConfiguration equipmentConfiguration = pluginUtils.loadConfiguration("REMOTE", "legends/equipment.yml",
                "", EquipmentConfiguration.class, legendsMapperModule);
        EquipmentConfigurationRepository equipmentConfigurationRepository =
                injector.getInstance(EquipmentConfigurationRepository.class);
        equipmentConfigurationRepository.save(equipmentConfiguration);
    }

    void initializeLegendsTeams() {
        TeamCommandService teamCommandService = injector.getInstance(TeamCommandService.class);
        Arrays.stream(LegendsTeam.values()).forEach(teamCommandService::registerTeam);
    }

    void initializeLegendsHandlers(boolean isProduction) {
        CommandCommandService commandCommandService = injector.getInstance(CommandCommandService.class);

        UniversalBlockBreakPlaceHandler universalBlockBreakPlaceHandler =
                injector.getInstance(UniversalBlockBreakPlaceHandler.class);
        eventService.subscribe(universalBlockBreakPlaceHandler);

        PlayerSpawnHandler playerSpawnHandler = injector.getInstance(PlayerSpawnHandler.class);
        eventService.subscribe(playerSpawnHandler);

        PlayerCurrencyEventHandler playerCurrencyEventHandler = injector.getInstance(PlayerCurrencyEventHandler.class);
        eventService.subscribe(playerCurrencyEventHandler);

        VoteHandler voteHandler = injector.getInstance(VoteHandler.class);
        getServer().getPluginManager().registerEvents(voteHandler, this);

        UniversalDroppedItemHandler universalDroppedItemHandler = injector.getInstance(UniversalDroppedItemHandler.class);
        getServer().getPluginManager().registerEvents(universalDroppedItemHandler, this);

        // AI
        NpcInteractHandler npcInteractHandler = injector.getInstance(NpcInteractHandler.class);
        eventService.subscribe(npcInteractHandler);

        NpcPersistenceHandler npcPersistenceHandler = injector.getInstance(NpcPersistenceHandler.class);
        eventService.subscribe(npcPersistenceHandler);

        TargetDummyDamageHandler targetDummyDamageHandler = injector.getInstance(TargetDummyDamageHandler.class);
        eventService.subscribe(targetDummyDamageHandler);

        // Combat
        EquipmentAttributesHandler equipmentAttributesHandler = injector.getInstance(EquipmentAttributesHandler.class);
        eventService.subscribe(equipmentAttributesHandler);

        PassiveAssignmentHandler passiveAssignmentHandler = injector.getInstance(PassiveAssignmentHandler.class);
        eventService.subscribe(passiveAssignmentHandler);

        ArrowDistributionHandler arrowDistributionHandler = injector.getInstance(ArrowDistributionHandler.class);
        eventService.subscribe(arrowDistributionHandler);

        ArrowPickupHandler arrowPickupHandler = injector.getInstance(ArrowPickupHandler.class);
        eventService.subscribe(arrowPickupHandler);

        FocusHandler focusHandler = injector.getInstance(FocusHandler.class);
        eventService.subscribe(focusHandler);

        WhoDamagedWhoHandler whoDamagedWhoHandler = injector.getInstance(WhoDamagedWhoHandler.class);
        eventService.subscribe(whoDamagedWhoHandler);

        FlameTotemDamageLimitationHandler flameTotemDamageLimitationHandler =
                injector.getInstance(FlameTotemDamageLimitationHandler.class);
        eventService.subscribe(flameTotemDamageLimitationHandler);

        CaptureAndKillRewardHandler captureAndKillRewardHandler = injector.getInstance(CaptureAndKillRewardHandler.class);
        eventService.subscribe(captureAndKillRewardHandler);

        ArrowFriendlyFireHandler arrowFriendlyFireHandler = injector.getInstance(ArrowFriendlyFireHandler.class);
        eventService.subscribe(arrowFriendlyFireHandler);

        TutorialPvpHandler tutorialPvpHandler = injector.getInstance(TutorialPvpHandler.class);
        eventService.subscribe(tutorialPvpHandler);

        // Shop
        ShopProfileInitializationHandler shopProfileInitializationHandler =
                injector.getInstance(ShopProfileInitializationHandler.class);
        eventService.subscribe(shopProfileInitializationHandler);

        LootCrateUseHandler lootCrateUseHandler = injector.getInstance(LootCrateUseHandler.class);
        eventService.subscribe(lootCrateUseHandler);

        EquipmentUpgradeUseHandler equipmentUpgradeUseHandler = injector.getInstance(EquipmentUpgradeUseHandler.class);
        eventService.subscribe(equipmentUpgradeUseHandler);

        ShopCycleHandler shopCycleHandler = injector.getInstance(ShopCycleHandler.class);
        eventService.subscribe(shopCycleHandler);

        // Player ranks
        PlayerRankChangeHandler playerRankChangeHandler = injector.getInstance(PlayerRankChangeHandler.class);
        eventService.subscribe(playerRankChangeHandler);

        PlayerRankChangedHandler playerRankChangedHandler = injector.getInstance(PlayerRankChangedHandler.class);
        eventService.subscribe(playerRankChangedHandler);

        PrestigeAttributesHandler prestigeAttributesHandler = injector.getInstance(PrestigeAttributesHandler.class);
        eventService.subscribe(prestigeAttributesHandler);

        RankUpRewardHandler rankUpRewardHandler = injector.getInstance(RankUpRewardHandler.class);
        eventService.subscribe(rankUpRewardHandler);
        commandCommandService.createCommand("claimrankuprewards", Collections.emptyList(), rankUpRewardHandler);

        // Statistics
        DamageHealingDoneTakenStatisticHandler damageHealingDoneTakenStatisticHandler =
                injector.getInstance(DamageHealingDoneTakenStatisticHandler.class);
        eventService.subscribe(damageHealingDoneTakenStatisticHandler);

        KillDeathStatisticHandler killDeathStatisticHandler =
                injector.getInstance(KillDeathStatisticHandler.class);
        eventService.subscribe(killDeathStatisticHandler);

        ObjectiveStatisticHandler objectiveStatisticHandler =
                injector.getInstance(ObjectiveStatisticHandler.class);
        eventService.subscribe(objectiveStatisticHandler);

        ResourcesGatheredStatisticHandler resourcesGatheredStatisticHandler =
                injector.getInstance(ResourcesGatheredStatisticHandler.class);
        eventService.subscribe(resourcesGatheredStatisticHandler);

        LootCrateStatisticHandler lootCrateStatisticHandler =
                injector.getInstance(LootCrateStatisticHandler.class);
        eventService.subscribe(lootCrateStatisticHandler);

        TimePlayedStatisticHandler timePlayedStatisticHandler = injector.getInstance(TimePlayedStatisticHandler.class);
        eventService.subscribe(timePlayedStatisticHandler);

        CurrencyStatisticHandler currencyStatisticHandler = injector.getInstance(CurrencyStatisticHandler.class);
        eventService.subscribe(currencyStatisticHandler);

        // Player inspect
        PlayerInspectHandler playerInspectHandler = injector.getInstance(PlayerInspectHandler.class);
        eventService.subscribe(playerInspectHandler);

        // Game / Objectives
        ObjectiveAnnouncementHandler objectiveAnnouncementHandler =
                injector.getInstance(ObjectiveAnnouncementHandler.class);
        eventService.subscribe(objectiveAnnouncementHandler);

        GameObjectivesAndPointsHandler gameObjectivesAndPointsHandler =
                injector.getInstance(GameObjectivesAndPointsHandler.class);
        eventService.subscribe(gameObjectivesAndPointsHandler);

        GameTimerHandler gameTimerHandler = injector.getInstance(GameTimerHandler.class);
        taskService.runTask(gameTimerHandler, timeUtils.createDurationInTicks(5), timeUtils.createDurationInSeconds(1));

        SpigotScoreboardHandler spigotScoreboardHandler = injector.getInstance(SpigotScoreboardHandler.class);
        taskService.runTask(spigotScoreboardHandler, timeUtils.createDurationInTicks(9), timeUtils.createDurationInSeconds(1));

        DailySkirmishHandler dailySkirmishHandler = injector.getInstance(DailySkirmishHandler.class);
        taskService.runTask(dailySkirmishHandler, timeUtils.createDurationInSeconds(60), timeUtils.createDurationInSeconds(60));

        SkirmishRewardHandler skirmishRewardHandler = injector.getInstance(SkirmishRewardHandler.class);
        eventService.subscribe(skirmishRewardHandler);

        // Metrics
        if (isProduction) {
            PlayerCountMetricHandler playerCountMetricHandler = injector.getInstance(PlayerCountMetricHandler.class);
            eventService.subscribe(playerCountMetricHandler);
            taskService.runTask(playerCountMetricHandler, timeUtils.oneTick(), timeUtils.createDurationInSeconds(300));

            TotalPointsMetricHandler totalPointsMetricHandler = injector.getInstance(TotalPointsMetricHandler.class);
            eventService.subscribe(totalPointsMetricHandler);
        }

        // Combat logging
        CombatLoggingHandler combatLoggingHandler = injector.getInstance(CombatLoggingHandler.class);
        eventService.subscribe(combatLoggingHandler);

        // Challenges
        ChallengeResetHandler challengeResetHandler = injector.getInstance(ChallengeResetHandler.class);
        eventService.subscribe(challengeResetHandler);

        ChallengeCycleHandler challengeCycleHandler = injector.getInstance(ChallengeCycleHandler.class);
        eventService.subscribe(challengeCycleHandler);

        ChallengeCompletionHandler challengeCompletionHandler = injector.getInstance(ChallengeCompletionHandler.class);
        eventService.subscribe(challengeCompletionHandler);

        // Leaderboards
        LeaderboardUpdateHandler leaderboardUpdateHandler = injector.getInstance(LeaderboardUpdateHandler.class);
        eventService.subscribe(leaderboardUpdateHandler);

        LeaderboardRewardHandler leaderboardRewardHandler = injector.getInstance(LeaderboardRewardHandler.class);
        eventService.subscribe(leaderboardRewardHandler);

        // Gathering
        // TODO re-enable
        // CapturedGatheringRestrictionHandler capturedGatheringRestrictionHandler = injector.getInstance(CapturedGatheringRestrictionHandler.class);
        // eventService.subscribe(capturedGatheringRestrictionHandler);
    }

    void initializeLegendsCommands() {
        CommandCommandService commandCommandService = injector.getInstance(CommandCommandService.class);

        MenuCommandHandler menuCommandHandler = injector.getInstance(MenuCommandHandler.class);
        commandCommandService.createCommand("me", Lists.newArrayList("menu", "monolith", "help"), menuCommandHandler);
        commandCommandService.createCommand("team", Lists.newArrayList("race"), menuCommandHandler);
        commandCommandService.createCommand("class", Lists.newArrayList("kit", "kits"), menuCommandHandler);
        commandCommandService.createCommand("dec", Lists.newArrayList("decoration", "decorations"), menuCommandHandler);
        commandCommandService.createCommand("pet", Lists.newArrayList("pets", "minipet", "minipets", "mp"), menuCommandHandler);

        GoldCommandHandler goldCommandHandler = injector.getInstance(GoldCommandHandler.class);
        commandCommandService.createCommand("gold", Lists.newArrayList("money", "bal", "balance"), goldCommandHandler);

        FishCommandHandler fishCommandHandler = injector.getInstance(FishCommandHandler.class);
        commandCommandService.createCommand("fish", Collections.emptyList(), fishCommandHandler);

        MobDropHandler mobDropHandler = injector.getInstance(MobDropHandler.class);
        commandCommandService.createCommand("dropitems", Collections.emptyList(), mobDropHandler);
        eventService.subscribe(mobDropHandler);

        SpawnCommandHandler spawnCommandHandler = injector.getInstance(SpawnCommandHandler.class);
        commandCommandService.createCommand("spawn", Lists.newArrayList("return"), spawnCommandHandler);

        KitCommandHandler kitCommandHandler = injector.getInstance(KitCommandHandler.class);
        commandCommandService.createCommand("kit", Collections.emptyList(), kitCommandHandler);

        ShopCommandHandler shopCommandHandler = injector.getInstance(ShopCommandHandler.class);
        commandCommandService.createCommand("shop", Collections.emptyList(), shopCommandHandler);

        TrebCommandHandler trebCommandHandler = injector.getInstance(TrebCommandHandler.class);
        commandCommandService.createCommand("treb", Lists.newArrayList("trebuchet"), trebCommandHandler);

        LaunchRewardsCommandHandler launchRewardsCommandHandler = injector.getInstance(LaunchRewardsCommandHandler.class);
        commandCommandService.createCommand("ilovewarcraft", Collections.emptyList(), launchRewardsCommandHandler);

        VoteCommandHandler voteCommandHandler = injector.getInstance(VoteCommandHandler.class);
        commandCommandService.createCommand("vote", Collections.emptyList(), voteCommandHandler);

        FreezeCommandHandler freezeCommandHandler = injector.getInstance(FreezeCommandHandler.class);
        commandCommandService.createCommand("freeze", Lists.newArrayList("unfreeze"), freezeCommandHandler);
    }

    // TODO remove all this code and any classes from the spigotOLD package
    void initializeOLDHACK() {
        LegendsPlayerQueryService playerQueryService = injector.getInstance(LegendsPlayerQueryService.class);
        EntityCommandService entityCommandService = injector.getInstance(EntityCommandService.class);
        EntityQueryService entityQueryService = injector.getInstance(EntityQueryService.class);
        CombatPetFactory combatPetFactory = injector.getInstance(CombatPetFactory.class);
        TaskService taskService = injector.getInstance(TaskService.class);
        TimeUtils timeUtils = injector.getInstance(TimeUtils.class);
        EventService eventService = injector.getInstance(EventService.class);
        CombatFactory combatFactory = injector.getInstance(CombatFactory.class);

        eventService.subscribe(new CraftPreventionHandler());

        HunterPetListener hunterPetListener = new HunterPetListener(getServer(), playerQueryService, entityQueryService,
                entityCommandService, combatPetFactory, taskService, timeUtils, combatFactory);
        eventService.subscribe(hunterPetListener);
        getServer().getPluginManager().registerEvents(hunterPetListener, this);

        SpigotMiniPetFactory spigotMiniPetFactory = injector.getInstance(SpigotMiniPetFactory.class);
        eventService.subscribe(spigotMiniPetFactory);

        PersonalScoreboardHandler personalScoreboardHandler = new PersonalScoreboardHandler(eventService, getServer(),
                this, playerQueryService, taskService, timeUtils);
        eventService.subscribe(personalScoreboardHandler);

        RangerOnlyBowHandler rangerOnlyBowHandler = new RangerOnlyBowHandler(playerQueryService);
        getServer().getPluginManager().registerEvents(rangerOnlyBowHandler, this);

        SpydeyBot spyBot = injector.getInstance(SpydeyBot.class);
        eventService.subscribe(spyBot);

        OnePointEightPvPInitializationHandler onePointEightPvPInitializationHandler = new OnePointEightPvPInitializationHandler();
        getServer().getPluginManager().registerEvents(onePointEightPvPInitializationHandler, this);

        // TODO mass bamboozle is actually implemented quite cleanly, needs to be in the spigot package until
        // TODO LibsDisguises support has been added to Monolith though
        MassBamboozleHandler massBamboozleHandler = injector.getInstance(MassBamboozleHandler.class);
        eventService.subscribe(massBamboozleHandler);

        AbilityCommandService abilityCommandService = injector.getInstance(AbilityCommandService.class);
        abilityCommandService.registerRangedAbility("Mass Bamboozle", MassBamboozle.class, MassBamboozleFactory.class);
    }

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

    private void initializeGame() {
        GameCommandService gameCommandService = injector.getInstance(GameCommandService.class);
        GameQueryService gameQueryService = injector.getInstance(GameQueryService.class);
        Game activeGame = gameQueryService.getActiveGame();
        if (activeGame == null) {
            Game currentWar = gameQueryService.getCurrentWar();
            if (currentWar == null) {
                gameCommandService.startWar();
            } else {
                throw new IllegalStateException("There is a current war, but no active game. This shouldn't happen.");
            }
        }
        gameCommandService.initializeActiveGame();
    }

    @Override
    public void onLoad() {
        saveDefaultConfig();
        Monolith.registerModule(new SpigotLegendsModule(this));
    }

    @Override
    public void onEnable() {
        this.injector = Monolith.getInstance().getInjector();
        this.eventService = injector.getInstance(EventService.class);
        this.taskService = injector.getInstance(TaskService.class);
        this.timeUtils = injector.getInstance(TimeUtils.class);

        boolean isProduction = getConfig().getBoolean("isProduction");

        loadServerConfiguration();
        loadNpcsConfiguration();
        loadShopConfiguration();
        loadEquipmentConfiguration();
        initializeLegendsTeams();
        initializeLegendsHandlers(isProduction);
        initializeLegendsCommands();
        initializeOLDHACK();
        initializeTutorial();
        initializeGame();
        injector.getInstance(LegendsCombatConfiguration.class);
    }
}
 */
