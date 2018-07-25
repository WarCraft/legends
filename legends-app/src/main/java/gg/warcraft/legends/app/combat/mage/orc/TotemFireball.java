package gg.warcraft.legends.app.combat.mage.orc;

import gg.warcraft.legends.combat.effect.mage.orc.TotemFireballEffect;
import gg.warcraft.monolith.combat.MonolithProjectile;
import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.entity.EntityService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.sound.MonolithCombatSound;
import gg.warcraft.monolith.sound.MonolithSound;
import gg.warcraft.monolith.util.CancellableGroup;
import gg.warcraft.monolith.util.RunnableUtils;
import gg.warcraft.monolith.util.hitbox.Hitbox;
import gg.warcraft.monolith.util.hitbox.SphericalHitbox;
import gg.warcraft.monolith.util.movement.LinearMovement;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.Collection;

import static gg.warcraft.monolith.util.Duration.Type.TICKS;

public class TotemFireball extends SimpleMonolithAbility implements MonolithProjectile.Observer {
    private static final double MAX_DISTANCE = 30;

    private final CancellableGroup tasks;

    public TotemFireball(MonolithEntity caster, AbilityConfiguration config, Location location, Vector direction) {
        this(EntityService.getInstance(), MonolithCombatSound.FIREBALL_CAST, caster, config, location, direction);
    }

    public TotemFireball(EntityService entityService, MonolithSound onCastSound, MonolithEntity caster,
                         AbilityConfiguration config, Location location, Vector direction) {
        super(caster, config);
        this.tasks = new CancellableGroup();

        double radius = config.get(AbilityConfiguration.Attribute.RADIUS);
        LinearMovement movement = new LinearMovement(location, direction, MAX_DISTANCE);
        Hitbox hitbox = new SphericalHitbox(entityService, radius, entity -> entity.getRace() != caster.getRace());
        MonolithProjectile projectile = new MonolithProjectile(movement, hitbox);
        projectile.addObserver(this);
        this.tasks.add(RunnableUtils.runTaskEachTick(projectile));

        this.tasks.add(new TotemFireballEffect(movement::getLocation));

        onCastSound.play(location);
    }

    @Override
    public String toString() {
        return "Totem Fireball";
    }

    @Override
    public void onHit(MonolithProjectile projectile, Collection<MonolithEntity> casualties) {
        int burnDuration = (int) getConfiguration().getDuration(AbilityConfiguration.EffectType.BURN).as(TICKS);
        publishHitAndDamageEvents(this, casualties).forEach(damageEvent -> {
            MonolithEntity entity = damageEvent.getEntity();
            entity.damage(damageEvent.getDamage());
            entity.setFireTicks(burnDuration);
        });
        tasks.cancel();
    }

    @Override
    public void onGrounded(MonolithProjectile projectile, Location location) {
        tasks.cancel();
    }

    @Override
    public void onMaxDistanceReached(MonolithProjectile projectile, double distance) {
        tasks.cancel(); // TODO make cancel default func on MonolithProjectile (maybe not, might want to play sound, but that should be a reason to override)
    }
}
