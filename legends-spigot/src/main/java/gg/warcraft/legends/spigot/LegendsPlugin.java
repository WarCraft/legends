package gg.warcraft.legends.spigot;

import com.google.common.collect.Lists;
import com.google.inject.Injector;
import gg.warcraft.combat.api.ability.config.service.AbilityConfigurationQueryService;
import gg.warcraft.combat.api.ability.service.AbilityCommandService;
import gg.warcraft.legends.api.config.ServerConfiguration;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.entity.player.rank.service.RankCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.shop.config.ShopConfiguration;
import gg.warcraft.legends.api.shop.service.ShopCommandService;
import gg.warcraft.legends.app.combat.LegendsCombatConfiguration;
import gg.warcraft.legends.app.combat.handler.ArrowDistributionHandler;
import gg.warcraft.legends.app.combat.handler.ArrowPickupHandler;
import gg.warcraft.legends.app.combat.passive.handler.PassiveAssignmentHandler;
import gg.warcraft.legends.app.combat.ranger.marksman.FocusHandler;
import gg.warcraft.legends.app.config.LegendsMapperModule;
import gg.warcraft.legends.app.core.game.handler.ObjectiveAnnouncementHandler;
import gg.warcraft.legends.app.core.handler.CombatLoggingHandler;
import gg.warcraft.legends.app.core.handler.FishCommandHandler;
import gg.warcraft.legends.app.core.handler.PlayerCountMetricHandler;
import gg.warcraft.legends.app.core.handler.TotalPointsMetricHandler;
import gg.warcraft.legends.app.entity.player.handler.PlayerCurrencyEventHandler;
import gg.warcraft.legends.app.entity.player.handler.PlayerInspectionHandler;
import gg.warcraft.legends.app.entity.player.rank.handler.PlayerRankChangeHandler;
import gg.warcraft.legends.app.entity.player.rank.handler.PlayerRankChangedHandler;
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
import gg.warcraft.monolith.api.Monolith;
import gg.warcraft.monolith.api.MonolithPluginUtils;
import gg.warcraft.monolith.api.command.service.CommandCommandService;
import gg.warcraft.monolith.api.core.EventService;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.entity.team.service.TeamCommandService;
import gg.warcraft.monolith.api.util.TimeUtils;
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
        LegendsMapperModule legendsMapperModule = new LegendsMapperModule();

        ServerConfiguration serverConfiguration = pluginUtils.loadConfiguration("REMOTE", "legends/server.yml", "",
                ServerConfiguration.class, legendsMapperModule);
        RankCommandService rankCommandService = injector.getInstance(RankCommandService.class);
        rankCommandService.initializeRanks(LegendsTeam.HUMAN, serverConfiguration.getRankFavorTable(),
                serverConfiguration.getRankPrefixes(), serverConfiguration.getHumanRankSuffixes());
        rankCommandService.initializeRanks(LegendsTeam.ORC, serverConfiguration.getRankFavorTable(),
                serverConfiguration.getRankPrefixes(), serverConfiguration.getOrcRankSuffixes());
    }

    void loadShopConfiguration() {
        MonolithPluginUtils pluginUtils = injector.getInstance(MonolithPluginUtils.class);
        LegendsMapperModule legendsMapperModule = new LegendsMapperModule();

        ShopConfiguration shopConfiguration = pluginUtils.loadConfiguration("REMOTE", "legends/shop.yml", "",
                ShopConfiguration.class, legendsMapperModule);
        ShopCommandService shopCommandService = injector.getInstance(ShopCommandService.class);
        shopCommandService.readConfiguration(shopConfiguration);
    }

    void initializeLegendsTeams() {
        TeamCommandService teamCommandService = injector.getInstance(TeamCommandService.class);
        Arrays.stream(LegendsTeam.values()).forEach(teamCommandService::registerTeam);
    }

    void initializeLegendsHandlers() {
        PlayerCurrencyEventHandler playerCurrencyEventHandler = injector.getInstance(PlayerCurrencyEventHandler.class);
        eventService.subscribe(playerCurrencyEventHandler);

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

        ShopProfileInitializationHandler shopProfileInitializationHandler =
                injector.getInstance(ShopProfileInitializationHandler.class);
        eventService.subscribe(shopProfileInitializationHandler);

        PlayerRankChangeHandler playerRankChangeHandler = injector.getInstance(PlayerRankChangeHandler.class);
        eventService.subscribe(playerRankChangeHandler);

        PlayerRankChangedHandler playerRankChangedHandler = injector.getInstance(PlayerRankChangedHandler.class);
        eventService.subscribe(playerRankChangedHandler);

        PlayerInspectionHandler playerInspectionHandler = injector.getInstance(PlayerInspectionHandler.class);
        eventService.subscribe(playerInspectionHandler);

        ObjectiveAnnouncementHandler objectiveAnnouncementHandler =
                injector.getInstance(ObjectiveAnnouncementHandler.class);
        eventService.subscribe(objectiveAnnouncementHandler);

        PlayerCountMetricHandler playerCountMetricHandler = injector.getInstance(PlayerCountMetricHandler.class);
        eventService.subscribe(playerCountMetricHandler);
        taskService.runTask(playerCountMetricHandler, timeUtils.oneTick(), timeUtils.createDurationInSeconds(300));

        TotalPointsMetricHandler totalPointsMetricHandler = injector.getInstance(TotalPointsMetricHandler.class);
        eventService.subscribe(totalPointsMetricHandler);

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
        TaskService taskService = injector.getInstance(TaskService.class);
        TimeUtils timeUtils = injector.getInstance(TimeUtils.class);
        EventService eventService = injector.getInstance(EventService.class);

        HunterPetListener hunterPetListener = new HunterPetListener(getServer(), legendsPlayerQueryService,
                teamCommandService, taskService, timeUtils);
        eventService.subscribe(hunterPetListener);
        getServer().getPluginManager().registerEvents(hunterPetListener, this);

        SpigotMiniPetFactory spigotMiniPetFactory = injector.getInstance(SpigotMiniPetFactory.class);
        eventService.subscribe(spigotMiniPetFactory);

        PersonalScoreboardHandler personalScoreboardHandler = new PersonalScoreboardHandler(abilityConfigurationQueryService,
                eventService, getServer(), this, legendsPlayerQueryService, taskService, timeUtils);
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

    @Override
    public void onLoad() {
        Monolith.registerModule(new SpigotLegendsModule(this));
    }

    @Override
    public void onEnable() {
        this.injector = Monolith.getInstance().getInjector();
        this.eventService = injector.getInstance(EventService.class);
        this.taskService = injector.getInstance(TaskService.class);
        this.timeUtils = injector.getInstance(TimeUtils.class);

        loadServerConfiguration();
        loadShopConfiguration();
        initializeLegendsTeams();
        initializeLegendsHandlers();
        initializeLegendsCommands();
        initializeOLDHACK();
        injector.getInstance(LegendsCombatConfiguration.class);
    }
}
