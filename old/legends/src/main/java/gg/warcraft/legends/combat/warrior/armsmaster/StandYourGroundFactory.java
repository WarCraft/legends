package gg.warcraft.legends.combat.warrior.armsmaster;

import gg.warcraft.monolith.combat.MonolithAbilityFactory;
import gg.warcraft.monolith.combat.event.MonolithAbilityCastEvent;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.config.ConfigurationService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.event.EventService;

public class StandYourGroundFactory implements MonolithAbilityFactory {

    private final ConfigurationService configService;
    private final EventService eventService;

    public StandYourGroundFactory() {
        this(ConfigurationService.getInstance(), EventService.getInstance());
    }

    public StandYourGroundFactory(ConfigurationService configService, EventService eventService) {
        this.configService = configService;
        this.eventService = eventService;
    }

    @Override
    public StandYourGround create(MonolithEntity caster) {
        AbilityConfiguration config = configService.getAbilityConfigurationFor(StandYourGround.class);
        MonolithAbilityCastEvent event = new MonolithAbilityCastEvent(StandYourGround.class, config, caster);
        eventService.publishEvent(event);
        if (!event.isCancelled()) {
            return new StandYourGround(caster, config);
        } else {
            return null;
        }
    }
}
