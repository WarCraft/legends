package gg.warcraft.legends.combat.warrior.harbinger;

import gg.warcraft.monolith.combat.TargetedMonolithAbilityFactory;
import gg.warcraft.monolith.combat.event.MonolithAbilityCastEvent;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.config.ConfigurationService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.event.EventService;
import gg.warcraft.monolith.util.Duration;
import gg.warcraft.monolith.util.PotionConfiguration;
import org.bukkit.potion.PotionEffectType;

public class LifeLeechFactory implements TargetedMonolithAbilityFactory {

    private final ConfigurationService configService;
    private final EventService eventService;

    public LifeLeechFactory() {
        this(ConfigurationService.getInstance(), EventService.getInstance());
    }

    public LifeLeechFactory(ConfigurationService configService, EventService eventService) {
        this.configService = configService;
        this.eventService = eventService;
    }

    @Override
    public LifeLeech create(MonolithEntity caster, MonolithEntity target) {
        AbilityConfiguration config = configService.getAbilityConfigurationFor(LifeLeech.class);
        MonolithAbilityCastEvent event = new MonolithAbilityCastEvent(LifeLeech.class, config, caster);
        eventService.publishEvent(event);
        if (!event.isCancelled()) {
            double heal = config.get(AbilityConfiguration.Attribute.HEAL);
            Duration witherDuration = config.getDuration(PotionEffectType.WITHER);
            int witherAmplifier = config.getAmplifier(PotionEffectType.WITHER);
            PotionConfiguration wither = new PotionConfiguration(witherDuration, witherAmplifier);
            return new LifeLeech(caster, target, heal, wither);
        } else {
            return null;
        }
    }
}
