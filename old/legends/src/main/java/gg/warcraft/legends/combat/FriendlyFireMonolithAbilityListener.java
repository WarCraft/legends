package gg.warcraft.legends.combat;

import gg.warcraft.monolith.combat.event.MonolithTargetedAbilityCastEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class FriendlyFireMonolithAbilityListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onMonolithTargetedAbilityCastEvent(MonolithTargetedAbilityCastEvent event) {
        if (event.getCaster().getRace() == event.getTarget().getRace()) {
            event.setCancelled(true);
        }
    }
}
