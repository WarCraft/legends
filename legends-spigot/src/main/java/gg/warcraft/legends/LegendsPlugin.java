package gg.warcraft.legends;

import com.google.inject.Guice;
import com.google.inject.Injector;
import gg.warcraft.legends.chat.LegendsChatConfiguration;
import gg.warcraft.legends.chat.LegendsChatModule;
import gg.warcraft.monolith.api.core.EventService;
import gg.warcraft.monolith.spigot.SpigotMonolithModule;
import gg.warcraft.monolith.spigot.event.SpigotChatEventMapper;
import gg.warcraft.monolith.spigot.event.SpigotPlayerEventMapper;
import org.bukkit.plugin.java.JavaPlugin;

public class LegendsPlugin extends JavaPlugin {
    private static final String PLUGIN_NAME = "Monolith";

    private Injector injector;

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        String worldName = getConfig().getString("world");

        this.injector = Guice.createInjector(
                new SpigotMonolithModule(PLUGIN_NAME, worldName),
                new SpigotLegendsModule(),
                new LegendsChatModule());

        injector.getInstance(LegendsChatConfiguration.class);

        // TODO move elsewhere
        EventService eventService = injector.getInstance(EventService.class);
        getServer().getPluginManager().registerEvents(new SpigotChatEventMapper(eventService), this);
        getServer().getPluginManager().registerEvents(new SpigotPlayerEventMapper(eventService), this);
    }
}
