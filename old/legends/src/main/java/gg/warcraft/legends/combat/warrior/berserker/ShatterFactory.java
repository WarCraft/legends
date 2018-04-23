package gg.warcraft.legends.combat.warrior.berserker;

import gg.warcraft.monolith.combat.MonolithAbilityFactory;
import gg.warcraft.monolith.combat.event.MonolithAbilityCastEvent;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.config.ConfigurationService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.event.EventService;

public class ShatterFactory implements MonolithAbilityFactory {

    private final ConfigurationService configService;
    private final EventService eventService;

    public ShatterFactory() {
        this(ConfigurationService.getInstance(), EventService.getInstance());
    }

    public ShatterFactory(ConfigurationService configService, EventService eventService) {
        this.configService = configService;
        this.eventService = eventService;
    }

    @Override
    public Shatter create(MonolithEntity caster) {
        AbilityConfiguration config = configService.getAbilityConfigurationFor(Shatter.class);
        MonolithAbilityCastEvent event = new MonolithAbilityCastEvent(Shatter.class, config, caster);
        eventService.publishEvent(event);
        if (!event.isCancelled()) {
            return new Shatter(caster, config);
        } else {
            return null;
        }
    }
}
