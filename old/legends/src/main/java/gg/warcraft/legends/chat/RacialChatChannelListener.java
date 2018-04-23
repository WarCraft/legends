package gg.warcraft.legends.chat;

import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.legends.player.LegendsPlayerService;
import gg.warcraft.monolith.chat.GlobalChannel;
import gg.warcraft.monolith.event.MonolithEntityRaceChangedEvent;
import gg.warcraft.monolith.event.PlayerDisconnectEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class RacialChatChannelListener implements Listener {
    private final LegendsPlayerService playerService;
    private final GlobalChannel humanChannel;
    private final GlobalChannel orcChannel;

    public RacialChatChannelListener(GlobalChannel humanChannel, GlobalChannel orcChannel) {
        this(LegendsPlayerService.getInstance(), humanChannel, orcChannel);
    }

    public RacialChatChannelListener(LegendsPlayerService playerService,
                                     GlobalChannel humanChannel, GlobalChannel orcChannel) {
        this.playerService = playerService;
        this.humanChannel = humanChannel;
        this.orcChannel = orcChannel;
    }

    @EventHandler
    public void onMonolithEntityRaceChangedEvent(MonolithEntityRaceChangedEvent event) {
        LegendsPlayer player = playerService.getLegendsPlayer(event.getEntity().getUniqueId());
        if (player == null) {
            System.out.println("Failed to find player for EntityRaceChangedEvent in RacialChatChannelListener, this should never happen!");
        }

        // TODO fix hack when moving to kotlin isolated version of Chat
        PlayerDisconnectEvent disconnectEvent = new PlayerDisconnectEvent(player);
        PlayerJoinEvent playerJoinEvent = new PlayerJoinEvent(player, "");
        humanChannel.onPlayerDisconnectEvent(disconnectEvent); // TODO maybe add re-assess method in stead?
        orcChannel.onPlayerDisconnectEvent(disconnectEvent);
        humanChannel.onPlayerJoinEvent(playerJoinEvent);
        orcChannel.onPlayerJoinEvent(playerJoinEvent);
    }
}
