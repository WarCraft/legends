package gg.warcraft.legends.combat.ranger.marksman;

import gg.warcraft.monolith.combat.MonolithAbilityFactory;
import gg.warcraft.monolith.combat.event.MonolithAbilityCastEvent;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.config.ConfigurationService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.event.EventService;

public class PowerShotFactory implements MonolithAbilityFactory {

    private final ConfigurationService configService;
    private final EventService eventService;

    public PowerShotFactory() {
        this(ConfigurationService.getInstance(), EventService.getInstance());
    }

    public PowerShotFactory(ConfigurationService configService, EventService eventService) {
        this.configService = configService;
        this.eventService = eventService;
    }

    @Override
    public PowerShot create(MonolithEntity caster) {
        AbilityConfiguration config = configService.getAbilityConfigurationFor(PowerShot.class);
        MonolithAbilityCastEvent event = new MonolithAbilityCastEvent(PowerShot.class, config, caster);
        eventService.publishEvent(event);
        if (!event.isCancelled()) {
            return new PowerShot(caster, config);
        } else {
            return null;
        }
    }
}
