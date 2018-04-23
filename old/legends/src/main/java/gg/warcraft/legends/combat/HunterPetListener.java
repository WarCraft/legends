package gg.warcraft.legends.combat;

import gg.warcraft.legends.event.LegendsPlayerSpecializationChangedEvent;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.legends.player.LegendsPlayerService;
import gg.warcraft.monolith.Monolith;
import gg.warcraft.monolith.ai.CombatPetMaker;
import gg.warcraft.monolith.combat.RangerProjectileEvent;
import gg.warcraft.monolith.entity.SimpleMonolithEntity;
import gg.warcraft.monolith.event.MonolithEntityRaceChangedEvent;
import gg.warcraft.monolith.event.MonolithPlayerConnectEvent;
import gg.warcraft.monolith.event.MonolithPlayerDisconnectEvent;
import gg.warcraft.monolith.event.ServerShutdownEvent;
import gg.warcraft.monolith.player.MonolithPlayer;
import gg.warcraft.monolith.util.RunnableUtils;
import gg.warcraft.monolith.util.TimeUtils;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.Map;

public class HunterPetListener implements Listener {
    private static final int BASE_PET_HEALTH = 40;
    private static final MobDisguise orcPetDisguise = new MobDisguise(DisguiseType.PIG);

    private final LegendsPlayerService playerService;
    final Map<LegendsPlayer, Tameable> pets;
    final Map<Tameable, LegendsPlayer> petToOwner;

    public HunterPetListener(LegendsPlayerService playerService) {
        this.playerService = playerService;
        this.pets = new HashMap<>();
        this.petToOwner = new HashMap<>();
    }

    private void spawnPet(LegendsPlayer player) {
        Tameable pet = null;
        switch (player.getSpecialization()) {
            case MARKSMAN:
            case HUNTER:
                switch (player.getRace()) {
                    case HUMAN: {
                        World world = Monolith.getInstance().getWorld();
                        Wolf wolf = (Wolf) world.spawnEntity(player.getLocation(), EntityType.WOLF);
                        wolf.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(BASE_PET_HEALTH);
                        wolf.setHealth(BASE_PET_HEALTH);
                        CombatPetMaker.makePet(wolf, player);
                        pet = wolf;
                    }
                    break;
                    case ORC: {
                        World world = Monolith.getInstance().getWorld();
                        Wolf wolf = (Wolf) world.spawnEntity(player.getLocation(), EntityType.WOLF);
                        wolf.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(BASE_PET_HEALTH);
                        wolf.setHealth(BASE_PET_HEALTH);
                        CombatPetMaker.makePet(wolf, player);
                        pet = wolf;

                        DisguiseAPI.disguiseToAll(pet, orcPetDisguise);
                    }
                    break;
                }
                break;
        }
        if (pet == null) {
            return;
        }

        pet.setOwner(player);
        pets.put(player, pet);
        petToOwner.put(pet, player);
    }

    private void removePet(MonolithPlayer player) {
        Tameable pet = pets.remove(player);
        if (pet != null) {
            pet.remove();
            petToOwner.remove(pet);
        }
    }

    private boolean raceOrSpecIsNull(LegendsPlayer player) {
        return player.getRace() == null || player.getSpecialization() == null; // TODO add NULL_OBJECTS for both?
    }

    @EventHandler
    public void onMonolithPlayerConnectEvent(MonolithPlayerConnectEvent event) {
        LegendsPlayer player = playerService.getLegendsPlayer(event.getPlayer().getUniqueId());
        if (!raceOrSpecIsNull(player)) {
            spawnPet(player);
        }
    }

    @EventHandler
    public void onMonolithPlayerDisconnectEvent(MonolithPlayerDisconnectEvent event) {
        removePet(event.getPlayer());
    }

    @EventHandler
    public void onMonolithEntityRaceChangedEvent(MonolithEntityRaceChangedEvent event) {
        if (!(event.getEntity() instanceof LegendsPlayer)) {
            return;
        }
        System.out.println("Player changed race");

        LegendsPlayer player = (LegendsPlayer) event.getEntity();
        if (raceOrSpecIsNull(player)) {
            System.out.println("Race or spec was null");
            return;
        }

        switch (player.getSpecialization()) {
            case MARKSMAN:
            case HUNTER:
                removePet(player);
                spawnPet(player);
                break;
        }
    }

    @EventHandler
    public void onLegendsPlayerSpecializationChangedEvent(LegendsPlayerSpecializationChangedEvent event) {
        LegendsPlayer player = event.getPlayer();
        System.out.println("Player changed spec");
        if (raceOrSpecIsNull(player)) {
            System.out.println("Race or spec was null");
            return;
        }

        if (player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }

        switch (player.getSpecialization()) {
            case MARKSMAN:
            case HUNTER:
                if (!pets.containsKey(player)) {
                    spawnPet(player);
                }
                break;
            default:
                removePet(player);
                break;
        }
    }

    @EventHandler
    public void onRangerProjectileEvent(RangerProjectileEvent event) {
        if (!event.getEvent().isCancelled()) {
            if (event.getCaster() instanceof LegendsPlayer) {
                Tameable pet = pets.get(event.getCaster());
                if (pet != null && pet instanceof Wolf) {
                    if (event.getTarget() instanceof SimpleMonolithEntity) {
                        ((Wolf) pet).setTarget(((SimpleMonolithEntity) event.getTarget()).getLivingEntity());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event) {
        LegendsPlayer petOwner = petToOwner.get(event.getEntity());
        if (petOwner != null) {
            RunnableUtils.runLater(() -> spawnPet(petOwner), TimeUtils.TICKS_PER_SECOND * 30);

            // prevent combat pets from dropping anything
            event.setDroppedExp(0);
            event.getDrops().clear();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleportEvent(PlayerTeleportEvent event) {
        Tameable pet = pets.get(LegendsPlayerService.getInstance().getLegendsPlayer(event.getPlayer().getUniqueId()));
        if (pet != null && !pet.isDead()) {
            RunnableUtils.runOnNextTick(() -> {
                pet.teleport(event.getPlayer().getLocation());
            });
        }
    }

    @EventHandler
    public void onEntityTeleportEvent(EntityTeleportEvent event) {
        if (event.getEntity() instanceof Player) {
            return;
        }

        if (event.getEntity() instanceof Wolf) {
            ((Wolf) event.getEntity()).setTarget(null);
        }
    }

    @EventHandler
    public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent event) {
        LegendsPlayer player = LegendsPlayerService.getInstance().getLegendsPlayer(event.getPlayer().getUniqueId());
        removePet(player);
        if (event.getNewGameMode() == GameMode.SURVIVAL) {
            spawnPet(player);
        }
    }

    @EventHandler
    public void onServerShutdownEvent(ServerShutdownEvent event) {
        pets.values().forEach(Tameable::remove);
    }

    // TODO tp out of range pets back to owner via pet.getEntity().teleport(AbstractLocation)
}
