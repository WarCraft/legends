package gg.warcraft.legends.chat;

import gg.warcraft.legends.entity.LegendsRace;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.legends.player.LegendsPlayerService;
import gg.warcraft.monolith.AuthorizationService;
import gg.warcraft.monolith.LoggerService;
import gg.warcraft.monolith.chat.Channel;
import gg.warcraft.monolith.chat.DefaultChatService;
import gg.warcraft.monolith.chat.GlobalChannel;
import gg.warcraft.monolith.chat.Message;
import gg.warcraft.monolith.chat.factory.SimpleMessageFactory;
import gg.warcraft.monolith.chat.logger.ConsoleMessageLogger;
import gg.warcraft.monolith.chat.logger.MessageLogger;
import gg.warcraft.monolith.command.CommandService;
import gg.warcraft.monolith.event.EventService;
import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.DARK_GRAY;
import static org.bukkit.ChatColor.DARK_PURPLE;
import static org.bukkit.ChatColor.DARK_RED;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.LIGHT_PURPLE;
import static org.bukkit.ChatColor.WHITE;
import static org.bukkit.ChatColor.YELLOW;

public class LegendsChatService extends DefaultChatService {

    private final CommandService commandService;
    private final Channel serverNotificationChannel;
    private final Channel staffNotificationChannel;
    private final Channel globalChannel;
    private final Channel staffChannel;

    public LegendsChatService(LoggerService loggerService, AuthorizationService authService, CommandService commandService, EventService eventService) {
        super(loggerService, commandService, eventService);
        this.commandService = commandService;
        MessageLogger consoleLogger = new ConsoleMessageLogger();

        globalChannel = new GlobalChannel(this, "Global", null, null,
                new SimpleMessageFactory((sender, message) ->
                        GRAY + "[Global] " + getStaffTag(sender) + GRAY + sender.getName() + ": " + message),
                consoleLogger,
                player -> true);
        registerChannel(globalChannel);

        GlobalChannel humanChannel = new GlobalChannel(this, "Human", null, null,
                new SimpleMessageFactory((sender, message) -> GRAY + "[Human] " + sender.getName() + DARK_GRAY + ": " + message),
                consoleLogger,
                player -> {
                    // TODO refactor to have channels accept monolithPlayers
                    LegendsPlayer legendsPlayer = LegendsPlayerService.getInstance().getLegendsPlayer(player.getUniqueId());
                    return legendsPlayer.getRace() == LegendsRace.HUMAN;
                });
        registerChannel(humanChannel);

        GlobalChannel orcChannel = new GlobalChannel(this, "Orc", null, null,
                new SimpleMessageFactory((sender, message) -> GRAY + "[Orc] " + sender.getName() + DARK_GRAY + ": " + message),
                consoleLogger,
                player -> {
                    LegendsPlayer legendsPlayer = LegendsPlayerService.getInstance().getLegendsPlayer(player.getUniqueId());
                    return legendsPlayer.getRace() == LegendsRace.ORC;
                });
        registerChannel(orcChannel);

        staffChannel = new GlobalChannel(this, "Staff", null, null,
                new SimpleMessageFactory((sender, message) -> LIGHT_PURPLE + "[Staff] " + sender.getName() + GRAY + ": " + message),
                consoleLogger,
                authService::isStaff);
        registerChannel(staffChannel);

        serverNotificationChannel = new GlobalChannel(this, "Server", null, null,
                new SimpleMessageFactory((sender, message) -> YELLOW + "[Server] " + message),
                consoleLogger,
                player -> true);
        registerChannel(serverNotificationChannel);

        staffNotificationChannel = new GlobalChannel(this, "Staff", null, null,
                new SimpleMessageFactory((sender, message) -> DARK_PURPLE + "[Staff] " + WHITE + message),
                consoleLogger,
                authService::isStaff);
        registerChannel(staffNotificationChannel);

        eventService.registerForEvents(new RacialChatChannelListener(humanChannel, orcChannel));
    }

    @Override
    public Channel getDefaultChannel() {
        return globalChannel;
    }

    @Override
    public void notifyServer(Message message) {
        commandService.dispatchConsoleCommand(serverNotificationChannel.getName() + " " + message);
    }

    @Override
    public void notifyStaff(Message message) {
        commandService.dispatchConsoleCommand(staffNotificationChannel.getName() + " " + message);
    }

    private String getStaffTag(CommandSender sender) {
        switch (sender.getName()) {
            case "fishb6nes":
            case "eijlen":
                return DARK_RED + "[Admin] ";
            case "Dragonayr":
                return GOLD + "[Mod] ";
            default:
                return "";
        }
    }
}
