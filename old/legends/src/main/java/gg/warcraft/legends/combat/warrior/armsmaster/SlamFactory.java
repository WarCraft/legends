package gg.warcraft.legends.combat.warrior.armsmaster;

import gg.warcraft.monolith.combat.TargetedMonolithAbilityFactory;
import gg.warcraft.monolith.combat.event.MonolithAbilityCastEvent;
import gg.warcraft.monolith.combat.event.MonolithTargetedAbilityCastEvent;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.config.ConfigurationService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.event.EventService;

public class SlamFactory implements TargetedMonolithAbilityFactory {

    private final ConfigurationService configService;
    private final EventService eventService;

    public SlamFactory() {
        this(ConfigurationService.getInstance(), EventService.getInstance());
    }

    public SlamFactory(ConfigurationService configService, EventService eventService) {
        this.configService = configService;
        this.eventService = eventService;
    }

    @Override
    public Slam create(MonolithEntity caster, MonolithEntity target) {
        AbilityConfiguration config = configService.getAbilityConfigurationFor(Slam.class);
        MonolithTargetedAbilityCastEvent event = new MonolithTargetedAbilityCastEvent(Slam.class, config, caster, target);
        eventService.publishEvent(event);
        if (!event.isCancelled()) {
            return new Slam(caster, target, config);
        } else {
            return null;
        }
    }
}
