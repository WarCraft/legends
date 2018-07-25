package gg.warcraft.legends.app.combat.mage.wizard;

import gg.warcraft.monolith.combat.MonolithAbilityFactory;
import gg.warcraft.monolith.combat.event.MonolithAbilityCastEvent;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.config.ConfigurationService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.event.EventService;
import gg.warcraft.monolith.util.Duration;
import gg.warcraft.monolith.util.MaterialUtils;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;

import static gg.warcraft.monolith.config.AbilityConfiguration.Attribute.RADIUS;

public class MassPiggifyFactory implements MonolithAbilityFactory {

    private final ConfigurationService configService;
    private final EventService eventService;

    public MassPiggifyFactory() {
        this(ConfigurationService.getInstance(), EventService.getInstance());
    }

    public MassPiggifyFactory(ConfigurationService configService, EventService eventService) {
        this.configService = configService;
        this.eventService = eventService;
    }

    @Override
    public MassPiggify create(MonolithEntity caster) {
        if (true) {
            caster.sendMessage(ChatColor.YELLOW + "Mass Piggify has been temporarily disabled while we sort out an issue with it, sorry!");
            return null;
        }

        Block target = caster.getTargetBlock(MaterialUtils.NON_SOLIDS, 50);
        if (!target.isEmpty()) {
            AbilityConfiguration config = configService.getAbilityConfigurationFor(MassPiggify.class);
            MonolithAbilityCastEvent event = new MonolithAbilityCastEvent(MassPiggify.class, config, caster);
            eventService.publishEvent(event);
            if (!event.isCancelled()) {
                Duration countdown = Duration.fromSeconds(config.getDouble("countdown"));
                double radius = config.get(RADIUS);
                Duration duration = config.getDuration();
                return new MassPiggify(caster, target.getLocation().add(0, 1, 0), countdown, radius, duration);
            }
        }
        return null;
    }
}
