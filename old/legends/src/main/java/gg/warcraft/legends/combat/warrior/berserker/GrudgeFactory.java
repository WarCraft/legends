package gg.warcraft.legends.combat.warrior.berserker;

import gg.warcraft.monolith.combat.TargetedMonolithAbilityFactory;
import gg.warcraft.monolith.combat.event.MonolithTargetedAbilityCastEvent;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.config.ConfigurationService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.event.EventService;

public class GrudgeFactory implements TargetedMonolithAbilityFactory {

    private final ConfigurationService configService;
    private final EventService eventService;

    public GrudgeFactory() {
        this(ConfigurationService.getInstance(), EventService.getInstance());
    }

    public GrudgeFactory(ConfigurationService configService, EventService eventService) {
        this.configService = configService;
        this.eventService = eventService;
    }

    @Override
    public Grudge create(MonolithEntity caster, MonolithEntity target) {
        AbilityConfiguration config = configService.getAbilityConfigurationFor(Grudge.class);
        MonolithTargetedAbilityCastEvent event = new MonolithTargetedAbilityCastEvent(Grudge.class, config, caster, target);
        eventService.publishEvent(event);
        if (!event.isCancelled()) {
            return new Grudge(caster, target, config);
        } else {
            return null;
        }
    }
}
