package gg.warcraft.legends.combat.mage.tempest;

import gg.warcraft.monolith.combat.MonolithAbilityFactory;
import gg.warcraft.monolith.combat.event.MonolithAbilityCastEvent;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.config.ConfigurationService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.event.EventService;

public class HailBlastFactory implements MonolithAbilityFactory {

    private final ConfigurationService configService;
    private final EventService eventService;

    public HailBlastFactory() {
        this(ConfigurationService.getInstance(), EventService.getInstance());
    }

    public HailBlastFactory(ConfigurationService configService, EventService eventService) {
        this.configService = configService;
        this.eventService = eventService;
    }

    @Override
    public HailBlast create(MonolithEntity caster) {
        AbilityConfiguration config = configService.getAbilityConfigurationFor(HailBlast.class);
        MonolithAbilityCastEvent event = new MonolithAbilityCastEvent(HailBlast.class, config, caster);
        eventService.publishEvent(event);
        if (!event.isCancelled()) {
            return new HailBlast(caster, config);
        } else {
            return null;
        }
    }
}
