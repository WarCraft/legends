package gg.warcraft.legends.app.combat.mage.tempest;

import gg.warcraft.monolith.combat.HostileTo;
import gg.warcraft.monolith.combat.MonolithProjectile;
import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.combat.effect.mage.FireballEffect;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.entity.EntityService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.util.CancellableGroup;
import gg.warcraft.monolith.util.Duration;
import gg.warcraft.monolith.util.RunnableUtils;
import gg.warcraft.monolith.util.hitbox.Hitbox;
import gg.warcraft.monolith.util.hitbox.SphericalHitbox;
import gg.warcraft.monolith.util.movement.LinearMovement;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.util.Vector;

import java.util.Collection;

import static gg.warcraft.monolith.config.AbilityConfiguration.Attribute.RADIUS;
import static gg.warcraft.monolith.config.AbilityConfiguration.EffectType.BURN;

public class MoltenFireball extends SimpleMonolithAbility implements MonolithProjectile.Observer {
    private static final double MAX_DISTANCE = 30;

    private final CancellableGroup tasks;

    public MoltenFireball(MonolithEntity caster, AbilityConfiguration config, Location origin, Vector direction) {
        this(EntityService.getInstance(), caster, config, origin, direction);
    }

    public MoltenFireball(EntityService entityService, MonolithEntity caster, AbilityConfiguration config,
                          Location origin, Vector direction) {
        super(caster, config);
        this.tasks = new CancellableGroup();

        double radius = config.get(RADIUS);
        LinearMovement movement = new LinearMovement(origin, direction.normalize(), MAX_DISTANCE);
        Hitbox hitbox = new SphericalHitbox(entityService, radius, new HostileTo(caster));
        MonolithProjectile projectile = new MonolithProjectile(movement, hitbox);
        projectile.addObserver(this);
        this.tasks.add(RunnableUtils.runTask(projectile, 1, 1));

        this.tasks.add(new FireballEffect(movement::getLocation));
    }

    private void playExplodeSound(Location location) {
        location.getWorld().playSound(location, Sound.ENTITY_ENDERDRAGON_FIREBALL_EXPLODE, SoundCategory.HOSTILE, 1, 1);
    }

    @Override
    public void onHit(MonolithProjectile projectile, Collection<MonolithEntity> casualties) {
        int burnDuration = (int) getConfiguration().getDuration(BURN).as(Duration.Type.TICKS);
        playExplodeSound(casualties.iterator().next().getLocation());
        publishHitAndDamageEvents(this, casualties).forEach(damageEvent -> {
            MonolithEntity entity = damageEvent.getEntity();
            entity.damage(damageEvent.getDamage());
            entity.setFireTicks(burnDuration);
        });
        tasks.cancel();
    }

    @Override
    public void onGrounded(MonolithProjectile projectile, Location location) {
        playExplodeSound(location);
        tasks.cancel();
    }

    @Override
    public void onMaxDistanceReached(MonolithProjectile projectile, double distance) {
        tasks.cancel();
    }
}
