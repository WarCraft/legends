package gg.warcraft.legends.combat.ranger.hunter;

import gg.warcraft.monolith.combat.MonolithAbilityFactory;
import gg.warcraft.monolith.combat.event.MonolithAbilityCastEvent;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.config.ConfigurationService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.event.EventService;

public class ToxicArrowFactory implements MonolithAbilityFactory {

    private final ConfigurationService configService;
    private final EventService eventService;

    public ToxicArrowFactory() {
        this(ConfigurationService.getInstance(), EventService.getInstance());
    }

    public ToxicArrowFactory(ConfigurationService configService, EventService eventService) {
        this.configService = configService;
        this.eventService = eventService;
    }

    @Override
    public ToxicArrow create(MonolithEntity caster) {
        AbilityConfiguration config = configService.getAbilityConfigurationFor(ToxicArrow.class);
        MonolithAbilityCastEvent event = new MonolithAbilityCastEvent(ToxicArrow.class, config, caster);
        eventService.publishEvent(event);
        if (!event.isCancelled()) {
            return new ToxicArrow(caster, config);
        } else {
            return null;
        }
    }
}
