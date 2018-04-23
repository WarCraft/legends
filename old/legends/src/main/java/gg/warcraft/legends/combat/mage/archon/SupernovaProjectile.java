package gg.warcraft.legends.combat.mage.archon;

import gg.warcraft.legends.combat.effect.mage.archon.SupernovaProjectileEffect;
import gg.warcraft.monolith.combat.MonolithProjectile;
import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.effect.MonolithEffect;
import gg.warcraft.monolith.entity.EntityService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.util.Duration;
import gg.warcraft.monolith.util.hitbox.Hitbox;
import gg.warcraft.monolith.util.hitbox.SphericalHitbox;
import gg.warcraft.monolith.util.movement.LinearMovement;
import gg.warcraft.monolith.util.movement.Movement;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class SupernovaProjectile extends SimpleMonolithAbility implements MonolithProjectile.Observer {
    private static final double MAX_DISTANCE = 30;

    private final Movement movement;
    private final MonolithEffect effect;

    public SupernovaProjectile(MonolithEntity caster, double radius) {
        this(EntityService.getInstance(), caster, radius);
    }

    public SupernovaProjectile(EntityService entityService, MonolithEntity caster, double radius) {
        super(caster);
        Location location = caster.getEyeLocation();
        Vector direction = caster.getEyeLocation().getDirection();
        this.movement = new LinearMovement(location, direction, MAX_DISTANCE);
        Hitbox hitbox = new SphericalHitbox(entityService, radius, entity -> entity != caster);
        MonolithProjectile projectile = new MonolithProjectile(movement, hitbox);
        projectile.addObserver(this);
        this.effect = new SupernovaProjectileEffect(movement::getLocation);
    }

    @Override
    public void onGrounded(MonolithProjectile projectile, Location location) {
        if (location.getBlock().getRelative(BlockFace.UP).isEmpty()) {
            new SupernovaVacuum(getCaster(), movement.getLocation(), 10, Duration.fromSeconds(5)); // TODO: Get values from config, see vaccuum todo
            effect.cancel();
        }
    }

    @Override
    public String toString() {
        return "Supernova";
    }
}
