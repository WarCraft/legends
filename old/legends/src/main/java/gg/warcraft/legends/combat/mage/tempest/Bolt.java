package gg.warcraft.legends.combat.mage.tempest;

import gg.warcraft.monolith.combat.HostileTo;
import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.entity.EntityService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.util.MaterialUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LightningStrike;

import java.util.List;
import java.util.stream.Collectors;

import static gg.warcraft.monolith.config.AbilityConfiguration.Attribute.RADIUS;
import static gg.warcraft.monolith.config.AbilityConfiguration.Attribute.RANGE;

public class Bolt extends SimpleMonolithAbility {

    public Bolt(MonolithEntity caster, AbilityConfiguration config) {
        this(EntityService.getInstance(), caster, config);
    }

    public Bolt(EntityService entityService, MonolithEntity caster, AbilityConfiguration config) {
        super(caster, config);
        int range = (int) config.get(RANGE);
        Block block = caster.getTargetBlock(MaterialUtils.NON_SOLIDS, range);
        if (block != null) {
            double radius = config.get(RADIUS);
            Location location = block.getLocation();
            LightningStrike bolt = location.getWorld().strikeLightningEffect(location);
            List<MonolithEntity> casualties = entityService.getNearbyMonolithEntities(bolt.getLocation(), radius);
            casualties = casualties.stream().filter(new HostileTo(caster)).collect(Collectors.toList());
            publishHitAndDamageEvents(this, casualties).forEach(damageEvent -> {
                damageEvent.getEntity().damage(damageEvent.getDamage());
            });
        }
    }

    @Override
    public String toString() {
        return "Bolt";
    }
}
