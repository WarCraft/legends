package gg.warcraft.legends.combat.mage.tempest;

import gg.warcraft.monolith.combat.MonolithAbilityFactory;
import gg.warcraft.monolith.combat.event.MonolithAbilityCastEvent;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.config.ConfigurationService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.event.EventService;

public class BoltFactory implements MonolithAbilityFactory {

    private final ConfigurationService configService;
    private final EventService eventService;

    public BoltFactory() {
        this(ConfigurationService.getInstance(), EventService.getInstance());
    }

    public BoltFactory(ConfigurationService configService, EventService eventService) {
        this.configService = configService;
        this.eventService = eventService;
    }

    @Override
    public Bolt create(MonolithEntity caster) {
        AbilityConfiguration config = configService.getAbilityConfigurationFor(Bolt.class);
        MonolithAbilityCastEvent event = new MonolithAbilityCastEvent(Bolt.class, config, caster);
        eventService.publishEvent(event);
        if (!event.isCancelled()) {
            return new Bolt(caster, config);
        } else {
            return null;
        }
    }
}
