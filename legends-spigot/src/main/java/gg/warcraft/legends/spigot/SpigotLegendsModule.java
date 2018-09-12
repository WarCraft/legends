package gg.warcraft.legends.spigot;

import gg.warcraft.legends.api.shop.MiniPetFactory;
import gg.warcraft.legends.app.AbstractLegendsModule;
import gg.warcraft.legends.spigot.OLD.shop.SpigotMiniPetFactory;
import gg.warcraft.monolith.api.core.PluginLogger;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

public class SpigotLegendsModule extends AbstractLegendsModule {
    private final Plugin plugin;

    public SpigotLegendsModule(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        super.configure();
        bind(Plugin.class).toInstance(plugin);
        bind(Logger.class).annotatedWith(PluginLogger.class).toProvider(plugin::getLogger);

        // TODO eventually remove this hack and all classes associated with it
        bind(MiniPetFactory.class).to(SpigotMiniPetFactory.class);
        expose(MiniPetFactory.class);
    }
}
