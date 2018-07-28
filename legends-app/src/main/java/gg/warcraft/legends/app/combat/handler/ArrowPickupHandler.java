package gg.warcraft.legends.app.combat.handler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupArrowEvent;

public class ArrowPickupHandler implements Listener {

    @EventHandler
    public void onPlayerPickupArrowEvent(PlayerPickupArrowEvent event) { // TODO rework Arrow and other nonliving entities in Monolith into Doodads
        event.setCancelled(true);
    }
}
