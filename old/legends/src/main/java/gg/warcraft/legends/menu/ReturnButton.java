package gg.warcraft.legends.menu;

import gg.warcraft.legends.SpawnListener;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.monolith.event.EventService;
import gg.warcraft.monolith.menu.Button;
import gg.warcraft.monolith.util.MonolithTask;
import gg.warcraft.monolith.util.RunnableUtils;
import gg.warcraft.monolith.util.TimeUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReturnButton extends Button {
    private static final long COOLDOWN = 20 * TimeUtils.MILLIS_PER_MINUTE;
    private static final SpawnListener spawnListener = new SpawnListener();
    private static final Map<UUID, MonolithTask> returnTasks = new HashMap<>();
    private static final Map<UUID, Long> cooldowns = new HashMap<>();
    private static final Map<UUID, Location> startLocations = new HashMap<>();

    static {
        EventService.getInstance().registerForEvents(new Listener() {
            @EventHandler
            public void onPlayerMoveEvent(PlayerMoveEvent event) {
                if (!returnTasks.containsKey(event.getPlayer().getUniqueId())) {
                    return;
                }
                if (startLocations.get(event.getPlayer().getUniqueId()).distanceSquared(event.getTo()) < 0.1) {
                    return;
                }
                returnTasks.remove(event.getPlayer().getUniqueId()).cancel();
                event.getPlayer().removePotionEffect(PotionEffectType.CONFUSION);
                event.getPlayer().sendMessage(ChatColor.YELLOW + "The grasp fades.");
            }
        });
    }

    private final LegendsPlayer player;
    private String cooldownText;

    public ReturnButton(LegendsPlayer player) {
        super(Material.FLINT, "Shard of the Monolith");
        this.player = player;

        List<String> lore = new ArrayList<>();
        lore.add("A pitch black splinter that is said to have");
        lore.add("once been part of The Monolith. It can");
        lore.add("take you back to where The Monolith's");
        lore.add("power was at its strongest.");
        lore.add("");
        long cooldown = cooldowns.getOrDefault(player.getUniqueId(), 0L);
        if (cooldown < System.currentTimeMillis()) {
            lore.add(ChatColor.GREEN + "[SHIFT RIGHT CLICK] to return to your spawn");
        } else {
            cooldownText = (int) ((double) (cooldown - System.currentTimeMillis()) / ((double) 60000)) + "";
            lore.add(ChatColor.RED + cooldownText + " minutes cooldown remaining");
        }
        setLoreStrict(lore);
    }

    public void forceTeleportInstantly() {
        Location spawn = spawnListener.getSpawnLocationFor(player);
        player.teleport(spawn);
    }

    public void teleport() {
        UUID playerId = player.getUniqueId();
        if (returnTasks.containsKey(playerId)) {
            return;
        }

        if (cooldowns.getOrDefault(playerId, 0L) > System.currentTimeMillis()) {
            player.sendMessage(ChatColor.YELLOW + "Sorry, that action is not yet available. Please wait " + cooldownText + " minutes.");
            return;
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 15 * TimeUtils.TICKS_PER_SECOND, 2));
        player.sendMessage(ChatColor.YELLOW + "You can feel The Monolith's energy reaching out for you as it pulls you into The Realm's Weave.");

        MonolithTask returnTask = RunnableUtils.runLater(() -> {
            cooldowns.put(playerId, System.currentTimeMillis() + COOLDOWN);
            Location spawn = spawnListener.getSpawnLocationFor(player);
            player.teleport(spawn);
            returnTasks.remove(playerId);
            startLocations.remove(playerId);
        }, 10 * TimeUtils.TICKS_PER_SECOND);
        returnTasks.put(playerId, returnTask);
        startLocations.put(playerId, player.getLocation());
        RunnableUtils.runOnNextTick(player::closeInventory);
    }

    @Override
    public void click(InventoryClickEvent event) {
        if (event.isShiftClick() && event.isRightClick()) {
            if (event.getWhoClicked().getUniqueId() != player.getUniqueId()) {
                player.sendMessage(ChatColor.YELLOW + event.getWhoClicked().getName() + " is trying to teleport you back to spawn.");
            }
            teleport();
        }
    }
}
