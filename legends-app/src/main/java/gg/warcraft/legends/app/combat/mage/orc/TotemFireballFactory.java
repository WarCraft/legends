package gg.warcraft.legends.app.combat.mage.orc;

import gg.warcraft.legends.entity.SpoofedAbilityCasterMonolithEntityDecorator;
import gg.warcraft.monolith.combat.MonolithAbilityFactory;
import gg.warcraft.monolith.combat.event.MonolithAbilityCastEvent;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.config.ConfigurationService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.event.EventService;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class TotemFireballFactory implements MonolithAbilityFactory {

    private final ConfigurationService configService;
    private final EventService eventService;

    public TotemFireballFactory() {
        this(ConfigurationService.getInstance(), EventService.getInstance());
    }

    public TotemFireballFactory(ConfigurationService configService, EventService eventService) {
        this.configService = configService;
        this.eventService = eventService;
    }

    public TotemFireball create(MonolithEntity caster, Location location, Vector direction) {
        MonolithEntity spoofedCaster = new SpoofedAbilityCasterMonolithEntityDecorator(caster);
        AbilityConfiguration config = configService.getAbilityConfigurationFor(TotemFireball.class);
        MonolithAbilityCastEvent event = new MonolithAbilityCastEvent(TotemFireball.class, config, spoofedCaster);
        eventService.publishEvent(event);
        if (!event.isCancelled()) {
            return new TotemFireball(spoofedCaster, config, location, direction);
        } else {
            return null;
        }
    }

    @Override
    public TotemFireball create(MonolithEntity caster) {
        System.out.println("WARN Attempted to instanciate TotemFireball with solely caster!");
        Location eyeLocation = caster.getEyeLocation();
        return create(caster, eyeLocation, eyeLocation.getDirection());
    }
}
