package gg.warcraft.legends.combat;

import gg.warcraft.legends.event.LegendsPlayerSpecializationChangedEvent;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.legends.player.LegendsPlayerService;
import gg.warcraft.monolith.util.RunnableUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

public class NeverEndingQuiverListener implements Listener {
    private final LegendsPlayerService playerService;

    public NeverEndingQuiverListener(LegendsPlayerService playerService) {
        this.playerService = playerService;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityShootBowEvent(EntityShootBowEvent event) {
        LegendsPlayer player = playerService.getLegendsPlayer(event.getEntity().getUniqueId());
        if (player == null) {
            return;
        }

        RunnableUtils.runOnNextTick(() -> {
            if (!player.giveItem(new ItemStack(Material.ARROW), false)) {
                System.out.println("Failed to give Ranger a new arrow, inventory was full! (Should this ever happen? Player probably has more than 1 arrow already)");
            }
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLegendsPlayerSpecializationChangedEvent(LegendsPlayerSpecializationChangedEvent event) {
        LegendsPlayer player = event.getPlayer();
        switch (player.getSpecialization()) {
            case MARKSMAN:
            case HUNTER:
                if (!player.getInventory().containsAtLeast(new ItemStack(Material.ARROW), 1)) {
                    if (!player.giveItem(new ItemStack(Material.ARROW), false)) {
                        player.sendMessage(ChatColor.YELLOW + "Whoops we could not give you an unlimited arrow as your inventory was full! Please contact a member of staff for your very own special arrow.");
                        System.out.println("Failed to give " + player.getName() + " a new arrow AFTER CHANGING SPEC, inventory was full!");
                    }
                }
                break;
        }
    }
}
