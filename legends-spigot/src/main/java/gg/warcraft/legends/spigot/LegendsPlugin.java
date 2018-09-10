package gg.warcraft.legends.spigot;

import com.google.common.collect.Lists;
import com.google.inject.Injector;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.shop.config.ShopConfiguration;
import gg.warcraft.legends.api.shop.service.ShopCommandService;
import gg.warcraft.legends.app.combat.LegendsCombatConfiguration;
import gg.warcraft.legends.app.combat.handler.ArrowDistributionHandler;
import gg.warcraft.legends.app.combat.handler.ArrowPickupHandler;
import gg.warcraft.legends.app.combat.passive.handler.PassiveAssignmentHandler;
import gg.warcraft.legends.app.combat.ranger.marksman.FocusHandler;
import gg.warcraft.legends.app.config.LegendsMapperModule;
import gg.warcraft.legends.app.entity.player.handler.PlayerCurrencyEventHandler;
import gg.warcraft.legends.app.item.handler.EquipmentAttributesHandler;
import gg.warcraft.legends.app.menu.handler.MenuCommandHandler;
import gg.warcraft.legends.app.shop.profile.handler.ShopProfileInitializationHandler;
import gg.warcraft.monolith.api.Monolith;
import gg.warcraft.monolith.api.MonolithPluginUtils;
import gg.warcraft.monolith.api.command.service.CommandCommandService;
import gg.warcraft.monolith.api.core.EventService;
import gg.warcraft.monolith.api.entity.team.service.TeamCommandService;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class LegendsPlugin extends JavaPlugin {
    private Injector injector;
    private EventService eventService;

    void loadShopConfiguration() {
        MonolithPluginUtils pluginUtils = injector.getInstance(MonolithPluginUtils.class);
        LegendsMapperModule legendsMapperModule = new LegendsMapperModule();

        ShopConfiguration shopConfiguration =
                pluginUtils.loadConfiguration("REMOTE", "legends/shop.yml", "", ShopConfiguration.class, legendsMapperModule);
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
    }

    void initializeLegendsCommands() {
        CommandCommandService commandCommandService = injector.getInstance(CommandCommandService.class);

        MenuCommandHandler menuCommandHandler = injector.getInstance(MenuCommandHandler.class);
        commandCommandService.createCommand("me", Lists.newArrayList("menu", "monolith", "help"), menuCommandHandler);
        commandCommandService.createCommand("team", Lists.newArrayList("race"), menuCommandHandler);
        commandCommandService.createCommand("spec", Lists.newArrayList("specialization", "class", "kits"), menuCommandHandler);
        commandCommandService.createCommand("dec", Lists.newArrayList("decoration", "decorations"), menuCommandHandler);
        commandCommandService.createCommand("pet", Lists.newArrayList("pets", "minipet", "minipets", "mp"), menuCommandHandler);
    }

    @Override
    public void onLoad() {
        Monolith.registerModule(new SpigotLegendsModule(this));
    }

    @Override
    public void onEnable() {
        this.injector = Monolith.getInstance().getInjector();
        this.eventService = injector.getInstance(EventService.class);

        loadShopConfiguration();
        initializeLegendsTeams();
        initializeLegendsHandlers();
        initializeLegendsCommands();
        injector.getInstance(LegendsCombatConfiguration.class);
    }
}
