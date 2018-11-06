package gg.warcraft.legends.spigot;

import gg.warcraft.legends.api.ai.CombatPetFactory;
import gg.warcraft.legends.api.ai.NpcFactory;
import gg.warcraft.legends.api.shop.MiniPetFactory;
import gg.warcraft.legends.app.AbstractLegendsModule;
import gg.warcraft.legends.spigot.OLD.ai.SpigotCombatPetFactory;
import gg.warcraft.legends.spigot.OLD.ai.SpigotNpcFactory;
import gg.warcraft.legends.spigot.OLD.shop.SpigotMiniPetFactory;
import gg.warcraft.legends.spigot.core.handler.SpigotScoreboardHandler;
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

        bind(CombatPetFactory.class).to(SpigotCombatPetFactory.class);
        expose(CombatPetFactory.class);

        bind(gg.warcraft.legends.api.ai.MiniPetFactory.class).to(gg.warcraft.legends.spigot.OLD.ai.SpigotMiniPetFactory.class);
        expose(gg.warcraft.legends.api.ai.MiniPetFactory.class);
        bind(MiniPetFactory.class).to(SpigotMiniPetFactory.class);
        expose(MiniPetFactory.class);

        bind(NpcFactory.class).to(SpigotNpcFactory.class);
        expose(NpcFactory.class);

        bind(SpigotScoreboardHandler.class);
        expose(SpigotScoreboardHandler.class);
    }
}
