package gg.warcraft.legends.app.combat.mage.tempest;

import gg.warcraft.monolith.combat.MonolithAbilityFactory;
import gg.warcraft.monolith.combat.event.MonolithAbilityCastEvent;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.config.ConfigurationService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.event.EventService;
import gg.warcraft.monolith.util.MaterialUtils;
import org.bukkit.block.Block;

public class MoltenStormFactory implements MonolithAbilityFactory {

    private final ConfigurationService configService;
    private final EventService eventService;

    public MoltenStormFactory() {
        this(ConfigurationService.getInstance(), EventService.getInstance());
    }

    public MoltenStormFactory(ConfigurationService configService, EventService eventService) {
        this.configService = configService;
        this.eventService = eventService;
    }

    @Override
    public MoltenStorm create(MonolithEntity caster) {
        Block target = caster.getTargetBlock(MaterialUtils.NON_SOLIDS, 50);
        if (!target.isEmpty()) {
            AbilityConfiguration config = configService.getAbilityConfigurationFor(MoltenStorm.class);
            MonolithAbilityCastEvent event = new MonolithAbilityCastEvent(MoltenStorm.class, config, caster);
            eventService.publishEvent(event);
            if (!event.isCancelled()) {
                return new MoltenStorm(caster, config, target.getLocation().add(0, 1, 0));
            }
        }
        return null;
    }
}
