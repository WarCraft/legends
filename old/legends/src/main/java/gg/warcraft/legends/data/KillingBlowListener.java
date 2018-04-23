package gg.warcraft.legends.data;

import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.legends.player.LegendsPlayerService;
import gg.warcraft.monolith.combat.DamageService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class KillingBlowListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        boolean wasKIA = DataService.getInstance().wasKIA(event.getEntity().getUniqueId(), true);
        UUID lastDamager = DamageService.getInstance().getLastDamager(event.getEntity().getUniqueId());
        if (lastDamager != null) {
            LegendsPlayer lastDamagerAsPlayer = LegendsPlayerService.getInstance().getLegendsPlayer(lastDamager);
            if (lastDamagerAsPlayer != null) {
                if (!wasKIA) {
                    DataService.getInstance().addKillingBlow(lastDamager);
                }
            } // else was a mob
        } // natural death
    }
}
