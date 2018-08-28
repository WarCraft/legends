package gg.warcraft.legends.combat.mage.wizard;

import gg.warcraft.monolith.combat.MonolithAbilityFactory;
import gg.warcraft.monolith.combat.event.MonolithAbilityCastEvent;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.config.ConfigurationService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.event.EventService;

public class ArcaneMissileFactory implements MonolithAbilityFactory {

    private final ConfigurationService configService;
    private final EventService eventService;

    public ArcaneMissileFactory() {
        this(ConfigurationService.getInstance(), EventService.getInstance());
    }

    public ArcaneMissileFactory(ConfigurationService configService, EventService eventService) {
        this.configService = configService;
        this.eventService = eventService;
    }

    @Override
    public ArcaneMissile create(MonolithEntity caster) {
        AbilityConfiguration config = configService.getAbilityConfigurationFor(ArcaneMissile.class);
        MonolithAbilityCastEvent event = new MonolithAbilityCastEvent(ArcaneMissile.class, config, caster);
        eventService.publishEvent(event);
        if (!event.isCancelled()) {
            return new ArcaneMissile(caster, config);
        } else {
            return null;
        }
    }
}
