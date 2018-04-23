package gg.warcraft.legends.combat.warrior.overlord;

import gg.warcraft.monolith.combat.TargetedMonolithAbilityFactory;
import gg.warcraft.monolith.combat.event.MonolithAbilityCastEvent;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.config.ConfigurationService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.event.EventService;
import gg.warcraft.monolith.util.Duration;
import gg.warcraft.monolith.util.PotionConfiguration;
import org.bukkit.potion.PotionEffectType;

public class HeroicStrikeFactory implements TargetedMonolithAbilityFactory {

    private final ConfigurationService configService;
    private final EventService eventService;

    public HeroicStrikeFactory() {
        this(ConfigurationService.getInstance(), EventService.getInstance());
    }

    public HeroicStrikeFactory(ConfigurationService configService, EventService eventService) {
        this.configService = configService;
        this.eventService = eventService;
    }

    @Override
    public HeroicStrike create(MonolithEntity caster, MonolithEntity target) {
        AbilityConfiguration config = configService.getAbilityConfigurationFor(HeroicStrike.class);
        MonolithAbilityCastEvent event = new MonolithAbilityCastEvent(HeroicStrike.class, config, caster);
        eventService.publishEvent(event);
        if (!event.isCancelled()) {
            double damage = config.get(AbilityConfiguration.Attribute.DAMAGE);
            Duration weaknessDuration = config.getDuration(PotionEffectType.WEAKNESS);
            int weaknessAmplifier = config.getAmplifier(PotionEffectType.WEAKNESS);
            PotionConfiguration weakness = new PotionConfiguration(weaknessDuration, weaknessAmplifier);
            return new HeroicStrike(caster, target, damage, weakness);
        } else {
            return null;
        }
    }
}
