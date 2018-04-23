package gg.warcraft.legends.objective;

import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.legends.player.LegendsPlayerService;
import gg.warcraft.monolith.combat.DamageService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class KillingBlowListener implements Listener {

    private final Scoreboard scoreboard;

    public KillingBlowListener(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        UUID lastDamager = DamageService.getInstance().getLastDamager(event.getEntity().getUniqueId());
        if (lastDamager != null) {
            LegendsPlayer lastDamagerAsPlayer = LegendsPlayerService.getInstance().getLegendsPlayer(lastDamager);
            if (lastDamagerAsPlayer != null) {
                LegendsPlayer killedAsPlayer = LegendsPlayerService.getInstance().getLegendsPlayer(event.getEntity().getUniqueId());
                if (killedAsPlayer.getRace() != lastDamagerAsPlayer.getRace()) { // should never be possible not to be but just in case
                    int points = DefaultLegendsObjectiveService.skirmishActive ? 3 : 1;
                    scoreboard.addScore(lastDamagerAsPlayer.getRace(), points);
                }
            } // else was a mob
        } // natural death
    }
}
