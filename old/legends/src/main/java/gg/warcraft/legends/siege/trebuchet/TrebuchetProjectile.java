package gg.warcraft.legends.siege.trebuchet;

import gg.warcraft.legends.LegendsSiegeSound;
import gg.warcraft.legends.siege.SiegeExplosion;
import gg.warcraft.legends.siege.effect.trebuchet.TrebuchetProjectileEffect;
import gg.warcraft.monolith.combat.MonolithProjectile;
import gg.warcraft.monolith.entity.EntityService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.javafx.geometry.BoundingBox;
import gg.warcraft.monolith.javafx.geometry.Point3D;
import gg.warcraft.monolith.sound.MonolithSound;
import gg.warcraft.monolith.util.CancellableGroup;
import gg.warcraft.monolith.util.RunnableUtils;
import gg.warcraft.monolith.util.hitbox.Hitbox;
import gg.warcraft.monolith.util.hitbox.SphericalHitbox;
import gg.warcraft.monolith.util.movement.Movement;
import gg.warcraft.monolith.util.movement.ProjectileMovement;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.Collection;

public class TrebuchetProjectile implements MonolithProjectile.Observer {
    private static final double MAX_DISTANCE = 100;
    private static final float EXPLOSION_STRENGTH = 2;

    private final Location location;
    private final Vector velocity;
    private final double damage;
    private final CancellableGroup tasks;
    private final MonolithSound onHitSound;
    private final MonolithSound onGroundedSound;

    public TrebuchetProjectile(Location location, Vector velocity, double damage, double radius) {
        this(EntityService.getInstance(), LegendsSiegeSound.TREBUCHET_CAST, LegendsSiegeSound.TREBUCHET_HIT,
                LegendsSiegeSound.TREBUCHET_GROUNDED, location, velocity, damage, radius);
    }

    public TrebuchetProjectile(EntityService entityService,
                               MonolithSound onCastSound, MonolithSound onHitSound, MonolithSound onGroundedSound,
                               Location location, Vector velocity, double damage, double radius) {
        this.location = location;
        this.velocity = velocity;
        this.damage = damage;
        this.tasks = new CancellableGroup();
        this.onHitSound = onHitSound;
        this.onGroundedSound = onGroundedSound;

        Movement movement = new ProjectileMovement(location, velocity, MAX_DISTANCE);
        Hitbox hitbox = new SphericalHitbox(entityService, radius, entity -> true); // TODO filter out friendly fire on race?
        MonolithProjectile projectile = new MonolithProjectile(movement, hitbox);
        projectile.addObserver(this);
        tasks.add(RunnableUtils.runTask(projectile, 1, 1));

        tasks.add(new TrebuchetProjectileEffect(movement::getLocation));

        onCastSound.play(location);
    }

    @Override
    public String toString() {
        return "Trebuchet Projectile";
    }

    @Override
    public void onHit(MonolithProjectile projectile, Collection<MonolithEntity> casualties) {
        onHitSound.play(casualties.iterator().next().getLocation());
        casualties.forEach(entity -> entity.damage(damage));
        // TODO we don't cancel here, siege projectiles will demolish players and keep flying till they hit
        // TODO a block, might want to cache already hit players tho so they're not obliterated 20 times per second
        // TODO as they stand in the path of the hitbox
    }

    // TODO move into common place, OneUpDownMaxGroundTargetValidator also has this duplicated
    public static final BoundingBox humanCapital = new BoundingBox(-1570, 50, 141, 308, 105, 295);
    public static final BoundingBox orcCapital = new BoundingBox(186, 42, 112, 364, 76, 429);

    public static boolean isInCapital(Block block) {
        Point3D blockPoint = new Point3D(block.getX(), block.getY(), block.getZ());
        return humanCapital.contains(blockPoint) || orcCapital.contains(blockPoint);
    }

    @Override
    public void onGrounded(MonolithProjectile projectile, Location location) {
        if (!isInCapital(location.getBlock())) {
            onGroundedSound.play(location);
            new SiegeExplosion(location, EXPLOSION_STRENGTH);
        }
        tasks.cancel();
    }

    @Override
    public void onMaxDistanceReached(MonolithProjectile projectile, double distance) {
        tasks.cancel();
    }
}
