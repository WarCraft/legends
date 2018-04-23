package gg.warcraft.legends.combat.mage.archon;

import gg.warcraft.legends.combat.effect.mage.archon.SupernovaVacuumEffect;
import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.effect.MonolithEffect;
import gg.warcraft.monolith.entity.EntityService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.util.Duration;
import gg.warcraft.monolith.util.RunnableUtils;
import gg.warcraft.monolith.util.TimeUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

import static gg.warcraft.monolith.util.Duration.Type.TICKS;

public class SupernovaVacuum extends SimpleMonolithAbility {

    private final EntityService entityService;
    private final Location location;
    private final Duration countdown;
    private final double radius;

    private BukkitTask task;
    private MonolithEffect effect;
    private int timer;

    public SupernovaVacuum(MonolithEntity caster, Location location, double radius, Duration countdown) {
        this(EntityService.getInstance(), caster, location, radius, countdown);
    }

    public SupernovaVacuum(EntityService entityService, MonolithEntity caster, Location location, double radius, Duration countdown) {
        super(caster);
        this.entityService = entityService;
        this.location = location;
        this.countdown = countdown;
        this.radius = radius;
        task = RunnableUtils.runTask(this::tick, 1L, 1L);
        effect = new SupernovaVacuumEffect(location);
        playSound();
    }

    private void playSound() {
        location.getWorld().playSound(location, Sound.ENTITY_WITHER_AMBIENT, 0.2f, 0.1f);
    }

    public void tick() {
        if (++timer >= countdown.as(TICKS)) {
            new SupernovaImplosion(getCaster(), location, 10, 5); // TODO: get values somehow from config, make spells observable and listen for them from factory?
            task.cancel();
            effect.cancel();
        } else {
            List<MonolithEntity> entities = entityService.getNearbyMonolithEntities(location, radius);
            for (MonolithEntity entity : entities) {
                entity.vacuum(location, 1);
                entity.heavy(TimeUtils.TICKS_PER_SECOND);
            }
        }
    }

    @Override
    public String toString() {
        return "Supernova";
    }
}
