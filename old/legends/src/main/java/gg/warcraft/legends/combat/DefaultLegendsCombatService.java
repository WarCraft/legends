package gg.warcraft.legends.combat;

import gg.warcraft.legends.combat.mage.tempest.BoltFactory;
import gg.warcraft.legends.combat.mage.tempest.HailBlastFactory;
import gg.warcraft.legends.combat.mage.tempest.MoltenStormFactory;
import gg.warcraft.legends.combat.mage.wizard.ArcaneMissileFactory;
import gg.warcraft.legends.combat.mage.wizard.MassPiggifyFactory;
import gg.warcraft.legends.combat.mage.wizard.TeleportFactory;
import gg.warcraft.legends.combat.passive.PassiveAssignmentListener;
import gg.warcraft.legends.combat.ranger.hunter.DisengageFactory;
import gg.warcraft.legends.combat.ranger.hunter.ToxicArrowFactory;
import gg.warcraft.legends.combat.ranger.hunter.TrapFactory;
import gg.warcraft.legends.combat.ranger.marksman.FocusFactory;
import gg.warcraft.legends.combat.ranger.marksman.PowerShotFactory;
import gg.warcraft.legends.combat.ranger.marksman.SneakFactory;
import gg.warcraft.legends.combat.ranger.valewalker.BeastWithinFactory;
import gg.warcraft.legends.combat.ranger.valewalker.CamouflageFactory;
import gg.warcraft.legends.combat.warrior.armsmaster.HamstringFactory;
import gg.warcraft.legends.combat.warrior.armsmaster.SlamFactory;
import gg.warcraft.legends.combat.warrior.armsmaster.StandYourGroundFactory;
import gg.warcraft.legends.combat.warrior.berserker.GrudgeFactory;
import gg.warcraft.legends.combat.warrior.berserker.MaulFactory;
import gg.warcraft.legends.combat.warrior.berserker.ShatterFactory;
import gg.warcraft.legends.combat.warrior.harbinger.LifeLeechFactory;
import gg.warcraft.legends.combat.warrior.overlord.HeroicStrikeFactory;
import gg.warcraft.legends.data.KillingBlowListener;
import gg.warcraft.legends.objective.DefaultLegendsObjectiveService;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.legends.player.LegendsPlayerService;
import gg.warcraft.monolith.combat.DefaultCombatService;
import gg.warcraft.monolith.combat.event.MonolithEntityDamageEvent;
import gg.warcraft.monolith.combat.mage.FireballFactory;
import gg.warcraft.monolith.entity.EntityService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.entity.SimpleMonolithEntity;
import gg.warcraft.monolith.event.EventService;
import gg.warcraft.monolith.player.MonolithPlayer;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class DefaultLegendsCombatService extends DefaultCombatService implements LegendsCombatService {

    private final LegendsPlayerService playerService;
    private final EventService eventService;
    private final HunterPetListener hunterPetListener;

    public DefaultLegendsCombatService(EventService eventService, LegendsPlayerService playerService) {
        this.playerService = playerService;
        this.eventService = eventService;
        eventService.registerForEvents(new AbilityCooldownListener(1));
        eventService.registerForEvents(new FriendlyFireMonolithAbilityListener());
        eventService.registerForEvents(new PassiveAssignmentListener(playerService));
        eventService.registerForEvents(new NeverEndingQuiverListener(playerService));
        eventService.registerForEvents(new PreventArrowPickUpListener());
        hunterPetListener = new HunterPetListener(playerService);
        eventService.registerForEvents(hunterPetListener);
        eventService.registerForEvents(new KillingBlowListener());
        eventService.registerForEvents(new MonolithAbilityCasterAttributeListener());
        eventService.registerForEvents(AntiCombatLogger.getInstance());
        eventService.registerForEvents(new CapitalAbilityPreventListener());

        // ------------------------------------------------------------
        // Warrior
        // ------------------------------------------------------------
        // Berserker
        Specialization.BERSERKER.validMainHandWeapons.add(Material.WOOD_AXE);
        Specialization.BERSERKER.validMainHandWeapons.add(Material.STONE_AXE);
        Specialization.BERSERKER.validMainHandWeapons.add(Material.IRON_AXE);
        Specialization.BERSERKER.validMainHandWeapons.add(Material.GOLD_AXE);
        Specialization.BERSERKER.validMainHandWeapons.add(Material.DIAMOND_AXE);
        Specialization.BERSERKER.validOffHandWeapons.add(Material.AIR);
        Specialization.BERSERKER.validOffHandWeapons.add(Material.SKULL_ITEM);
        // [Left] Auto attack, [Shift Left] Grudge, [Right] Maul, [Shift Right] Shatter
        Specialization.BERSERKER.onShiftLeftClickEntity = new GrudgeFactory();
        Specialization.BERSERKER.onShiftRightClickAir = new ShatterFactory();
        Specialization.BERSERKER.onShiftRightClickBlock = new ShatterFactory();
        Specialization.BERSERKER.onRightClickEntity = new MaulFactory();
        Specialization.BERSERKER.onShiftRightClickEntity = new ShatterFactory();

        // Armsmaster
        Specialization.ARMSMASTER.validMainHandWeapons.add(Material.WOOD_SWORD);
        Specialization.ARMSMASTER.validMainHandWeapons.add(Material.STONE_SWORD);
        Specialization.ARMSMASTER.validMainHandWeapons.add(Material.IRON_SWORD);
        Specialization.ARMSMASTER.validMainHandWeapons.add(Material.GOLD_SWORD);
        Specialization.ARMSMASTER.validMainHandWeapons.add(Material.DIAMOND_SWORD);
        Specialization.ARMSMASTER.validOffHandWeapons.add(Material.AIR);
        Specialization.ARMSMASTER.validOffHandWeapons.add(Material.SHIELD);
        // [Left] Auto attack, [Shift Left] Slam, [Right] Hamstring, [Shift Right] Bolster
        Specialization.ARMSMASTER.onShiftLeftClickEntity = new SlamFactory();
        Specialization.ARMSMASTER.onShiftRightClickAir = new StandYourGroundFactory();
        Specialization.ARMSMASTER.onShiftRightClickBlock = new StandYourGroundFactory();
        Specialization.ARMSMASTER.onRightClickEntity = new HamstringFactory();
        Specialization.ARMSMASTER.onShiftRightClickEntity = new StandYourGroundFactory();

        // Harbinger
        Specialization.HARBINGER.validMainHandWeapons.add(Material.WOOD_AXE);
        Specialization.HARBINGER.validMainHandWeapons.add(Material.STONE_AXE);
        Specialization.HARBINGER.validMainHandWeapons.add(Material.IRON_AXE);
        Specialization.HARBINGER.validMainHandWeapons.add(Material.GOLD_AXE);
        Specialization.HARBINGER.validMainHandWeapons.add(Material.DIAMOND_AXE);
        Specialization.HARBINGER.validOffHandWeapons.add(Material.AIR);
        // [Left] Auto attack, [Shift Left] GraspOfTheDead, [Right] LifeLeech, [Shift Right] BoneExplosion
        Specialization.HARBINGER.onShiftLeftClickAir = null; // TODO GraspOfTheDeadFactory();
        Specialization.HARBINGER.onShiftLeftClickBlock = null; // TODO GraspOfTheDeadFactory();
        Specialization.HARBINGER.onShiftLeftClickEntity = null; // TODO GraspOfTheDeadFactory();
        Specialization.HARBINGER.onShiftRightClickAir = null; // TODO BoneExplosionFactory();
        Specialization.HARBINGER.onShiftRightClickBlock = null; // TODO BoneExplosionFactory();
        Specialization.HARBINGER.onRightClickEntity = new LifeLeechFactory();
        Specialization.HARBINGER.onShiftRightClickEntity = null; // TODO BoneExplosionFactory();

        // Overlord
        Specialization.OVERLORD.validMainHandWeapons.add(Material.WOOD_SWORD);
        Specialization.OVERLORD.validMainHandWeapons.add(Material.STONE_SWORD);
        Specialization.OVERLORD.validMainHandWeapons.add(Material.IRON_SWORD);
        Specialization.OVERLORD.validMainHandWeapons.add(Material.GOLD_SWORD);
        Specialization.OVERLORD.validMainHandWeapons.add(Material.DIAMOND_SWORD);
        Specialization.OVERLORD.validOffHandWeapons.add(Material.AIR);
        // [Left] Auto attack, [Shift Left] Boulder, [Right] HeroicStrike, [Shift Right] Rally
        Specialization.OVERLORD.onShiftLeftClickAir = null; // TODO BoulderFactory();
        Specialization.OVERLORD.onShiftLeftClickBlock = null; // TODO BoulderFactory();
        Specialization.OVERLORD.onShiftLeftClickEntity = null; // TODO BoulderFactory();
        Specialization.OVERLORD.onShiftRightClickAir = null; // TODO RallyFactory();
        Specialization.OVERLORD.onShiftRightClickBlock = null; // TODO RallyFactory();
        Specialization.OVERLORD.onRightClickEntity = new HeroicStrikeFactory();
        Specialization.OVERLORD.onShiftRightClickEntity = null; // TODO RallyFactory();

        // ------------------------------------------------------------
        // Ranger
        // ------------------------------------------------------------
        // Marksman
        Specialization.MARKSMAN.validMainHandWeapons.add(Material.BOW);
        Specialization.MARKSMAN.validOffHandWeapons.add(Material.AIR);
        // [Left] Power shot, [Shift Left] Grapple, [Right] Auto attack, [Shift Right] Explosive shot
        Specialization.MARKSMAN.onLeftClickAir = new PowerShotFactory();
        Specialization.MARKSMAN.onShiftLeftClickAir = new FocusFactory();
        Specialization.MARKSMAN.onLeftClickBlock = new PowerShotFactory();
        Specialization.MARKSMAN.onShiftLeftClickBlock = new FocusFactory();
        Specialization.MARKSMAN.onLeftClickEntity = new PowerShotFactory();
        Specialization.MARKSMAN.onShiftLeftClickEntity = new FocusFactory();
        Specialization.MARKSMAN.onShiftRightClickAir = new SneakFactory();
        Specialization.MARKSMAN.onShiftRightClickBlock = new SneakFactory();
        Specialization.MARKSMAN.onShiftRightClickEntity = new SneakFactory();

        // Hunter
        Specialization.HUNTER.validMainHandWeapons.add(Material.BOW);
        Specialization.HUNTER.validOffHandWeapons.add(Material.AIR);
        // [Left] Trap, [Shift Left] Disengage, [Right] Auto attack, [Shift Right] Camouflage
        Specialization.HUNTER.onLeftClickAir = new ToxicArrowFactory();
        Specialization.HUNTER.onShiftLeftClickAir = new TrapFactory();
        Specialization.HUNTER.onLeftClickBlock = new ToxicArrowFactory();
        Specialization.HUNTER.onShiftLeftClickBlock = new TrapFactory();
        Specialization.HUNTER.onLeftClickEntity = new ToxicArrowFactory();
        Specialization.HUNTER.onShiftLeftClickEntity = new TrapFactory();
        Specialization.HUNTER.onShiftRightClickAir = new DisengageFactory();
        Specialization.HUNTER.onShiftRightClickBlock = new DisengageFactory();
        Specialization.HUNTER.onShiftRightClickEntity = new DisengageFactory();

        // Riftstalker
        Specialization.RIFTSTALKER.validMainHandWeapons.add(Material.BOW);
        Specialization.RIFTSTALKER.validOffHandWeapons.add(Material.AIR);
        // [Left] Nerve shot, [Shift Left] Blink arrow, [Right] Auto attack, [Shift Right] Hail of arrows
        Specialization.RIFTSTALKER.onLeftClickAir = null; // TODO NerveShotFactory();
        Specialization.RIFTSTALKER.onShiftLeftClickAir = null; // TODO BlinkArrowFactory();
        Specialization.RIFTSTALKER.onLeftClickBlock = null; // TODO NerveShotFactory();
        Specialization.RIFTSTALKER.onShiftLeftClickBlock = null; // TODO BlinkArrowFactory();
        Specialization.RIFTSTALKER.onLeftClickEntity = null; // TODO NerveShotFactory();
        Specialization.RIFTSTALKER.onShiftLeftClickEntity = null; // TODO BlinkArrowFactory();
        Specialization.RIFTSTALKER.onShiftRightClickAir = null; // TODO HailOfArrowsFactory();
        Specialization.RIFTSTALKER.onShiftRightClickBlock = null; // TODO HailOfArrowsFactory();
        Specialization.RIFTSTALKER.onShiftRightClickEntity = null; // TODO HailOfArrowsFactory();

        // Valewalker
        Specialization.VALEWALKER.validMainHandWeapons.add(Material.BOW);
        Specialization.VALEWALKER.validOffHandWeapons.add(Material.AIR);
        // [Left] Cobra shot, [Shift Left] Mud, [Right] Auto attack, [Shift Right] Beast within
        Specialization.VALEWALKER.onLeftClickAir = null; // TODO CobraShotFactory();
        Specialization.VALEWALKER.onShiftLeftClickAir = new CamouflageFactory();
        Specialization.VALEWALKER.onLeftClickBlock = null; // TODO CobraShotFactory();
        Specialization.VALEWALKER.onShiftLeftClickBlock = new CamouflageFactory();
        Specialization.VALEWALKER.onLeftClickEntity = null; // TODO CobraShotFactory();
        Specialization.VALEWALKER.onShiftLeftClickEntity = new CamouflageFactory();
        Specialization.VALEWALKER.onShiftRightClickAir = new BeastWithinFactory();
        Specialization.VALEWALKER.onShiftRightClickBlock = new BeastWithinFactory();
        Specialization.VALEWALKER.onShiftRightClickEntity = new BeastWithinFactory();

        // ------------------------------------------------------------
        // Mage
        // ------------------------------------------------------------
        // Tempest
        Specialization.TEMPEST.validMainHandWeapons.add(Material.WOOD_HOE);
        Specialization.TEMPEST.validMainHandWeapons.add(Material.STONE_HOE);
        Specialization.TEMPEST.validMainHandWeapons.add(Material.IRON_HOE);
        Specialization.TEMPEST.validMainHandWeapons.add(Material.GOLD_HOE);
        Specialization.TEMPEST.validMainHandWeapons.add(Material.DIAMOND_HOE);
        Specialization.TEMPEST.validOffHandWeapons.add(Material.AIR);
        // [Left] Fireball, [Shift Left] Hailstorm, [Right] Bolt, [Shift Right] Molten shower
        Specialization.TEMPEST.onLeftClickAir = new FireballFactory();
        Specialization.TEMPEST.onShiftLeftClickAir = new HailBlastFactory();
        Specialization.TEMPEST.onLeftClickBlock = new FireballFactory();
        Specialization.TEMPEST.onShiftLeftClickBlock = new HailBlastFactory();
        Specialization.TEMPEST.onLeftClickEntity = new FireballFactory();
        Specialization.TEMPEST.onShiftLeftClickEntity = new HailBlastFactory();
        Specialization.TEMPEST.onRightClickAir = new BoltFactory();
        Specialization.TEMPEST.onShiftRightClickAir = new MoltenStormFactory();
        Specialization.TEMPEST.onRightClickBlock = new BoltFactory();
        Specialization.TEMPEST.onShiftRightClickBlock = new MoltenStormFactory();
        Specialization.TEMPEST.onRightClickEntity = new BoltFactory();
        Specialization.TEMPEST.onShiftRightClickEntity = new MoltenStormFactory();

        // Wizard
        Specialization.WIZARD.validMainHandWeapons.add(Material.STICK);
        Specialization.WIZARD.validMainHandWeapons.add(Material.BLAZE_ROD);
        Specialization.WIZARD.validOffHandWeapons.add(Material.AIR);
        Specialization.WIZARD.validOffHandWeapons.add(Material.BOOK);
        // [Left] Fireball, [Shift Left] Teleport, [Right] Arcane missile, [Shift Right] Mass piggify
        Specialization.WIZARD.onLeftClickAir = new FireballFactory();
        Specialization.WIZARD.onShiftLeftClickAir = new TeleportFactory();
        Specialization.WIZARD.onLeftClickBlock = new FireballFactory();
        Specialization.WIZARD.onShiftLeftClickBlock = new TeleportFactory();
        Specialization.WIZARD.onLeftClickEntity = new FireballFactory();
        Specialization.WIZARD.onShiftLeftClickEntity = new TeleportFactory();
        Specialization.WIZARD.onRightClickAir = new ArcaneMissileFactory();
        Specialization.WIZARD.onShiftRightClickAir = new MassPiggifyFactory();
        Specialization.WIZARD.onRightClickBlock = new ArcaneMissileFactory();
        Specialization.WIZARD.onShiftRightClickBlock = new MassPiggifyFactory();
        Specialization.WIZARD.onRightClickEntity = new ArcaneMissileFactory();
        Specialization.WIZARD.onShiftRightClickEntity = new MassPiggifyFactory();

        // Oracle
        Specialization.ORACLE.validMainHandWeapons.add(Material.WOOD_HOE);
        Specialization.ORACLE.validMainHandWeapons.add(Material.STONE_HOE);
        Specialization.ORACLE.validMainHandWeapons.add(Material.IRON_HOE);
        Specialization.ORACLE.validMainHandWeapons.add(Material.GOLD_HOE);
        Specialization.ORACLE.validMainHandWeapons.add(Material.DIAMOND_HOE);
        Specialization.ORACLE.validOffHandWeapons.add(Material.AIR);
        // [Left] Fireball, [Shift Left] Purifying flames, [Right] Starsurge, [Shift Right] Astral pulse
        Specialization.ORACLE.onLeftClickAir = new FireballFactory();
        Specialization.ORACLE.onLeftClickBlock = new FireballFactory();
        Specialization.ORACLE.onLeftClickEntity = new FireballFactory();
        Specialization.ORACLE.onShiftLeftClickEntity = null; // TODO PurifyingFlamesFactory();
        Specialization.ORACLE.onRightClickAir = null; // TODO StarsurgeFactory();
        Specialization.ORACLE.onShiftRightClickAir = null; // TODO AstralPulseFactory();
        Specialization.ORACLE.onRightClickBlock = null; // TODO StarsurgeFactory();
        Specialization.ORACLE.onShiftRightClickBlock = null; // TODO AstralPulseFactory();
        Specialization.ORACLE.onRightClickEntity = null; // TODO StarsurgeFactory();
        Specialization.ORACLE.onShiftRightClickEntity = null; // TODO AstralPulseFactory();

        // Archon
        Specialization.ARCHON.validMainHandWeapons.add(Material.STICK);
        Specialization.ARCHON.validMainHandWeapons.add(Material.BLAZE_ROD);
        Specialization.ARCHON.validOffHandWeapons.add(Material.AIR);
        // [Left] Fireball, [Shift Left] Void prison, [Right] Void bolt, [Shift Right] Supernova
        Specialization.ARCHON.onLeftClickAir = new FireballFactory();
        Specialization.ARCHON.onShiftLeftClickAir = null; // TODO VoidPrisonFactory();
        Specialization.ARCHON.onLeftClickBlock = new FireballFactory();
        Specialization.ARCHON.onShiftLeftClickBlock = null; // TODO VoidPrisonFactory();
        Specialization.ARCHON.onLeftClickEntity = new FireballFactory();
        Specialization.ARCHON.onShiftLeftClickEntity = null; // TODO VoidPrisonFactory();
        Specialization.ARCHON.onRightClickAir = null; // TODO VoidBoltFactory();
        Specialization.ARCHON.onShiftRightClickAir = null; // FIXME cast Supernova
        Specialization.ARCHON.onRightClickBlock = null; // TODO VoidBoltFactory();
        Specialization.ARCHON.onShiftRightClickBlock = null; // FIXME cast Supernova
        Specialization.ARCHON.onRightClickEntity = null; // TODO VoidBoltFactory();
        Specialization.ARCHON.onShiftRightClickEntity = null; // FIXME cast Supernova
    }

    @Override
    public Tameable getCombatPetFor(MonolithPlayer player) {
        return hunterPetListener.pets.get(player);
    }

    @Override
    public MonolithEntity getOwnerFor(Tameable combatPet) {
        Player owner = hunterPetListener.petToOwner.get(combatPet);
        if (owner != null) {
            return playerService.getLegendsPlayer(owner.getUniqueId());
        } else {
            return null;
        }
    }

    @Override
    protected UUID overrideEntityDamageByEntityEventMonitor(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Wolf) { // combat pet
            MonolithEntity owner = getOwnerFor(((Wolf) event.getDamager()));
            if (owner != null) {
                return owner.getUniqueId();
            }
        }
        return null;
    }

    @Override
    protected void onMonolithEntityDamageEventMonitorOverride(MonolithEntityDamageEvent event) {
        LivingEntity livingEntity = ((SimpleMonolithEntity) event.getEntity()).getLivingEntity();
        LivingEntity damager = ((SimpleMonolithEntity) event.getDamager()).getLivingEntity();
        if (livingEntity instanceof Wolf) { // combat pet
            Wolf pet = (Wolf) livingEntity;
            if (pet.getTarget() == null) {
                pet.setTarget(damager);
            }
        } else if (event.getEntity() instanceof MonolithPlayer) {
            Tameable pet = getCombatPetFor((MonolithPlayer) event.getEntity());
            Wolf wolf = (Wolf) pet;
            if (pet != null) {
                if (wolf.getTarget() == null) {
                    wolf.setTarget(damager);
                } else {
                    MonolithEntity wolfTarget = EntityService.getInstance().getMonolithEntityFor(wolf.getTarget());
                    MonolithEntity wolfAsMEntity = EntityService.getInstance().getMonolithEntityFor(wolf);
                    if (wolfAsMEntity.getRace() == wolfTarget.getRace()) {
                        wolf.setTarget(damager);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        super.onPlayerJoinEvent(event);
        eventService.registerForEvents(new PersonalScoreboard(event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        super.onEntityDamageByEntityEvent(event);
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player damager = (Player) event.getDamager();
        LegendsPlayer player = playerService.getLegendsPlayer(damager.getUniqueId());
        Specialization spec = player.getSpecialization();
        if (spec != null) {
            spec.onEntityDamageByEntityEvent(event, damager);
        }
    }

    // TODO maybe check for cancelled and type != AIR to instantly return?
    @EventHandler(priority = EventPriority.LOW) // do not ignoreCancelled, INTERACT_AIR fires as cancelled
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        UUID uniqueId = event.getPlayer().getUniqueId();
        LegendsPlayer player = playerService.getLegendsPlayer(uniqueId);
        Specialization spec = player.getSpecialization();
        if (spec != null) {
            spec.onPlayerInteractEvent(event);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        UUID uniqueId = event.getPlayer().getUniqueId();
        LegendsPlayer player = playerService.getLegendsPlayer(uniqueId);
        Specialization spec = player.getSpecialization();
        if (spec != null) {
            spec.onPlayerInteractEntityEvent(event);
        }
    }
}
