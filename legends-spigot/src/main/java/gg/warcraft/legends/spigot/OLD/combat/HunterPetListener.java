package gg.warcraft.legends.spigot.OLD.combat;

import com.google.common.eventbus.Subscribe;
import gg.warcraft.combat.api.specialization.Specialization;
import gg.warcraft.combat.api.specialization.event.PlayerSpecializationChangedEvent;
import gg.warcraft.legends.api.ai.CombatPetFactory;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.monolith.api.combat.CombatFactory;
import gg.warcraft.monolith.api.combat.CombatSource;
import gg.warcraft.monolith.api.combat.value.CombatValue;
import gg.warcraft.monolith.api.combat.value.CombatValueModifier;
import gg.warcraft.monolith.api.combat.value.CombatValueModifierType;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.core.event.ServerShutdownEvent;
import gg.warcraft.monolith.api.entity.event.EntityDamageEvent;
import gg.warcraft.monolith.api.entity.event.EntityPreDamageEvent;
import gg.warcraft.monolith.api.entity.event.EntityTeamChangedEvent;
import gg.warcraft.monolith.api.entity.player.event.PlayerConnectEvent;
import gg.warcraft.monolith.api.entity.player.event.PlayerDisconnectEvent;
import gg.warcraft.monolith.api.entity.team.service.TeamCommandService;
import gg.warcraft.monolith.api.util.TimeUtils;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import org.bukkit.GameMode;
import org.bukkit.Server;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class HunterPetListener implements Listener {
    private static final int BASE_PET_HEALTH = 40;
    private static final MobDisguise orcPetDisguise = new MobDisguise(DisguiseType.PIG);

    private final Server server;
    private final LegendsPlayerQueryService legendsPlayerQueryService;
    private final TeamCommandService teamCommandService;
    private final CombatPetFactory combatPetFactory;
    private final CombatFactory combatFactory;
    private final TaskService taskService;
    private final TimeUtils timeUtils;

    final Map<UUID, Tameable> uuidToTameable;
    final Map<UUID, Tameable> pets;
    final Map<Tameable, UUID> petToOwner;
    final Map<Tameable, UUID> targets;
    final Map<UUID, Set<Tameable>> targetToPets;

    public HunterPetListener(Server server, LegendsPlayerQueryService legendsPlayerQueryService,
                             TeamCommandService teamCommandService, CombatPetFactory combatPetFactory,
                             TaskService taskService, TimeUtils timeUtils, CombatFactory combatFactory) {
        this.server = server;
        this.legendsPlayerQueryService = legendsPlayerQueryService;
        this.teamCommandService = teamCommandService;
        this.combatPetFactory = combatPetFactory;
        this.taskService = taskService;
        this.timeUtils = timeUtils;
        this.combatFactory = combatFactory;
        this.uuidToTameable = new HashMap<>();
        this.pets = new HashMap<>();
        this.petToOwner = new HashMap<>();
        this.targets = new HashMap<>();
        this.targetToPets = new HashMap<>();
    }

    private void spawnPet(LegendsPlayer player) {
        Tameable pet = null;
        Player spigotPlayer = server.getPlayer(player.getId());
        Specialization specialization = player.getSpecialization();
        if (specialization == null) {
            return;
        }

        switch (specialization.getName()) {
            case "Marksman":
            case "Hunter":
                LegendsTeam team = player.getTeam();
                switch (team) {
                    case HUMAN: {
                        World world = spigotPlayer.getLocation().getWorld();
                        Wolf wolf = (Wolf) world.spawnEntity(spigotPlayer.getLocation(), EntityType.WOLF);
                        wolf.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(BASE_PET_HEALTH);
                        wolf.setHealth(BASE_PET_HEALTH);
                        wolf.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(2);
                        combatPetFactory.create(wolf.getUniqueId(), spigotPlayer.getUniqueId());
                        pet = wolf;
                        teamCommandService.setTeam(LegendsTeam.HUMAN, wolf.getUniqueId());
                    }
                    break;
                    case ORC: {
                        World world = spigotPlayer.getLocation().getWorld();
                        Wolf wolf = (Wolf) world.spawnEntity(spigotPlayer.getLocation(), EntityType.WOLF);
                        wolf.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(BASE_PET_HEALTH);
                        wolf.setHealth(BASE_PET_HEALTH);
                        wolf.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(2);
                        combatPetFactory.create(wolf.getUniqueId(), spigotPlayer.getUniqueId());
                        pet = wolf;
                        teamCommandService.setTeam(LegendsTeam.ORC, wolf.getUniqueId());

                        DisguiseAPI.disguiseToAll(pet, orcPetDisguise);
                    }
                    break;
                }
                break;
        }
        if (pet == null) {
            return;
        }

        pet.setOwner(spigotPlayer);
        uuidToTameable.put(pet.getUniqueId(), pet);
        pets.put(player.getId(), pet);
        petToOwner.put(pet, player.getId());
    }

    private void removePet(UUID playerId) {
        Tameable pet = pets.remove(playerId);
        if (pet != null) {
            uuidToTameable.remove(pet.getUniqueId());
            pet.remove();
            petToOwner.remove(pet);
            UUID target = targets.remove(pet);
            if (target != null) {
                targetToPets.get(target).remove(pet);
            }
        }
    }

    private boolean raceOrSpecIsNull(LegendsPlayer player) {
        return player.getTeam() == null || player.getSpecialization() == null; // TODO add NULL_OBJECTS for both?
    }

    @Subscribe
    public void onPlayerConnectEvent(PlayerConnectEvent event) {
        LegendsPlayer player = legendsPlayerQueryService.getPlayer(event.getPlayerId());
        if (!raceOrSpecIsNull(player)) {
            spawnPet(player);
        }
    }

    @Subscribe
    public void onPlayerDisconnectEvent(PlayerDisconnectEvent event) {
        removePet(event.getPlayerId());
    }

    @Subscribe
    public void onPlayerTeamChangedEvent(EntityTeamChangedEvent event) {
        if (event.getEntityType() != gg.warcraft.monolith.api.entity.EntityType.PLAYER) {
            return;
        }

        LegendsPlayer player = legendsPlayerQueryService.getPlayer(event.getEntityId());
        System.out.println("Player changed race");

        if (raceOrSpecIsNull(player)) {
            System.out.println("Race or spec was null");
            return;
        }

        switch (player.getSpecialization().getName()) {
            case "Marksman":
            case "Hunter":
                removePet(player.getId());
                spawnPet(player);
                break;
        }
    }

    @Subscribe
    public void onPlayerSpecializationChangedEvent(PlayerSpecializationChangedEvent event) {
        LegendsPlayer player = legendsPlayerQueryService.getPlayer(event.getPlayerId());
        System.out.println("Player changed spec");
        if (raceOrSpecIsNull(player)) {
            System.out.println("Race or spec was null");
            return;
        }

        if (player.getGameMode() != gg.warcraft.monolith.api.entity.player.GameMode.SURVIVAL) {
            return;
        }

        switch (player.getSpecialization().getName()) {
            case "Marksman":
            case "Hunter":
                if (!pets.containsKey(player.getId())) {
                    spawnPet(player);
                }
                break;
            default:
                removePet(player.getId());
                break;
        }
    }

    @Subscribe
    public void onEntityPreDamageEvent(EntityPreDamageEvent event) {
        CombatSource source = event.getDamage().getSource();
        if (source == null) {
            return;
        }

        UUID attackerId = source.getEntityId();
        if (attackerId == null) {
            return;
        }

        Tameable pet = pets.get(attackerId);
        if (pet == null) {
            Tameable attackingPet = uuidToTameable.get(attackerId);
            if (attackingPet != null) {
                UUID attackingOwner = petToOwner.get(attackingPet);
                CombatSource ownerSource = combatFactory.createCombatSource("Pet", attackingOwner);
                CombatValueModifier petDamageLimiter = combatFactory.createCombatValueModifier(
                        CombatValueModifierType.OVERRIDE, 2f, ownerSource);
                List<CombatValueModifier> newModifiers = event.getDamage().getModifiers();
                newModifiers.add(petDamageLimiter);
                CombatValue ownerDamage = combatFactory.createCombatValue(event.getDamage().getBaseValue(),
                        newModifiers, ownerSource);
                event.setDamage(ownerDamage);
            }
        }
    }

    @Subscribe
    public void onEntityDamageEvent(EntityDamageEvent event) {
        CombatSource source = event.getDamage().getSource();
        if (source == null) {
            return;
        }

        UUID attackerId = source.getEntityId();
        if (attackerId == null) {
            return;
        }

        Tameable pet = pets.get(attackerId);
        if (pet instanceof Wolf) {
            LegendsPlayer attackedPlayer = legendsPlayerQueryService.getPlayer(event.getEntityId());
            if (attackedPlayer == null) {
                return;
            }

            Player spigotAttackedPlayer = server.getPlayer(attackedPlayer.getId());
            ((Wolf) pet).setTarget(spigotAttackedPlayer);
            UUID previousTarget = targets.put(pet, spigotAttackedPlayer.getUniqueId());
            if (previousTarget != spigotAttackedPlayer.getUniqueId()) {
                if (previousTarget != null) {
                    Set<Tameable> previousTargetPets = targetToPets.computeIfAbsent(previousTarget, id -> new HashSet<>());
                    previousTargetPets.remove(pet);
                }
                Set<Tameable> petsWithTarget = targetToPets.computeIfAbsent(spigotAttackedPlayer.getUniqueId(), id -> new HashSet<>());
                petsWithTarget.add(pet);
            }
        }
    }

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event) {
        Set<Tameable> petsWithTarget = targetToPets.getOrDefault(event.getEntity().getUniqueId(), Collections.emptySet());
        petsWithTarget.forEach(pet -> {
            Wolf wolfPet = ((Wolf) pet);
            wolfPet.setTarget(null);
            targets.remove(wolfPet);
        });

        if (!(event.getEntity() instanceof Tameable)) {
            return;
        }

        UUID petOwnerId = petToOwner.get((Tameable) event.getEntity());
        if (petOwnerId != null) {
            LegendsPlayer petOwner = legendsPlayerQueryService.getPlayer(petOwnerId);
            if (petOwner != null) {
                taskService.runLater(() -> spawnPet(petOwner), timeUtils.createDurationInSeconds(30));
            }

            // prevent combat pets from dropping anything
            event.setDroppedExp(0);
            event.getDrops().clear();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleportEvent(PlayerTeleportEvent event) {
        Tameable pet = pets.get(event.getPlayer().getUniqueId());
        if (pet != null && !pet.isDead()) {
            taskService.runNextTick(() -> pet.teleport(event.getPlayer().getLocation()));
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
        removePet(event.getPlayer().getUniqueId());
        if (event.getNewGameMode() == GameMode.SURVIVAL) {
            LegendsPlayer player = legendsPlayerQueryService.getPlayer(event.getPlayer().getUniqueId());
            spawnPet(player);
        }
    }

    @Subscribe
    public void onServerShutdownEvent(ServerShutdownEvent event) {
        pets.values().forEach(Tameable::remove);
    }

    // TODO tp out of range pets back to owner via pet.getEntity().teleport(AbstractLocation)
}
