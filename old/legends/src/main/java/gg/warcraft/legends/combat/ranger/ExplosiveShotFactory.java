package gg.warcraft.legends.combat.ranger;

import gg.warcraft.monolith.combat.MonolithAbilityFactory;
import gg.warcraft.monolith.combat.event.MonolithAbilityCastEvent;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.config.ConfigurationService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.event.EventService;

public class ExplosiveShotFactory implements MonolithAbilityFactory {

    private final ConfigurationService configService;
    private final EventService eventService;

    public ExplosiveShotFactory() {
        this(ConfigurationService.getInstance(), EventService.getInstance());
    }

    public ExplosiveShotFactory(ConfigurationService configService, EventService eventService) {
        this.configService = configService;
        this.eventService = eventService;
    }

    @Override
    public ExplosiveShot create(MonolithEntity caster) {
        AbilityConfiguration config = configService.getAbilityConfigurationFor(ExplosiveShot.class);
        MonolithAbilityCastEvent event = new MonolithAbilityCastEvent(ExplosiveShot.class, config, caster);
        eventService.publishEvent(event);
        if (!event.isCancelled()) {
            double damage = config.get(AbilityConfiguration.Attribute.DAMAGE);
            double knockback = config.get(AbilityConfiguration.Attribute.KNOCKBACK);
            double radius = config.get(AbilityConfiguration.Attribute.RADIUS);
            return new ExplosiveShot(caster, damage, knockback, radius);
        } else {
            return null;
        }
    }
}
