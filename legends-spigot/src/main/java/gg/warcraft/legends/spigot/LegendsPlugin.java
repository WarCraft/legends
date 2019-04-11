package gg.warcraft.legends.spigot;

import com.google.common.collect.Lists;
import com.google.inject.Injector;
import gg.warcraft.combat.api.ability.service.AbilityCommandService;
import gg.warcraft.legends.api.ai.CombatPetFactory;
import gg.warcraft.legends.api.ai.npc.config.NpcsConfiguration;
import gg.warcraft.legends.api.ai.npc.service.NpcCommandService;
import gg.warcraft.legends.api.config.ServerConfiguration;
import gg.warcraft.legends.api.core.game.Game;
import gg.warcraft.legends.api.core.game.service.GameCommandService;
import gg.warcraft.legends.api.core.game.service.GameQueryService;
import gg.warcraft.legends.api.core.service.ServerRepository;
import gg.warcraft.legends.api.core.service.SpawnRepository;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.entity.player.rank.service.RankCommandService;
import gg.warcraft.legends.api.entity.player.rank.service.RankRepository;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.item.config.EquipmentConfiguration;
import gg.warcraft.legends.api.item.config.service.EquipmentConfigurationRepository;
import gg.warcraft.legends.api.item.crate.service.LootCrateCommandService;
import gg.warcraft.legends.api.shop.config.ShopConfiguration;
import gg.warcraft.legends.api.shop.service.ShopCommandService;
import gg.warcraft.legends.app.ai.npc.handler.NpcInteractHandler;
import gg.warcraft.legends.app.ai.npc.handler.NpcPersistenceHandler;
import gg.warcraft.legends.app.ai.npc.handler.TargetDummyDamageHandler;
import gg.warcraft.legends.app.combat.LegendsCombatConfiguration;
import gg.warcraft.legends.app.combat.handler.ArrowDistributionHandler;
import gg.warcraft.legends.app.combat.handler.ArrowFriendlyFireHandler;
import gg.warcraft.legends.app.combat.handler.ArrowPickupHandler;
import gg.warcraft.legends.app.combat.handler.WhoDamagedWhoHandler;
import gg.warcraft.legends.app.combat.mage.orc.handler.FlameTotemDamageLimitationHandler;
import gg.warcraft.legends.app.combat.passive.handler.PassiveAssignmentHandler;
import gg.warcraft.legends.app.combat.ranger.marksman.FocusHandler;
import gg.warcraft.legends.app.config.LegendsMapperModule;
import gg.warcraft.legends.app.core.challenge.handler.ChallengeCompletionHandler;
import gg.warcraft.legends.app.core.challenge.handler.ChallengeCycleHandler;
import gg.warcraft.legends.app.core.challenge.handler.ChallengeResetHandler;
import gg.warcraft.legends.app.core.game.handler.DailySkirmishHandler;
import gg.warcraft.legends.app.core.game.handler.GameObjectivesAndPointsHandler;
import gg.warcraft.legends.app.core.game.handler.GameTimerHandler;
import gg.warcraft.legends.app.core.game.handler.ObjectiveAnnouncementHandler;
import gg.warcraft.legends.app.core.game.handler.SkirmishRewardHandler;
import gg.warcraft.legends.app.core.handler.CaptureAndKillRewardHandler;
import gg.warcraft.legends.app.core.handler.CombatLoggingHandler;
import gg.warcraft.legends.app.core.handler.FishCommandHandler;
import gg.warcraft.legends.app.core.handler.KitCommandHandler;
import gg.warcraft.legends.app.core.handler.LaunchRewardsCommandHandler;
import gg.warcraft.legends.app.core.handler.PlayerCountMetricHandler;
import gg.warcraft.legends.app.core.handler.PlayerSpawnHandler;
import gg.warcraft.legends.app.core.handler.SpawnCommandHandler;
import gg.warcraft.legends.app.core.handler.TotalPointsMetricHandler;
import gg.warcraft.legends.app.core.handler.TrebCommandHandler;
import gg.warcraft.legends.app.core.handler.UniversalBlockBreakPlaceHandler;
import gg.warcraft.legends.app.core.handler.VoteCommandHandler;
import gg.warcraft.legends.app.core.leaderboard.handler.LeaderboardRewardHandler;
import gg.warcraft.legends.app.core.leaderboard.handler.LeaderboardUpdateHandler;
import gg.warcraft.legends.app.core.tutorial.handler.TutorialCommandHandler;
import gg.warcraft.legends.app.core.tutorial.handler.TutorialStepHandler;
import gg.warcraft.legends.app.entity.player.handler.GoldCommandHandler;
import gg.warcraft.legends.app.entity.player.handler.PlayerCurrencyEventHandler;
import gg.warcraft.legends.app.entity.player.handler.PlayerInspectHandler;
import gg.warcraft.legends.app.entity.player.rank.handler.PlayerRankChangeHandler;
import gg.warcraft.legends.app.entity.player.rank.handler.PlayerRankChangedHandler;
import gg.warcraft.legends.app.entity.player.rank.handler.PrestigeAttributesHandler;
import gg.warcraft.legends.app.entity.player.rank.handler.RankUpRewardHandler;
import gg.warcraft.legends.app.entity.player.statistic.handler.CurrencyStatisticHandler;
import gg.warcraft.legends.app.entity.player.statistic.handler.DamageHealingDoneTakenStatisticHandler;
import gg.warcraft.legends.app.entity.player.statistic.handler.KillDeathStatisticHandler;
import gg.warcraft.legends.app.entity.player.statistic.handler.LootCrateStatisticHandler;
import gg.warcraft.legends.app.entity.player.statistic.handler.ObjectiveStatisticHandler;
import gg.warcraft.legends.app.entity.player.statistic.handler.ResourcesGatheredStatisticHandler;
import gg.warcraft.legends.app.entity.player.statistic.handler.TimePlayedStatisticHandler;
import gg.warcraft.legends.app.item.crate.handler.LootCrateUseHandler;
import gg.warcraft.legends.app.item.equipment.handler.EquipmentUpgradeUseHandler;
import gg.warcraft.legends.app.item.handler.EquipmentAttributesHandler;
import gg.warcraft.legends.app.item.handler.MobDropHandler;
import gg.warcraft.legends.app.menu.handler.MenuCommandHandler;
import gg.warcraft.legends.app.misc.SpydeyBot;
import gg.warcraft.legends.app.shop.handler.ShopCommandHandler;
import gg.warcraft.legends.app.shop.handler.ShopCycleHandler;
import gg.warcraft.legends.app.shop.profile.handler.ShopProfileInitializationHandler;
import gg.warcraft.legends.spigot.OLD.combat.HunterPetListener;
import gg.warcraft.legends.spigot.OLD.combat.PersonalScoreboardHandler;
import gg.warcraft.legends.spigot.OLD.combat.handler.RangerOnlyBowHandler;
import gg.warcraft.legends.spigot.OLD.combat.mage.MassBamboozle;
import gg.warcraft.legends.spigot.OLD.combat.mage.MassBamboozleFactory;
import gg.warcraft.legends.spigot.OLD.combat.mage.MassBamboozleHandler;
import gg.warcraft.legends.spigot.OLD.shop.SpigotMiniPetFactory;
import gg.warcraft.legends.spigot.core.handler.CraftPreventionHandler;
import gg.warcraft.legends.spigot.core.handler.OnePointEightPvPInitializationHandler;
import gg.warcraft.legends.spigot.core.handler.SpigotScoreboardHandler;
import gg.warcraft.legends.spigot.core.handler.VoteHandler;
import gg.warcraft.monolith.api.Monolith;
import gg.warcraft.monolith.api.MonolithPluginUtils;
import gg.warcraft.monolith.api.combat.CombatFactory;
import gg.warcraft.monolith.api.command.service.CommandCommandService;
import gg.warcraft.monolith.api.core.EventService;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.entity.service.EntityQueryService;
import gg.warcraft.monolith.api.entity.team.service.TeamCommandService;
import gg.warcraft.monolith.api.util.TimeUtils;
import gg.warcraft.monolith.api.world.Direction;
import gg.warcraft.monolith.api.world.WorldType;
import gg.warcraft.monolith.api.world.block.Block;
import gg.warcraft.monolith.api.world.block.box.BoundingBlockBox;
import gg.warcraft.monolith.api.world.block.box.BoundingBlockBoxFactory;
import gg.warcraft.monolith.api.world.location.LocationFactory;
import gg.warcraft.monolith.api.world.location.OrientedLocation;
import gg.warcraft.monolith.api.world.service.WorldQueryService;
import gg.warcraft.siege.api.weapon.PersistentSiegeWeaponDecorator;
import gg.warcraft.siege.api.weapon.SiegeWeapon;
import gg.warcraft.siege.api.weapon.SiegeWeaponFactory;
import gg.warcraft.siege.api.weapon.service.SiegeWeaponRepository;
import org.bukkit.plugin.java.JavaPlugin;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class LegendsPlugin extends JavaPlugin {
    private Injector injector;
    private EventService eventService;
    private TaskService taskService;
    private TimeUtils timeUtils;

    void loadServerConfiguration() {
        MonolithPluginUtils pluginUtils = injector.getInstance(MonolithPluginUtils.class);
        LocationFactory locationFactory = injector.getInstance(LocationFactory.class);
        LegendsMapperModule legendsMapperModule = new LegendsMapperModule();

        ServerConfiguration serverConfiguration = pluginUtils.loadConfiguration("REMOTE", "legends/server.yml", "",
                ServerConfiguration.class, legendsMapperModule);

        SpawnRepository spawnRepository = injector.getInstance(SpawnRepository.class);
        serverConfiguration.getSpawnLocations().forEach((team, location) -> {
            OrientedLocation spawn = locationFactory.createOrientedLocation(location);
            spawnRepository.save(team, spawn);
        });

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
        WorldQueryService worldQueryService = injector.getInstance(WorldQueryService.class);

        Block loadBlock = worldQueryService.getBlockAt(WorldType.THE_END, 294, 164, -193);
        Block turnLeftBlock = worldQueryService.getBlockAt(WorldType.THE_END, 293, 165, -198);
        Block turnRightBlock = worldQueryService.getBlockAt(WorldType.THE_END, 295, 165, -198);
        Block chargeBlock = worldQueryService.getBlockAt(WorldType.THE_END, 294, 165, -197);
        Block fireBlock = worldQueryService.getBlockAt(WorldType.THE_END, 294, 166, -197);
        Block projectileOriginBlock = worldQueryService.getBlockAt(WorldType.THE_END, 294, 176, -206);

        BoundingBlockBoxFactory boundingBlockBoxFactory = injector.getInstance(BoundingBlockBoxFactory.class);
        BoundingBlockBox tutorialTrebuchetBoundingBox = boundingBlockBoxFactory.createBoundingBlockBox(
                WorldType.THE_END, new Vector3i(291, 164, -206), new Vector3i(296, 176, -193));

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
