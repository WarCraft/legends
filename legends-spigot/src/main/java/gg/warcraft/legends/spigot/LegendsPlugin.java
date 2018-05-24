package gg.warcraft.legends.spigot;

import com.google.inject.Guice;
import com.google.inject.Injector;
import gg.warcraft.legends.app.chat.LegendsChatConfiguration;
import gg.warcraft.legends.app.chat.LegendsChatModule;
import gg.warcraft.legends.app.command.LegendsCommandConfiguration;
import gg.warcraft.legends.app.entity.player.LegendsPlayerConfiguration;
import gg.warcraft.monolith.api.event.EventService;
import gg.warcraft.monolith.app.command.ConsoleCommandSender;
import gg.warcraft.monolith.app.command.PlayerCommandSender;
import gg.warcraft.monolith.app.command.event.SimpleCommandExecutedEvent;
import gg.warcraft.monolith.spigot.SpigotMonolithModule;
import gg.warcraft.monolith.spigot.event.SpigotChatEventMapper;
import gg.warcraft.monolith.spigot.event.SpigotPlayerEventMapper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class LegendsPlugin extends JavaPlugin {
    private static final String PLUGIN_NAME = "Monolith";

    private Injector injector;
    private EventService eventService;

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
        injector.getInstance(LegendsCommandConfiguration.class);
        injector.getInstance(LegendsPlayerConfiguration.class);

        // TODO move elsewhere
        eventService = injector.getInstance(EventService.class);
        getServer().getPluginManager().registerEvents(new SpigotChatEventMapper(eventService), this);
        getServer().getPluginManager().registerEvents(new SpigotPlayerEventMapper(eventService), this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // TODO replace this with redis event bus
        eventService.publish(new SimpleCommandExecutedEvent(
                (sender instanceof Player)
                        ? new PlayerCommandSender(sender.getName(), ((Player) sender).getUniqueId())
                        : new ConsoleCommandSender(),
                command.getName(),
                label,
                Arrays.asList(args)));
        return true;
    }
}
