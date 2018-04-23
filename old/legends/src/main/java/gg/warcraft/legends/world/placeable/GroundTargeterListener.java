package gg.warcraft.legends.world.placeable;

import gg.warcraft.monolith.event.EventService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class GroundTargeterListener implements Listener {
    private static final String METADATA_KEY = GroundTargeter.class.getCanonicalName();

    public GroundTargeterListener() {
        this(EventService.getInstance());
    }

    public GroundTargeterListener(EventService eventService) {
        eventService.registerForEvents(this);
    }

    private boolean placeFor(Player player) {
        List<MetadataValue> metadata = player.getMetadata(METADATA_KEY);
        if (!metadata.isEmpty()) {
            GroundTargeter targeter = (GroundTargeter) metadata.get(0).value();
            if (targeter.isValid()) {
                targeter.place();
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean cancelFor(Player player) {
        List<MetadataValue> metadata = player.getMetadata(METADATA_KEY);
        if (!metadata.isEmpty()) {
            GroundTargeter targeter = (GroundTargeter) metadata.get(0).value();
            targeter.cancel();
            return true;
        } else {
            return false;
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        switch (event.getAction()) {
            case LEFT_CLICK_AIR:
            case LEFT_CLICK_BLOCK:
                if (placeFor(event.getPlayer())) {
                    event.setCancelled(true);
                }
                break;
            case RIGHT_CLICK_AIR:
            case RIGHT_CLICK_BLOCK:
                if (cancelFor(event.getPlayer())) {
                    event.setCancelled(true);
                }
                break;
        }
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        if (cancelFor(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent event) {
        cancelFor(event.getPlayer());
    }
}
