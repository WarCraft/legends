package gg.warcraft.legends.combat.warrior.armsmaster;

import gg.warcraft.monolith.combat.TargetedMonolithAbilityFactory;
import gg.warcraft.monolith.combat.event.MonolithTargetedAbilityCastEvent;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.config.ConfigurationService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.event.EventService;

public class HamstringFactory implements TargetedMonolithAbilityFactory {

    private final ConfigurationService configService;
    private final EventService eventService;

    public HamstringFactory() {
        this(ConfigurationService.getInstance(), EventService.getInstance());
    }

    public HamstringFactory(ConfigurationService configService, EventService eventService) {
        this.configService = configService;
        this.eventService = eventService;
    }

    @Override
    public Hamstring create(MonolithEntity caster, MonolithEntity target) {
        AbilityConfiguration config = configService.getAbilityConfigurationFor(Hamstring.class);
        MonolithTargetedAbilityCastEvent event = new MonolithTargetedAbilityCastEvent(Hamstring.class, config, caster, target);
        eventService.publishEvent(event);
        if (!event.isCancelled()) {
            return new Hamstring(caster, target, config);
        } else {
            return null;
        }
    }
}
