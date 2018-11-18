package gg.warcraft.legends.spigot;

import com.google.common.collect.Lists;
import com.google.inject.Injector;
import gg.warcraft.combat.api.ability.config.service.AbilityConfigurationQueryService;
import gg.warcraft.combat.api.ability.service.AbilityCommandService;
import gg.warcraft.legends.api.ai.CombatPetFactory;
import gg.warcraft.legends.api.ai.npc.config.NpcsConfiguration;
import gg.warcraft.legends.api.ai.npc.service.NpcCommandService;
import gg.warcraft.legends.api.config.ServerConfiguration;
import gg.warcraft.legends.api.core.game.Game;
import gg.warcraft.legends.api.core.game.service.GameCommandService;
import gg.warcraft.legends.api.core.game.service.GameQueryService;
import gg.warcraft.legends.api.core.service.SpawnLocationRepository;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.entity.player.rank.service.RankCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.item.config.EquipmentConfiguration;
import gg.warcraft.legends.api.item.config.service.EquipmentConfigurationRepository;
import gg.warcraft.legends.api.item.crate.service.LootCrateCommandService;
import gg.warcraft.legends.api.shop.config.ShopConfiguration;
import gg.warcraft.legends.api.shop.service.ShopCommandService;
import gg.warcraft.legends.app.ai.npc.handler.NpcInteractHandler;
import gg.warcraft.legends.app.ai.npc.handler.NpcPersistenceHandler;
import gg.warcraft.legends.app.combat.LegendsCombatConfiguration;
import gg.warcraft.legends.app.combat.handler.ArrowDistributionHandler;
import gg.warcraft.legends.app.combat.handler.ArrowPickupHandler;
import gg.warcraft.legends.app.combat.passive.handler.PassiveAssignmentHandler;
import gg.warcraft.legends.app.combat.ranger.marksman.FocusHandler;
import gg.warcraft.legends.app.config.LegendsMapperModule;
import gg.warcraft.legends.app.core.game.handler.GameObjectivesAndPointsHandler;
import gg.warcraft.legends.app.core.game.handler.GameTimerHandler;
import gg.warcraft.legends.app.core.game.handler.ObjectiveAnnouncementHandler;
import gg.warcraft.legends.app.core.handler.CombatLoggingHandler;
import gg.warcraft.legends.app.core.handler.FishCommandHandler;
import gg.warcraft.legends.app.core.handler.PlayerCountMetricHandler;
import gg.warcraft.legends.app.core.handler.PlayerSpawnHandler;
import gg.warcraft.legends.app.core.handler.TotalPointsMetricHandler;
import gg.warcraft.legends.app.core.handler.UniversalBlockBreakPlaceHandler;
import gg.warcraft.legends.app.entity.player.handler.PlayerCurrencyEventHandler;
import gg.warcraft.legends.app.entity.player.handler.PlayerInspectionHandler;
import gg.warcraft.legends.app.entity.player.rank.handler.PlayerRankChangeHandler;
import gg.warcraft.legends.app.entity.player.rank.handler.PlayerRankChangedHandler;
import gg.warcraft.legends.app.entity.player.rank.handler.PrestigeAttributesHandler;
import gg.warcraft.legends.app.item.crate.handler.LootCrateUseHandler;
import gg.warcraft.legends.app.item.handler.EquipmentAttributesHandler;
import gg.warcraft.legends.app.menu.handler.MenuCommandHandler;
import gg.warcraft.legends.app.misc.SpydeyBot;
import gg.warcraft.legends.app.shop.profile.handler.ShopProfileInitializationHandler;
import gg.warcraft.legends.spigot.OLD.combat.HunterPetListener;
import gg.warcraft.legends.spigot.OLD.combat.PersonalScoreboardHandler;
import gg.warcraft.legends.spigot.OLD.combat.handler.RangerOnlyBowHandler;
import gg.warcraft.legends.spigot.OLD.combat.mage.MassBamboozle;
import gg.warcraft.legends.spigot.OLD.combat.mage.MassBamboozleFactory;
import gg.warcraft.legends.spigot.OLD.combat.mage.MassBamboozleHandler;
import gg.warcraft.legends.spigot.OLD.shop.SpigotMiniPetFactory;
import gg.warcraft.legends.spigot.core.handler.SpigotScoreboardHandler;
import gg.warcraft.monolith.api.Monolith;
import gg.warcraft.monolith.api.MonolithPluginUtils;
import gg.warcraft.monolith.api.command.service.CommandCommandService;
import gg.warcraft.monolith.api.core.EventService;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.entity.team.service.TeamCommandService;
import gg.warcraft.monolith.api.util.TimeUtils;
import gg.warcraft.monolith.api.world.location.LocationFactory;
import gg.warcraft.monolith.api.world.location.OrientedLocation;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collections;

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

        SpawnLocationRepository spawnLocationRepository = injector.getInstance(SpawnLocationRepository.class);
        serverConfiguration.getSpawnLocations().forEach((team, location) -> {
            OrientedLocation spawn = locationFactory.createOrientedLocation(location);
            spawnLocationRepository.save(team, spawn);
        });

        RankCommandService rankCommandService = injector.getInstance(RankCommandService.class);
        rankCommandService.initializeRanks(LegendsTeam.HUMAN, serverConfiguration.getRankFavorTable(),
                serverConfiguration.getRankPrefixes(), serverConfiguration.getHumanRankSuffixes());
        rankCommandService.initializeRanks(LegendsTeam.ORC, serverConfiguration.getRankFavorTable(),
                serverConfiguration.getRankPrefixes(), serverConfiguration.getOrcRankSuffixes());

        GameCommandService gameCommandService = injector.getInstance(GameCommandService.class);
        gameCommandService.readConfiguration(serverConfiguration);

        LootCrateCommandService lootCrateCommandService = injector.getInstance(LootCrateCommandService.class);
        lootCrateCommandService.readConfiguration(serverConfiguration);
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
        UniversalBlockBreakPlaceHandler universalBlockBreakPlaceHandler =
                injector.getInstance(UniversalBlockBreakPlaceHandler.class);
        eventService.subscribe(universalBlockBreakPlaceHandler);

        PlayerSpawnHandler playerSpawnHandler = injector.getInstance(PlayerSpawnHandler.class);
        eventService.subscribe(playerSpawnHandler);

        PlayerCurrencyEventHandler playerCurrencyEventHandler = injector.getInstance(PlayerCurrencyEventHandler.class);
        eventService.subscribe(playerCurrencyEventHandler);

        // AI
        NpcInteractHandler npcInteractHandler = injector.getInstance(NpcInteractHandler.class);
        eventService.subscribe(npcInteractHandler);

        NpcPersistenceHandler npcPersistenceHandler = injector.getInstance(NpcPersistenceHandler.class);
        eventService.subscribe(npcPersistenceHandler);

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

        // Shop
        ShopProfileInitializationHandler shopProfileInitializationHandler =
                injector.getInstance(ShopProfileInitializationHandler.class);
        eventService.subscribe(shopProfileInitializationHandler);

        LootCrateUseHandler lootCrateUseHandler = injector.getInstance(LootCrateUseHandler.class);
        eventService.subscribe(lootCrateUseHandler);

        // Player ranks
        PlayerRankChangeHandler playerRankChangeHandler = injector.getInstance(PlayerRankChangeHandler.class);
        eventService.subscribe(playerRankChangeHandler);

        PlayerRankChangedHandler playerRankChangedHandler = injector.getInstance(PlayerRankChangedHandler.class);
        eventService.subscribe(playerRankChangedHandler);

        PrestigeAttributesHandler prestigeAttributesHandler = injector.getInstance(PrestigeAttributesHandler.class);
        eventService.subscribe(prestigeAttributesHandler);

        // Player inspect
        PlayerInspectionHandler playerInspectionHandler = injector.getInstance(PlayerInspectionHandler.class);
        eventService.subscribe(playerInspectionHandler);

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
    }

    void initializeLegendsCommands() {
        CommandCommandService commandCommandService = injector.getInstance(CommandCommandService.class);

        MenuCommandHandler menuCommandHandler = injector.getInstance(MenuCommandHandler.class);
        commandCommandService.createCommand("me", Lists.newArrayList("menu", "monolith", "help"), menuCommandHandler);
        commandCommandService.createCommand("team", Lists.newArrayList("race"), menuCommandHandler);
        commandCommandService.createCommand("spec", Lists.newArrayList("specialization", "class", "kits"), menuCommandHandler);
        commandCommandService.createCommand("dec", Lists.newArrayList("decoration", "decorations"), menuCommandHandler);
        commandCommandService.createCommand("pet", Lists.newArrayList("pets", "minipet", "minipets", "mp"), menuCommandHandler);

        FishCommandHandler fishCommandHandler = injector.getInstance(FishCommandHandler.class);
        commandCommandService.createCommand("fish", Collections.emptyList(), fishCommandHandler);
    }

    // TODO remove all this code and any classes from the spigotOLD package
    void initializeOLDHACK() {
        LegendsPlayerQueryService legendsPlayerQueryService = injector.getInstance(LegendsPlayerQueryService.class);
        AbilityConfigurationQueryService abilityConfigurationQueryService = injector.getInstance(AbilityConfigurationQueryService.class);
        TeamCommandService teamCommandService = injector.getInstance(TeamCommandService.class);
        CombatPetFactory combatPetFactory = injector.getInstance(CombatPetFactory.class);
        TaskService taskService = injector.getInstance(TaskService.class);
        TimeUtils timeUtils = injector.getInstance(TimeUtils.class);
        EventService eventService = injector.getInstance(EventService.class);

        HunterPetListener hunterPetListener = new HunterPetListener(getServer(), legendsPlayerQueryService,
                teamCommandService, combatPetFactory, taskService, timeUtils);
        eventService.subscribe(hunterPetListener);
        getServer().getPluginManager().registerEvents(hunterPetListener, this);

        SpigotMiniPetFactory spigotMiniPetFactory = injector.getInstance(SpigotMiniPetFactory.class);
        eventService.subscribe(spigotMiniPetFactory);

        PersonalScoreboardHandler personalScoreboardHandler = new PersonalScoreboardHandler(eventService, getServer(),
                this, legendsPlayerQueryService, taskService, timeUtils);
        eventService.subscribe(personalScoreboardHandler);

        RangerOnlyBowHandler rangerOnlyBowHandler = new RangerOnlyBowHandler(legendsPlayerQueryService);
        getServer().getPluginManager().registerEvents(rangerOnlyBowHandler, this);

        SpydeyBot spyBot = injector.getInstance(SpydeyBot.class);
        eventService.subscribe(spyBot);

        // TODO mass bamboozle is actually implemented quite cleanly, needs to be in the spigot package until
        // TODO LibsDisguises support has been added to Monolith though
        MassBamboozleHandler massBamboozleHandler = injector.getInstance(MassBamboozleHandler.class);
        eventService.subscribe(massBamboozleHandler);

        AbilityCommandService abilityCommandService = injector.getInstance(AbilityCommandService.class);
        abilityCommandService.registerRangedAbility("Mass Bamboozle", MassBamboozle.class, MassBamboozleFactory.class);
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
        initializeGame();
        injector.getInstance(LegendsCombatConfiguration.class);
    }
}
