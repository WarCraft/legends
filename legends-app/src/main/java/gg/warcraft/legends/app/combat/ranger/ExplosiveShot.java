package gg.warcraft.legends.app.combat.ranger;

import gg.warcraft.monolith.combat.HostileTo;
import gg.warcraft.monolith.combat.MonolithArrow;
import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.entity.EntityService;
import gg.warcraft.monolith.entity.MonolithEntity;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;

public class ExplosiveShot extends SimpleMonolithAbility implements MonolithArrow.Observer {

    private final EntityService entityService;
    private final double damage;
    private final double knockback;
    private final double radius;

    public ExplosiveShot(MonolithEntity caster, double damage, double knockback, double radius) {
        this(EntityService.getInstance(), caster, damage, knockback, radius);
    }

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

    @Override
    public String toString() {
        return "Explosive Shot";
    }
}
