package gg.warcraft.legends.combat;

import gg.warcraft.legends.data.DataService;
import gg.warcraft.monolith.Monolith;
import gg.warcraft.monolith.ai.PlayerSubstituteMaker;
import gg.warcraft.monolith.combat.DamageService;
import gg.warcraft.monolith.entity.EntityService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.event.MonolithPlayerConnectEvent;
import gg.warcraft.monolith.event.MonolithPlayerDisconnectEvent;
import gg.warcraft.monolith.player.MonolithPlayer;
import gg.warcraft.monolith.player.PlayerService;
import gg.warcraft.monolith.util.Duration;
import gg.warcraft.monolith.util.MonolithTask;
import gg.warcraft.monolith.util.RunnableUtils;
import gg.warcraft.monolith.util.TimeUtils;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Husk;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class AntiCombatLogger implements Listener {
    private static final long IN_COMBAT_COOLDOWN = 10 * TimeUtils.MILLIS_PER_SECOND;
    private static final AntiCombatLogger instance = new AntiCombatLogger();

    public static AntiCombatLogger getInstance() {
        return instance;
    }

    private final DamageService damageService = DamageService.getInstance();
    private final World world;
    private final Map<UUID, Player> players;
    private final Map<LivingEntity, UUID> substitutes;
    private final Map<UUID, LivingEntity> substitutesByPlayer;
    private final Map<UUID, Runnable> despawnRunnables;
    private final Map<UUID, MonolithTask> despawnTasks;
    private final Set<UUID> KIA;

    private AntiCombatLogger() {
        this.world = Monolith.getInstance().getWorld();
        this.players = new HashMap<>();
        this.substitutes = new HashMap<>();
        this.substitutesByPlayer = new HashMap<>();
        this.despawnRunnables = new HashMap<>();
        this.despawnTasks = new HashMap<>();
        this.KIA = new HashSet<>();

        RunnableUtils.runTask(() -> {
            substitutes.forEach((key, value) -> {
                if (Bukkit.getOfflinePlayer(value).isOnline()) {
                    key.remove();
                }
            });
        }, 1, 1);
    }

    public Player getSubstitutedPlayerFor(LivingEntity entity) {
        return players.get(substitutes.get(entity));
    }

    @EventHandler
    public void onMonolithPlayerDisconnectEvent(MonolithPlayerDisconnectEvent event) {
        Player player = event.getPlayer().getPlayer();
        UUID playerId = player.getUniqueId();
        long timeOfLastDamaged = damageService.getTimeOfLastHit(playerId);
        if (timeOfLastDamaged + IN_COMBAT_COOLDOWN > System.currentTimeMillis()) {
            players.put(playerId, player);

            Location playerLoc = player.getLocation();
            Husk playerSubstitute = (Husk) world.spawnEntity(playerLoc, EntityType.HUSK);
            PlayerSubstituteMaker.makeSubstitute(playerSubstitute, player);

            AttributeInstance attribute = playerSubstitute.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            attribute.setBaseValue(player.getHealth());
            playerSubstitute.setHealth(player.getHealth());

            PlayerDisguise disguise = new PlayerDisguise(player.getName());
            DisguiseAPI.disguiseToAll(playerSubstitute, disguise);
            substitutes.put(playerSubstitute, playerId);
            substitutesByPlayer.put(playerId, playerSubstitute);

            Runnable despawnRunnable = () -> {
                substitutes.remove(playerSubstitute);
                substitutesByPlayer.remove(playerId);
                if (!playerSubstitute.isDead()) {
                    playerSubstitute.remove();
                }
            };
            despawnRunnables.put(playerId, despawnRunnable);
            resetDespawnTask(playerId);
        }
    }

    private void resetDespawnTask(UUID playerId) {
        long timeOfLastDamaged = damageService.getTimeOfLastHit(playerId);
        Runnable despawnRunnable = despawnRunnables.get(playerId);

        MonolithTask existingTask = despawnTasks.get(playerId);
        if (existingTask != null) {
            existingTask.cancel();
        }

        MonolithTask despawnTask = RunnableUtils.runLater(() -> {
            if (despawnRunnable != null) { // TODO why can this be null?
                despawnRunnable.run();
                despawnRunnables.remove(playerId);
            }
        }, new Duration(timeOfLastDamaged + IN_COMBAT_COOLDOWN - System.currentTimeMillis(), Duration.Type.MILLIS));
        despawnTasks.put(playerId, despawnTask);
    }

    @EventHandler
    public void onMonolithPlayerConnectEvent(MonolithPlayerConnectEvent event) {
        Player player = event.getPlayer().getPlayer();
        Runnable despawnRunnable = despawnRunnables.remove(player.getUniqueId());
        if (despawnRunnable != null) {
            player.setHealth(substitutesByPlayer.get(player.getUniqueId()).getHealth());
            despawnRunnable.run();
            despawnTasks.get(player.getUniqueId()).cancel();
        }

        if (DataService.getInstance().wasKIA(event.getPlayer().getUniqueId(), false)) {
            KIA.add(player.getUniqueId());
            player.setHealth(0);
            player.sendMessage(ChatColor.YELLOW + "[Server] You were killed while offline. When you log off within 10 seconds " +
                    "of taking damage a copy will take your place until those 10 seconds have passed.");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        if (KIA.remove(event.getEntity().getUniqueId())) {
            event.setDroppedExp(0);
            event.getDrops().clear();
        }
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            UUID playerId = substitutes.get(event.getEntity());
            if (playerId == null) {
                return; // damaged entity was not a substitute
            }

            UUID damagerId;
            MonolithPlayer damagerPlayer = PlayerService.getInstance().getMonolithPlayer(event.getDamager().getUniqueId());
            if (damagerPlayer != null) {
                damagerId = damagerPlayer.getUniqueId();
            } else if (event.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getDamager();
                if (arrow.getShooter() instanceof LivingEntity) {
                    MonolithEntity caster = EntityService.getInstance().getMonolithEntityFor((LivingEntity) arrow.getShooter());
                    if (!(caster instanceof MonolithPlayer)) {
                        return; // arrow was not fired by a player
                    } else {
                        damagerId = caster.getUniqueId();
                    }
                } else {
                    return; // no idea
                }
            } else {
                return; // no idea
            }

            DamageService.getInstance().setLastDamager(playerId, damagerId);
            resetDespawnTask(playerId);
        }
    }

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event) {
        UUID playerId = substitutes.remove(event.getEntity());
        if (playerId == null) {
            return;
        }

        substitutesByPlayer.remove(playerId);
        Runnable despawnRunnable = despawnRunnables.remove(playerId);
        if (despawnRunnable != null) {
            despawnTasks.get(playerId).cancel();
        }

        UUID lastDamager = DamageService.getInstance().getLastDamager(playerId);
        if (lastDamager != null) {
            MonolithPlayer killer = PlayerService.getInstance().getMonolithPlayer(lastDamager);
            if (killer != null) {
                DataService.getInstance().addKillingBlow(killer.getUniqueId());
            }
        }

        DataService.getInstance().addKIA(playerId);
        event.setDroppedExp(0);
        event.getDrops().clear();
        Location dropLocation = event.getEntity().getLocation();
        Player player = players.get(playerId);
        if (player == null) {
            System.out.println("COULD NOT FIND PLAYER IN PLAYERS MAP IN ANTI COMBAT LOGGING PLUGIN");
            return;
        }
        PlayerInventory playerInventory = player.getInventory();
        ListIterator<ItemStack> iterator = playerInventory.iterator();
        while (iterator.hasNext()) {
            ItemStack item = iterator.next();
            if (item != null) {
                world.dropItemNaturally(dropLocation, item);
            }
        }
    }
}
