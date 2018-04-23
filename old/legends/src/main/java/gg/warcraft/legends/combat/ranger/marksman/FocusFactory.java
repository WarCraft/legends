package gg.warcraft.legends.combat.ranger.marksman;

import gg.warcraft.monolith.combat.MonolithAbilityFactory;
import gg.warcraft.monolith.combat.event.MonolithAbilityCastEvent;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.config.ConfigurationService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.event.EventService;

public class FocusFactory implements MonolithAbilityFactory {

    private final ConfigurationService configService;
    private final EventService eventService;

    public FocusFactory() {
        this(ConfigurationService.getInstance(), EventService.getInstance());
    }

    public FocusFactory(ConfigurationService configService, EventService eventService) {
        this.configService = configService;
        this.eventService = eventService;
    }

    @Override
    public Focus create(MonolithEntity caster) {
        AbilityConfiguration config = configService.getAbilityConfigurationFor(Focus.class);
        MonolithAbilityCastEvent event = new MonolithAbilityCastEvent(Focus.class, config, caster);
        eventService.publishEvent(event);
        if (!event.isCancelled()) {
            return new Focus(caster, config);
        } else {
            return null;
        }
    }
}
