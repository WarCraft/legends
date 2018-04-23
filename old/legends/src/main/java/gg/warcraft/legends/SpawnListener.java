package gg.warcraft.legends;

import gg.warcraft.legends.config.LegendsConfigurationService;
import gg.warcraft.legends.config.ServerConfiguration;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.legends.player.LegendsPlayerService;
import gg.warcraft.monolith.event.MonolithEntityRaceChangedEvent;
import gg.warcraft.monolith.player.MonolithPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class SpawnListener implements Listener {

    private final LegendsConfigurationService configurationService;
    private final LegendsPlayerService playerService;

    private boolean forceSpawnOnJoin;
    private boolean forceSpawnOnFirstJoin;
    private boolean forceSpawnOnRespawn;

    public SpawnListener() {
        this(LegendsConfigurationService.getInstance(), LegendsPlayerService.getInstance());
    }

    public SpawnListener(LegendsConfigurationService configurationService, LegendsPlayerService playerService) {
        this.configurationService = configurationService;
        this.playerService = playerService;
        this.forceSpawnOnJoin = false;
        this.forceSpawnOnFirstJoin = true;
        this.forceSpawnOnRespawn = true;
    }

    public Location getSpawnLocationFor(Player player) {
        LegendsPlayer legendsPlayer = playerService.getLegendsPlayer(player.getUniqueId());
        ServerConfiguration configuration = configurationService.getServerConfiguration();
        return configuration.getSpawnLocationFor(legendsPlayer.getRace());
    }

    @EventHandler
    public void onPlayerSpawnLocationEvent(PlayerSpawnLocationEvent event) {
        if (forceSpawnOnJoin || (forceSpawnOnFirstJoin && !event.getPlayer().hasPlayedBefore())) {
            Location spawn = getSpawnLocationFor(event.getPlayer());
            event.setSpawnLocation(spawn);
        }
    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        if (forceSpawnOnRespawn) {
            Location spawn = getSpawnLocationFor(event.getPlayer());
            event.setRespawnLocation(spawn);
        }
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        LegendsPlayer player = playerService.getLegendsPlayer(event.getPlayer().getUniqueId());
        if (player.getRace() == null) {
            player.teleport(getSpawnLocationFor(player));
        }
    }

    @EventHandler
    public void onMonolithEntityRaceChangedEvent(MonolithEntityRaceChangedEvent event) {
        if (event.getEntity() instanceof MonolithPlayer) {
            if (!event.getEntity().getWorld().getName().equals("Ouros_the_end")) {
                LegendsPlayer player = playerService.getLegendsPlayer(event.getEntity().getUniqueId());
                player.teleport(getSpawnLocationFor(player));
            }
        }
    }
}
