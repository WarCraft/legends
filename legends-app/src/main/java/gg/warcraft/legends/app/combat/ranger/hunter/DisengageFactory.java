package gg.warcraft.legends.app.combat.ranger.hunter;

import gg.warcraft.monolith.combat.MonolithAbilityFactory;
import gg.warcraft.monolith.combat.event.MonolithAbilityCastEvent;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.config.ConfigurationService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.event.EventService;

public class DisengageFactory implements MonolithAbilityFactory {

    private final ConfigurationService configService;
    private final EventService eventService;

    public DisengageFactory() {
        this(ConfigurationService.getInstance(), EventService.getInstance());
    }

    public DisengageFactory(ConfigurationService configService, EventService eventService) {
        this.configService = configService;
        this.eventService = eventService;
    }

    @Override
    public Disengage create(MonolithEntity caster) {
        AbilityConfiguration config = configService.getAbilityConfigurationFor(Disengage.class);
        MonolithAbilityCastEvent event = new MonolithAbilityCastEvent(Disengage.class, config, caster);
        eventService.publishEvent(event);
        if (!event.isCancelled()) {
            double distance = config.get(AbilityConfiguration.Attribute.DISTANCE);
            return new Disengage(caster, distance);
        } else {
            return null;
        }
    }
}
