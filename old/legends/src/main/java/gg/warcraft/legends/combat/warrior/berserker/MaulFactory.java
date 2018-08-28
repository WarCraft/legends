package gg.warcraft.legends.combat.warrior.berserker;

import gg.warcraft.monolith.combat.TargetedMonolithAbilityFactory;
import gg.warcraft.monolith.combat.event.MonolithTargetedAbilityCastEvent;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.config.ConfigurationService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.event.EventService;

public class MaulFactory implements TargetedMonolithAbilityFactory {

    private final ConfigurationService configService;
    private final EventService eventService;

    public MaulFactory() {
        this(ConfigurationService.getInstance(), EventService.getInstance());
    }

    public MaulFactory(ConfigurationService configService, EventService eventService) {
        this.configService = configService;
        this.eventService = eventService;
    }

    @Override
    public Maul create(MonolithEntity caster, MonolithEntity target) {
        AbilityConfiguration config = configService.getAbilityConfigurationFor(Maul.class);
        MonolithTargetedAbilityCastEvent event = new MonolithTargetedAbilityCastEvent(Maul.class, config, caster, target);
        eventService.publishEvent(event);
        if (!event.isCancelled()) {
            return new Maul(caster, target, config);
        } else {
            return null;
        }
    }
}
