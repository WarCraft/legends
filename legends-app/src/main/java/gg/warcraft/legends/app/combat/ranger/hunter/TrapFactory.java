package gg.warcraft.legends.app.combat.ranger.hunter;

import gg.warcraft.monolith.combat.MonolithAbilityFactory;
import gg.warcraft.monolith.combat.event.MonolithAbilityCastEvent;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.config.ConfigurationService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.event.EventService;
import gg.warcraft.monolith.util.Duration;
import gg.warcraft.monolith.util.PotionConfiguration;
import org.bukkit.potion.PotionEffectType;

import static gg.warcraft.monolith.config.AbilityConfiguration.EffectType.ROOT;

public class TrapFactory implements MonolithAbilityFactory {

    private final ConfigurationService configService;
    private final EventService eventService;

    public TrapFactory() {
        this(ConfigurationService.getInstance(), EventService.getInstance());
    }

    public TrapFactory(ConfigurationService configService, EventService eventService) {
        this.configService = configService;
        this.eventService = eventService;
    }

    @Override
    public Trap create(MonolithEntity caster) {
        AbilityConfiguration config = configService.getAbilityConfigurationFor(Trap.class);
        MonolithAbilityCastEvent event = new MonolithAbilityCastEvent(Trap.class, config, caster);
        eventService.publishEvent(event);
        if (!event.isCancelled()) {
            double radius = config.get(AbilityConfiguration.Attribute.RADIUS);
            Duration trapDuration = config.getDuration();
            Duration rootDuration = config.getDuration(ROOT);
            Duration poisonDuration = config.getDuration(PotionEffectType.POISON);
            int poisonAmplifier = config.getAmplifier(PotionEffectType.POISON);
            PotionConfiguration poison = new PotionConfiguration(poisonDuration, poisonAmplifier);
            return new Trap(caster, radius, trapDuration, rootDuration, poison);
        } else {
            return null;
        }
    }
}
