package gg.warcraft.legends.combat.warrior.berserker;

import gg.warcraft.monolith.combat.HostileTo;
import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.entity.EntityService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.util.PotionConfiguration;
import gg.warcraft.monolith.util.RunnableUtils;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.stream.Collectors;

public class Shatter extends SimpleMonolithAbility {

    private final EntityService entityService;
    private final BukkitTask task;

    public Shatter(MonolithEntity caster, AbilityConfiguration config) {
        this(EntityService.getInstance(), caster, config);
    }

    public Shatter(EntityService entityService, MonolithEntity caster, AbilityConfiguration config) {
        super(caster, config);
        this.entityService = entityService;
        double distance = config.get(AbilityConfiguration.Attribute.DISTANCE);

        Vector direction = caster.getEyeLocation().getDirection();
        getCaster().leap(direction, distance);

        this.task = RunnableUtils.runTask(this::tick, 1, 1);
    }

    private void tick() {
        if (getCaster().isOnGround()) {
            double radius = getConfiguration().get(AbilityConfiguration.Attribute.RADIUS);
            double launch = getConfiguration().get(AbilityConfiguration.Attribute.LAUNCH);
            PotionConfiguration confusion = getConfiguration().getPotionConfiguration(PotionEffectType.CONFUSION);

            List<MonolithEntity> casualties = entityService.getNearbyMonolithEntities(getCaster().getLocation(), radius).stream()
                    .filter(new HostileTo(getCaster()))
                    .collect(Collectors.toList());
            publishHitAndDamageEvents(this, casualties).forEach(damageEvent -> {
                damageEvent.getEntity().damage(damageEvent.getDamage());
                damageEvent.getEntity().knockup(launch);
                damageEvent.getEntity().addPotionEffect(PotionEffectType.CONFUSION, confusion);
            });
            task.cancel();
        }
    }

    @Override
    public String toString() {
        return "Shatter";
    }
}
