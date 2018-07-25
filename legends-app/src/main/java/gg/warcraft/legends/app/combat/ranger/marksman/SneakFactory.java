package gg.warcraft.legends.app.combat.ranger.marksman;

import gg.warcraft.monolith.combat.MonolithAbilityFactory;
import gg.warcraft.monolith.combat.event.MonolithAbilityCastEvent;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.config.ConfigurationService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.event.EventService;
import gg.warcraft.monolith.util.Duration;
import gg.warcraft.monolith.util.PotionConfiguration;
import org.bukkit.potion.PotionEffectType;

public class SneakFactory implements MonolithAbilityFactory {

    private final ConfigurationService configService;
    private final EventService eventService;

    public SneakFactory() {
        this(ConfigurationService.getInstance(), EventService.getInstance());
    }

    public SneakFactory(ConfigurationService configService, EventService eventService) {
        this.configService = configService;
        this.eventService = eventService;
    }

    @Override
    public Sneak create(MonolithEntity caster) {
        AbilityConfiguration config = configService.getAbilityConfigurationFor(Sneak.class);
        MonolithAbilityCastEvent event = new MonolithAbilityCastEvent(Sneak.class, config, caster);
        eventService.publishEvent(event);
        if (!event.isCancelled()) {
            Duration duration = config.getDuration();
            int speedAmplifier = config.getAmplifier(PotionEffectType.SPEED);
            PotionConfiguration speed = new PotionConfiguration(duration, speedAmplifier);
            return new Sneak(caster, speed);
        } else {
            return null;
        }
    }
}
