package gg.warcraft.legends.combat.mage.wizard;

import gg.warcraft.monolith.combat.MonolithAbilityFactory;
import gg.warcraft.monolith.combat.event.MonolithAbilityCastEvent;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.config.ConfigurationService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.event.EventService;

public class TeleportFactory implements MonolithAbilityFactory {

    private final ConfigurationService configService;
    private final EventService eventService;

    public TeleportFactory() {
        this(ConfigurationService.getInstance(), EventService.getInstance());
    }

    public TeleportFactory(ConfigurationService configService, EventService eventService) {
        this.configService = configService;
        this.eventService = eventService;
    }

    @Override
    public Teleport create(MonolithEntity caster) {
        AbilityConfiguration config = configService.getAbilityConfigurationFor(Teleport.class);
        MonolithAbilityCastEvent event = new MonolithAbilityCastEvent(Teleport.class, config, caster);
        eventService.publishEvent(event);
        if (!event.isCancelled()) {
            return new Teleport(caster, config);
        } else {
            return null;
        }
    }
}
