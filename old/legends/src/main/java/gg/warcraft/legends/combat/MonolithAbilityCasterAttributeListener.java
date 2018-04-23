package gg.warcraft.legends.combat;

import gg.warcraft.legends.entity.LegendsAttribute;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.legends.player.LegendsPlayerService;
import gg.warcraft.monolith.combat.event.MonolithEntityDamageEvent;
import gg.warcraft.monolith.entity.Attribute;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.util.Random;

public class MonolithAbilityCasterAttributeListener implements Listener {
    private static final Random random = new Random(System.currentTimeMillis());

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMonolithEntityDamageEvent(MonolithEntityDamageEvent event) {
        if (event.getDamager() instanceof LegendsPlayer) {
            LegendsPlayer caster = (LegendsPlayer) event.getDamager();
            Attribute primaryAttribute = caster.getSpecialization().getPrimaryAttribute();
            double primaryAttributeStat = caster.getAttributeValue(primaryAttribute);
            double critStat = caster.getAttributeValue(LegendsAttribute.CRIT);

            double damage = event.getDamage();
            damage += primaryAttributeStat * 0.1;
            if (random.nextDouble() <= critStat * 0.01) {
                damage *= 1.5;
            }
            event.setDamage(damage);
        }
    }

    @EventHandler
    public void onPlayerArrowFireEvent(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            LegendsPlayer player = LegendsPlayerService.getInstance().getLegendsPlayer(event.getEntity().getUniqueId());
            switch (player.getSpecialization()) {
                case MARKSMAN:
                case HUNTER:
                    break;
                default:
                    event.setCancelled(true);
                    player.getInventory().remove(Material.ARROW);
                    player.sendMessage(ChatColor.YELLOW + "You clumsily attempt to fire the bow, you failed. (Bows can only be used by Ranger specializations)");
            }
        }
    }
}
