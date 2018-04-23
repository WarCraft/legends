package gg.warcraft.legends.combat.ranger.valewalker;

import gg.warcraft.monolith.combat.MonolithAbilityFactory;
import gg.warcraft.monolith.combat.event.MonolithAbilityCastEvent;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.config.ConfigurationService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.event.EventService;
import gg.warcraft.monolith.util.Duration;
import gg.warcraft.monolith.util.PotionConfiguration;
import org.bukkit.potion.PotionEffectType;

public class BeastWithinFactory implements MonolithAbilityFactory {

    private final ConfigurationService configService;
    private final EventService eventService;

    public BeastWithinFactory() {
        this(ConfigurationService.getInstance(), EventService.getInstance());
    }

    public BeastWithinFactory(ConfigurationService configService, EventService eventService) {
        this.configService = configService;
        this.eventService = eventService;
    }

    @Override
    public BeastWithin create(MonolithEntity caster) {
        AbilityConfiguration config = configService.getAbilityConfigurationFor(BeastWithin.class);
        MonolithAbilityCastEvent event = new MonolithAbilityCastEvent(BeastWithin.class, config, caster);
        eventService.publishEvent(event);
        if (!event.isCancelled()) {
            Duration duration = config.getDuration();
            int jumpAmplifier = config.getAmplifier(PotionEffectType.JUMP);
            PotionConfiguration jump = new PotionConfiguration(duration, jumpAmplifier);
            int speedAmplifier = config.getAmplifier(PotionEffectType.SPEED);
            PotionConfiguration speed = new PotionConfiguration(duration, speedAmplifier);
            int regenAmplifier = config.getAmplifier(PotionEffectType.REGENERATION);
            PotionConfiguration regen = new PotionConfiguration(duration, regenAmplifier);
            int damageAmplifier = config.getAmplifier(PotionEffectType.INCREASE_DAMAGE);
            PotionConfiguration damage = new PotionConfiguration(duration, damageAmplifier);
            int nightVisionAmplifier = config.getAmplifier(PotionEffectType.NIGHT_VISION);
            PotionConfiguration nightVision = new PotionConfiguration(duration, nightVisionAmplifier);
            int saturationAmplifier = config.getAmplifier(PotionEffectType.SATURATION);
            PotionConfiguration saturation = new PotionConfiguration(duration, saturationAmplifier);
            return new BeastWithin(caster, jump, speed, regen, damage, nightVision, saturation);
        } else {
            return null;
        }
    }
}
