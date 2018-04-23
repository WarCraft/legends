package gg.warcraft.legends.item;

import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.legends.player.LegendsPlayerService;
import gg.warcraft.monolith.Monolith;
import gg.warcraft.monolith.combat.DamageService;
import gg.warcraft.monolith.event.EventService;
import gg.warcraft.monolith.util.TimeUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class ItemDropper implements CommandExecutor {
    private static final Random random = new Random(System.currentTimeMillis());
    private static final World world = Monolith.getInstance().getWorld();
    private static final Map<UUID, UUID> reservedFor = new HashMap<>();
    private static final Map<UUID, Long> reservedUntil = new HashMap<>();
    private static final int RESERVE_DURATION = TimeUtils.MILLIS_PER_MINUTE;

    static {
        EventService.getInstance().registerForEvents(new Listener() {
            @EventHandler
            public void onPlayerItemPickupEvent(PlayerPickupItemEvent event) {
                UUID itemId = event.getItem().getUniqueId();
                boolean stillReserved = reservedUntil.getOrDefault(itemId, 0L) > System.currentTimeMillis();
                if (stillReserved) {
                    if (!reservedFor.get(itemId).equals(event.getPlayer().getUniqueId())) {
                        event.setCancelled(true);
                    }
                } else {
                    reservedUntil.remove(itemId);
                    reservedFor.remove(itemId);
                }
            }
        });
    }

    private int getGoldAmount(String mobType) {
        switch (mobType) {
            case "Dunewalker":
            case "Charred":
                return random.nextInt(3);
            case "Elemental":
            case "Rider":
                return random.nextInt(5) + 5;
            default:
                return 0;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof ConsoleCommandSender)) {
            return false;
        }
        if (args.length != 5) {
            return false;
        }

        double x;
        double y;
        double z;

        String mobType;
        UUID mobId;
        try {
            x = Double.parseDouble(args[2]);
            y = Double.parseDouble(args[3]);
            z = Double.parseDouble(args[4]);

            mobType = args[0];
            mobId = UUID.fromString(args[1]);
        } catch (Exception ex) {
            System.out.println("Failed to drop items for " + Arrays.toString(args));
            return false;
        }

        Set<UUID> damagers = DamageService.getInstance().getDamagers(mobId);
        damagers.forEach(damager -> {
            LegendsPlayer player = LegendsPlayerService.getInstance().getLegendsPlayer(damager);
            if (player == null) {
                return; // damage was probably a pet or another mob
            }

            ItemStack drop = ItemService.getInstance().generateEquipment(player, false);
            Location dropLocation = new Location(world, x, y, z);
            Item droppedItem = world.dropItemNaturally(dropLocation, drop);

            Item secondDroppedItem = null;
            if (mobType.equals("Rider") || mobType.equals("Elemental")) {
                if (random.nextDouble() >= 0.5) {
                    ItemStack secondDrop = ItemService.getInstance().generateEquipment(player, false);
                    secondDroppedItem = world.dropItemNaturally(dropLocation, secondDrop);
                }
            }

            int goldDropped = getGoldAmount(mobType);
            if (goldDropped > 0) {
                player.addInGameCurrency(goldDropped);
                player.sendMessage(ChatColor.YELLOW + "You looted " + goldDropped + " gold.");
            }

            reservedFor.put(droppedItem.getUniqueId(), damager);
            reservedUntil.put(droppedItem.getUniqueId(), System.currentTimeMillis() + RESERVE_DURATION);

            if (secondDroppedItem != null) {
                reservedFor.put(secondDroppedItem.getUniqueId(), damager);
                reservedUntil.put(secondDroppedItem.getUniqueId(), System.currentTimeMillis() + RESERVE_DURATION);
            }
        });
        return true;
    }
}
