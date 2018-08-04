package gg.warcraft.legends.app.combat.ranger;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.MeleeAbility;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.monolith.api.entity.Entity;

public class ExplosiveShot extends AbstractAbility implements MeleeAbility {

    @Inject
    public ExplosiveShot(@Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                         @Assisted("target") Entity meleeTarget) {
        super(caster.getId(), snapshot);
    }

    @Override
    public void onHit(Entity entity) {

    }

    @Override
    public void onMiss(Entity entity) {

    }
}

  /*
    public ExplosiveShot(EntityService entityService, MonolithEntity caster, double damage, double knockback, double radius) {
        super(caster);
        this.entityService = entityService;
        this.damage = damage;
        this.knockback = knockback;
        this.radius = radius;

        MonolithArrow arrow = new MonolithArrow(caster, new HostileTo(caster));
        arrow.addObserver(this);
    }

    private void explode(Location location) {
        List<MonolithEntity> casualties = entityService.getNearbyMonolithEntities(location, radius);
        casualties.forEach(entity -> {
            entity.damage(damage);
            entity.knockback(location, knockback);
        });

        World world = location.getWorld();
        world.createExplosion(location.getX(), location.getY(), location.getZ(), 0, false, false);
    }

    @Override
    public void onGrounded(Location location) {
        explode(location);
    }

    @Override
    public void onHit(MonolithEntity entity) {
        explode(entity.getLocation());
    }
    */
