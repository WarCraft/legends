package gg.warcraft.legends.app.combat.mage.wizard;

import gg.warcraft.legends.LegendsCombatSound;
import gg.warcraft.legends.combat.effect.mage.wizard.ArcaneMissileEffect;
import gg.warcraft.monolith.combat.HostileTo;
import gg.warcraft.monolith.combat.MonolithProjectile;
import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.entity.EntityService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.sound.MonolithSound;
import gg.warcraft.monolith.util.CancellableGroup;
import gg.warcraft.monolith.util.RunnableUtils;
import gg.warcraft.monolith.util.hitbox.Hitbox;
import gg.warcraft.monolith.util.hitbox.SphericalHitbox;
import gg.warcraft.monolith.util.movement.LinearMovement;
import gg.warcraft.monolith.util.movement.Movement;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.Collection;

public class ArcaneMissile extends SimpleMonolithAbility implements MonolithProjectile.Observer {
    private static final double MAX_DISTANCE = 30;

    private final CancellableGroup tasks;
    private final MonolithSound onHitSound;

    public ArcaneMissile(MonolithEntity caster, AbilityConfiguration config) {
        this(EntityService.getInstance(), LegendsCombatSound.ARCANEMISSILE_CAST, LegendsCombatSound.ARCANEMISSILE_HIT,
                caster, config);
    }

    public ArcaneMissile(EntityService entityService, MonolithSound onCastSound, MonolithSound onHitSound,
                         MonolithEntity caster, AbilityConfiguration config) {
        super(caster, config);
        this.tasks = new CancellableGroup();
        this.onHitSound = onHitSound;

        Location location = caster.getEyeLocation();
        Vector direction = caster.getEyeLocation().getDirection();
        Movement movement = new LinearMovement(location, direction, MAX_DISTANCE);
        double radius = config.get(AbilityConfiguration.Attribute.RADIUS);
        Hitbox hitbox = new SphericalHitbox(entityService, radius, new HostileTo(caster));
        MonolithProjectile projectile = new MonolithProjectile(movement, hitbox);
        projectile.addObserver(this);
        tasks.add(RunnableUtils.runTaskEachTick(projectile));

        tasks.add(new ArcaneMissileEffect(movement::getLocation));

        onCastSound.play(location);
    }

    @Override
    public String toString() {
        return "Arcane Missile";
    }

    @Override
    public void onHit(MonolithProjectile projectile, Collection<MonolithEntity> casualties) {
        onHitSound.play(casualties.iterator().next().getLocation());
        publishHitAndDamageEvents(this, casualties).forEach(damageEvent -> {
            damageEvent.getEntity().damage(damageEvent.getDamage());
        });
        tasks.cancel();
    }

    @Override
    public void onGrounded(MonolithProjectile projectile, Location location) {
        tasks.cancel();
    }

    @Override
    public void onMaxDistanceReached(MonolithProjectile projectile, double distance) {
        tasks.cancel();
    }
}
