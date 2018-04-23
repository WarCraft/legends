package gg.warcraft.legends.combat;

import gg.warcraft.monolith.combat.MonolithAbility;
import gg.warcraft.monolith.combat.event.MonolithAbilityCastEvent;
import gg.warcraft.monolith.combat.event.MonolithTargetedAbilityCastEvent;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.util.Duration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

public class AbilityCooldownListener implements Listener {

    private final HashMap<Class<? extends MonolithAbility>, HashMap<UUID, Long>> cooldowns;

    private double modifier;

    public AbilityCooldownListener() {
        this(1);
    }

    public AbilityCooldownListener(double modifier) {
        this.cooldowns = new HashMap<>();
        this.modifier = modifier;
    }

    public void setCooldownModifier(double modifier) {
        this.modifier = modifier;
    }

    private boolean cancelEvent(MonolithEntity caster, Class<? extends MonolithAbility> ability) {
        HashMap<UUID, Long> abilityCooldowns = cooldowns.computeIfAbsent(ability, key -> new HashMap<>());
        Long cooldown = abilityCooldowns.getOrDefault(caster.getUniqueId(), 0L);
        return cooldown > System.currentTimeMillis();
    }

    private void setCooldown(MonolithEntity caster, AbilityConfiguration config, Class<? extends MonolithAbility> ability) {
        HashMap<UUID, Long> abilityCooldowns = cooldowns.computeIfAbsent(ability, key -> new HashMap<>());
        long abilityCooldown = config.getCooldown().as(Duration.Type.MILLIS);
        long cooldown = (long) (modifier * (System.currentTimeMillis() + abilityCooldown));
        abilityCooldowns.put(caster.getUniqueId(), cooldown);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onMonolithAbilityCastEvent(MonolithAbilityCastEvent event) {
        event.setCancelled(cancelEvent(event.getCaster(), event.getAbilityClass()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMonolithAbilityCastEventMonitor(MonolithAbilityCastEvent event) {
        setCooldown(event.getCaster(), event.getConfiguration(), event.getAbilityClass());
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onMonolithTargetedAbilityCastEvent(MonolithTargetedAbilityCastEvent event) {
        event.setCancelled(cancelEvent(event.getCaster(), event.getAbilityClass()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMonolithTargetedAbilityCastEventMonitor(MonolithTargetedAbilityCastEvent event) {
        setCooldown(event.getCaster(), event.getConfiguration(), event.getAbilityClass());
    }
}
