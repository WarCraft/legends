package gg.warcraft.legends.combat;

import gg.warcraft.legends.siege.trebuchet.TrebuchetProjectile;
import gg.warcraft.monolith.combat.event.MonolithAbilityCastEvent;
import gg.warcraft.monolith.combat.event.MonolithTargetedAbilityCastEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CapitalAbilityPreventListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (TrebuchetProjectile.isInCapital(event.getDamager().getLocation().getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMonolithAbilityCastEvent(MonolithAbilityCastEvent event) {
        if (TrebuchetProjectile.isInCapital(event.getCaster().getLocation().getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMonolithTargetedAbilityCastEvent(MonolithTargetedAbilityCastEvent event) {
        if (TrebuchetProjectile.isInCapital(event.getCaster().getLocation().getBlock())) {
            event.setCancelled(true);
        }
    }
}
