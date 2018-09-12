package gg.warcraft.legends.spigot.OLD.combat.handler;

import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

public class RangerOnlyBowHandler implements Listener {
    private final LegendsPlayerQueryService legendsPlayerQueryService;

    @Inject
    public RangerOnlyBowHandler(LegendsPlayerQueryService legendsPlayerQueryService) {
        this.legendsPlayerQueryService = legendsPlayerQueryService;
    }

    @EventHandler
    public void onPlayerArrowFireEvent(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            LegendsPlayer player = legendsPlayerQueryService.getPlayer(event.getEntity().getUniqueId());
            switch (player.getSpecialization().getName()) {
                case "Marksman":
                case "Hunter":
                    break;
                default:
                    event.setCancelled(true);
                    Player spigotPlayer = ((Player) event.getEntity());
                    spigotPlayer.getInventory().remove(Material.ARROW);
                    spigotPlayer.sendMessage(ChatColor.YELLOW + "You clumsily attempt to fire the bow, you failed. (Bows can only be used by Ranger specializations)");
            }
        }
    }
}
