package gg.warcraft.legends.spigot;

import com.google.inject.Injector;
import gg.warcraft.legends.app.entity.player.handler.PlayerCurrencyEventHandler;
import gg.warcraft.monolith.api.Monolith;
import gg.warcraft.monolith.api.core.EventService;
import org.bukkit.plugin.java.JavaPlugin;

public class LegendsPlugin extends JavaPlugin {
    private Injector injector;
    private EventService eventService;

    void initializeLegendsHandlers() {
        PlayerCurrencyEventHandler playerCurrencyEventHandler = injector.getInstance(PlayerCurrencyEventHandler.class);
        eventService.subscribe(playerCurrencyEventHandler);
    }

    @Override
    public void onLoad() {
        Monolith.registerModule(new SpigotLegendsModule());
    }

    @Override
    public void onEnable() {
        this.injector = Monolith.getInstance().getInjector();
        this.eventService = injector.getInstance(EventService.class);

        initializeLegendsHandlers();
    }
}
