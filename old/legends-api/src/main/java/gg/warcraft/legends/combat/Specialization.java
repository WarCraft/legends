package gg.warcraft.legends.combat;

import gg.warcraft.legends.entity.LegendsAttribute;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.legends.player.LegendsPlayerService;
import gg.warcraft.monolith.combat.MonolithAbilityFactory;
import gg.warcraft.monolith.combat.TargetedMonolithAbilityFactory;
import gg.warcraft.monolith.entity.EntityService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.util.TimeUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public enum Specialization {
    // Warrior
    BERSERKER(Material.STONE_AXE, LegendsAttribute.STRENGTH),
    ARMSMASTER(Material.IRON_SWORD, LegendsAttribute.STRENGTH),
    HARBINGER(Material.IRON_SWORD, LegendsAttribute.STRENGTH),
    OVERLORD(Material.IRON_SWORD, LegendsAttribute.STRENGTH),

    // Ranger
    MARKSMAN(Material.BOW, LegendsAttribute.DEXTERITY),
    HUNTER(Material.BOW, LegendsAttribute.DEXTERITY),
    RIFTSTALKER(Material.BOW, LegendsAttribute.DEXTERITY),
    VALEWALKER(Material.BOW, LegendsAttribute.DEXTERITY),

    // Mage
    TEMPEST(Material.WOOD_HOE, LegendsAttribute.INTELLIGENCE),
    WIZARD(Material.STICK, LegendsAttribute.INTELLIGENCE),
    ORACLE(Material.DIAMOND_HOE, LegendsAttribute.INTELLIGENCE),
    ARCHON(Material.BLAZE_ROD, LegendsAttribute.INTELLIGENCE);

    private static final long NOTIFICATION_COOLDOWN = 10 * TimeUtils.MILLIS_PER_MINUTE;
    private static final HashMap<UUID, HashMap<Specialization, Long>> notificationCooldowns = new HashMap<>();

    private final EntityService entityService;
    private final Material icon;
    private final LegendsAttribute primaryAttribute;

    HashSet<Material> validMainHandWeapons;
    HashSet<Material> validOffHandWeapons;
    MonolithAbilityFactory onLeftClickAir;
    MonolithAbilityFactory onShiftLeftClickAir;
    MonolithAbilityFactory onLeftClickBlock;
    MonolithAbilityFactory onShiftLeftClickBlock;
    TargetedMonolithAbilityFactory onLeftClickEntity;
    TargetedMonolithAbilityFactory onShiftLeftClickEntity;
    MonolithAbilityFactory onRightClickAir;
    MonolithAbilityFactory onShiftRightClickAir;
    MonolithAbilityFactory onRightClickBlock;
    MonolithAbilityFactory onShiftRightClickBlock;
    TargetedMonolithAbilityFactory onRightClickEntity;
    TargetedMonolithAbilityFactory onShiftRightClickEntity;

    Specialization(Material icon, LegendsAttribute primaryAttribute) {
        this(EntityService.getInstance(), icon, primaryAttribute);
    }

    Specialization(EntityService entityService, Material icon, LegendsAttribute primaryAttribute) {
        this.entityService = entityService;
        this.icon = icon;
        this.primaryAttribute = primaryAttribute;
        this.validMainHandWeapons = new HashSet<>();
        this.validOffHandWeapons = new HashSet<>();
    }

    public Material getIcon() {
        return icon;
    }

    public LegendsAttribute getPrimaryAttribute() {
        return primaryAttribute;
    }

    public boolean isValidMainHandWeapon(Material type) {
        return validMainHandWeapons.contains(type);
    }

    public boolean isValidOffHandWeapon(Material type) {
        return validOffHandWeapons.contains(type);
    }

    private boolean isUsingValidWeapons(Player player) {
        Material mainHandType = player.getInventory().getItemInMainHand().getType();
        Material offHandType = player.getInventory().getItemInOffHand().getType();
        boolean valid = validMainHandWeapons.contains(mainHandType) &&
                validOffHandWeapons.contains(offHandType);
        if (!valid && mainHandType != Material.AIR || offHandType == Material.FLINT) {
            LegendsPlayer legendsPlayer = LegendsPlayerService.getInstance().getLegendsPlayer(player.getUniqueId());

            HashMap<Specialization, Long> cooldowns = notificationCooldowns.computeIfAbsent(player.getUniqueId(), key -> new HashMap<>());
            long cooldown = cooldowns.getOrDefault(legendsPlayer.getSpecialization(), 0L);
            if (cooldown < System.currentTimeMillis()) {
                cooldowns.put(legendsPlayer.getSpecialization(), System.currentTimeMillis() + NOTIFICATION_COOLDOWN);
                String rest;
                String specName = legendsPlayer.getSpecialization().name().toLowerCase();
                switch (legendsPlayer.getSpecialization()) {
                    case ARMSMASTER:
                        rest = "As a " + specName + " you have to use a sword in your main hand and can optionally wield a shield in your offhand.";
                        break;
                    case BERSERKER:
                        rest = "As a " + specName + " you have to use an axe in your main hand and can optionally wield a skull in your offhand.";
                        break;
                    case HUNTER:
                        rest = "As a " + specName + " you can only use a bow.";
                        break;
                    case MARKSMAN:
                        rest = "As a " + specName + " you can only use a bow.";
                        break;
                    case TEMPEST:
                        rest = "As a " + specName + " you can only use a staff (hoe).";
                        break;
                    case WIZARD:
                        rest = "As a " + specName + " you have to use a wand (stick) in your main hand and can optionally wield a book in your offhand.";
                        break;
                    default:
                        rest = "";
                        break;
                }
                player.sendMessage(ChatColor.YELLOW + "Oops! You are using invalid weapons. " + rest);
            }
        }
        return valid;
    }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event, Player damager) {
        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK &&
                event.getEntity() instanceof LivingEntity &&
                isUsingValidWeapons(damager)) {
            TargetedMonolithAbilityFactory factory = damager.isSneaking() ? onShiftLeftClickEntity : onLeftClickEntity;
            if (factory != null) {
                MonolithEntity damagerAsMonolithEntity = entityService.getMonolithEntityFor(damager);
                MonolithEntity damaged = entityService.getMonolithEntityFor((LivingEntity) event.getEntity());
                factory.create(damagerAsMonolithEntity, damaged);
                event.setCancelled(true);
            }
        }
    }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (!isUsingValidWeapons(event.getPlayer())) {
            return;
        }

        MonolithAbilityFactory factory = null;
        boolean sneaking = event.getPlayer().isSneaking();
        switch (event.getAction()) {
            case LEFT_CLICK_AIR:
                factory = sneaking ? onShiftLeftClickAir : onLeftClickAir;
                break;
            case LEFT_CLICK_BLOCK:
                factory = sneaking ? onShiftLeftClickBlock : onLeftClickBlock;
                break;
            case RIGHT_CLICK_AIR:
                factory = sneaking ? onShiftRightClickAir : onRightClickAir;
                break;
            case RIGHT_CLICK_BLOCK:
                factory = sneaking ? onShiftRightClickBlock : onRightClickBlock;
                break;
        }

        if (factory != null) {
            factory.create(entityService.getMonolithEntityFor(event.getPlayer()));
            event.setCancelled(true);
        }
    }

    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof LivingEntity &&
                isUsingValidWeapons(event.getPlayer())) {
            boolean sneaking = event.getPlayer().isSneaking();
            TargetedMonolithAbilityFactory factory = sneaking ? onShiftRightClickEntity : onRightClickEntity;
            if (factory != null) {
                MonolithEntity damager = entityService.getMonolithEntityFor(event.getPlayer());
                MonolithEntity damaged = entityService.getMonolithEntityFor((LivingEntity) event.getRightClicked());
                factory.create(damager, damaged);
                event.setCancelled(true);
            }
        }
    }
}
