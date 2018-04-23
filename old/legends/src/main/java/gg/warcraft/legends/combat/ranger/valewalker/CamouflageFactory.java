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

public class CamouflageFactory implements MonolithAbilityFactory {

    private final ConfigurationService configService;
    private final EventService eventService;

    public CamouflageFactory() {
        this(ConfigurationService.getInstance(), EventService.getInstance());
    }

    public CamouflageFactory(ConfigurationService configService, EventService eventService) {
        this.configService = configService;
        this.eventService = eventService;
    }

    @Override
    public Camouflage create(MonolithEntity caster) {
        AbilityConfiguration config = configService.getAbilityConfigurationFor(Camouflage.class);
        MonolithAbilityCastEvent event = new MonolithAbilityCastEvent(Camouflage.class, config, caster);
        eventService.publishEvent(event);
        if (!event.isCancelled()) {
            Duration duration = config.getDuration();
            int invisibilityAmplifier = config.getAmplifier(PotionEffectType.INVISIBILITY);
            PotionConfiguration invisibility = new PotionConfiguration(duration, invisibilityAmplifier);
            int regenAmplifier = config.getAmplifier(PotionEffectType.REGENERATION);
            PotionConfiguration regen = new PotionConfiguration(duration, regenAmplifier);
            return new Camouflage(caster, duration, invisibility, regen);
        } else {
            return null;
        }
    }
}
