package gg.warcraft.legends.menu.objective;

import gg.warcraft.legends.objective.AbstractCapturableObjective;
import gg.warcraft.legends.objective.DefaultLegendsObjectiveService;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.monolith.Monolith;
import gg.warcraft.monolith.entity.Race;
import gg.warcraft.monolith.event.EventService;
import gg.warcraft.monolith.menu.Button;
import gg.warcraft.monolith.menu.ButtonBuilder;
import gg.warcraft.monolith.menu.ButtonSlot;
import gg.warcraft.monolith.menu.Menu;
import gg.warcraft.monolith.objective.Objective;
import gg.warcraft.monolith.util.MonolithTask;
import gg.warcraft.monolith.util.RunnableUtils;
import gg.warcraft.monolith.util.TimeUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HumanObjectiveMenu extends Menu {
    private static final long COOLDOWN = 10 * TimeUtils.MILLIS_PER_MINUTE;
    private static final long SKIRMISH_COOLDOWN = 3 * TimeUtils.MILLIS_PER_MINUTE;
    private static final Map<UUID, Long> cooldowns = new HashMap<>();
    private static final Map<UUID, MonolithTask> returnTasks = new HashMap<>();
    private static final Map<UUID, Location> startLocations = new HashMap<>();

    private static final Location capitalLocation;
    private static final Location eastKeepLocation;
    private static final Location westKeepLocation;
    private static final Location orcOutpostLocation;
    private static final Location farmLocation;
    private static final Location millLocation;
    private static final Location mineLocation;

    static {
        World world = Monolith.getInstance().getWorld();
        capitalLocation = new Location(world, -1554.5, 55, 357.5);
        capitalLocation.setDirection(new Vector(1, 0, 0));

        eastKeepLocation = new Location(world, -993.5, 67, 72.5);
        eastKeepLocation.setDirection(new Vector(-1, 0, 0));

        westKeepLocation = new Location(world, -1563.5, 47, -232.5);
        westKeepLocation.setDirection(new Vector(0, 0, -1));

        orcOutpostLocation = new Location(world, -656.5, 39, -164.5);
        orcOutpostLocation.setDirection(new Vector(0, 0, 1));

        farmLocation = new Location(world, -1583.5, 40, -16.5);
        farmLocation.setDirection(new Vector(0, 0, 1));

        millLocation = new Location(world, -801.5, 38, 422.5);
        millLocation.setDirection(new Vector(1, 0, 0));

        mineLocation = new Location(world, -1139.5, 47, 490.5);
        mineLocation.setDirection(new Vector(0, 0, -1));

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

    private final DefaultLegendsObjectiveService objectiveService;
    private final LegendsPlayer player;

    public HumanObjectiveMenu(LegendsPlayer viewer) {
        super(viewer, "Objectives");
        this.objectiveService = DefaultLegendsObjectiveService.DO_NOT_USE_objectiveService;
        this.player = viewer;

        Button capital = new ButtonBuilder(Material.BED)
                .setTitle("Capital")
                .setAction(() -> teleportTo(capitalLocation, "HUMAN_CAPITAL"))
                .addLore("")
                .addLore(getMouseClickLore("HUMAN_CAPITAL"))
                .build();

        Button eastKeep = new ButtonBuilder(Material.STONE_SWORD)
                .setTitle("East Keep")
                .setAction(() -> teleportTo(eastKeepLocation, "HUMAN_KEEP_EAST"))
                .addLore("")
                .addLore(getMouseClickLore("HUMAN_KEEP_EAST"))
                .build();

        Button westKeep = new ButtonBuilder(Material.STONE_SWORD)
                .setTitle("West Keep")
                .setAction(() -> teleportTo(westKeepLocation, "HUMAN_KEEP_WEST"))
                .addLore("")
                .addLore(getMouseClickLore("HUMAN_KEEP_WEST"))
                .build();

        Button outpost = new ButtonBuilder(Material.BANNER)
                .setTitle("Outpost")
                .setAction(() -> teleportTo(orcOutpostLocation, "HUMAN_OUTPOST_ORC"))
                .addLore("")
                .addLore(getMouseClickLore("HUMAN_OUTPOST_ORC"))
                .build();

        Button farm = new ButtonBuilder(Material.HAY_BLOCK)
                .setTitle("Farm")
                .setAction(() -> teleportTo(farmLocation, "HUMAN_FARM"))
                .addLore("")
                .addLore(getMouseClickLore("HUMAN_FARM"))
                .build();

        Button mill = new ButtonBuilder(Material.LOG)
                .setTitle("Lumber Mill")
                .setAction(() -> teleportTo(millLocation, "HUMAN_LUMBER_MILL"))
                .addLore("")
                .addLore(getMouseClickLore("HUMAN_LUMBER_MILL"))
                .build();

        Button mine = new ButtonBuilder(Material.STONE)
                .setTitle("Mine")
                .setAction(() -> teleportTo(mineLocation, "HUMAN_MINE"))
                .addLore("")
                .addLore(getMouseClickLore("HUMAN_MINE"))
                .build();

        update(ButtonSlot.FIRST, capital);
        update(ButtonSlot.SECOND, eastKeep);
        update(ButtonSlot.THIRD, westKeep);
        update(ButtonSlot.FOURTH, farm);
        update(ButtonSlot.FIFTH, mill);
        update(ButtonSlot.SIXTH, mine);
        update(ButtonSlot.SEVENTH, outpost);
    }

    private void teleportTo(Location location, String objectiveId) {
        if (getRemainingCooldown() > 0) {
            return;
        }

        if (getObjectiveOccupiers(objectiveId) != player.getRace()) {
            return;
        }

        UUID playerId = player.getUniqueId();
        if (returnTasks.containsKey(playerId)) {
            return;
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 15 * TimeUtils.TICKS_PER_SECOND, 2));
        player.sendMessage(ChatColor.YELLOW + "You can feel The Monolith's energy reaching out for you as it pulls you into The Realm's Weave.");

        MonolithTask returnTask = RunnableUtils.runLater(() -> {
            long activeCooldown = DefaultLegendsObjectiveService.skirmishActive ? SKIRMISH_COOLDOWN : COOLDOWN;
            cooldowns.put(playerId, System.currentTimeMillis() + activeCooldown);
            player.teleport(location);
            returnTasks.remove(playerId);
            startLocations.remove(playerId);
        }, 10 * TimeUtils.TICKS_PER_SECOND);
        returnTasks.put(playerId, returnTask);
        startLocations.put(playerId, player.getLocation());
        RunnableUtils.runOnNextTick(player::closeInventory);
    }

    private long getRemainingCooldown() {
        long cooldown = cooldowns.getOrDefault(player.getUniqueId(), 0L);
        long currentTime = System.currentTimeMillis();
        return cooldown - currentTime;
    }

    private Race getObjectiveOccupiers(String objectiveId) {
        Objective objective = objectiveService.getObjective(objectiveId);
        if (objective == null) {
            return player.getRace();
        }
        Race controllers = ((AbstractCapturableObjective) objective).getOccupiers();
        return controllers;
    }

    private String getRemainingCooldownText(long cooldownRemaining) {
        int minutesRemaining = (int) (cooldownRemaining / TimeUtils.MILLIS_PER_MINUTE);
        if (minutesRemaining == 0) {
            int secondsRemaining = (int) (cooldownRemaining / TimeUtils.MILLIS_PER_SECOND);
            if (secondsRemaining == 1) {
                return "1 second";
            } else {
                return secondsRemaining + " seconds";
            }
        }
        if (minutesRemaining == 1) {
            return "1 minute";
        } else {
            return minutesRemaining + " minutes";
        }
    }

    private String getMouseClickLore(String objectiveId) {
        long cooldownRemaining = getRemainingCooldown();
        if (cooldownRemaining < 0) {
            Race controllers = getObjectiveOccupiers(objectiveId);
            if (controllers == player.getRace()) {
                return ChatColor.GREEN + "[CLICK] to teleport to this location";
            } else {
                if(controllers != null) {
                    return ChatColor.GRAY + "This objective is currently controlled by the " + controllers.getName();
                } else {
                    return ChatColor.GRAY + "This objective is currently not controlled by anyone";
                }
            }
        } else {
            return ChatColor.RED + "You can teleport again in " + getRemainingCooldownText(cooldownRemaining);
        }
    }
}
