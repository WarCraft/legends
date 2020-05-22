package gg.warcraft.legends.spigot.OLD.ai;

import net.minecraft.server.v1_14_R1.EntityInsentient;
import net.minecraft.server.v1_14_R1.PathEntity;
import net.minecraft.server.v1_14_R1.PathfinderGoal;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

public class PathfinderGoalFollowOwnerReliable extends PathfinderGoal {
    private final LivingEntity spigotEntity;
    private final EntityInsentient nmsEntity;
    private final Player player;

    private PathEntity path;

    public PathfinderGoalFollowOwnerReliable(LivingEntity entity, Player toFollow) {
        this.spigotEntity = entity;
        this.nmsEntity = (EntityInsentient) ((CraftLivingEntity) entity).getHandle();
        this.player = toFollow;
    }

    @Override
    public boolean a() {
        if (!player.isOnline()) {
            spigotEntity.remove();
            return true;
        }

        Location targetLocation = player.getLocation();
        path = nmsEntity.getNavigation().a(targetLocation.getX(), targetLocation.getY(), targetLocation.getZ(), 1); // TODO what is this int?

        if (path != null) {
            c();
            return true;
        }
        return false;
    }

    @Override
    public void c() {
        Location targetLocation = player.getLocation();
        Vector entityLoc = new Vector(nmsEntity.locX, nmsEntity.locY, nmsEntity.locZ);
        double distanceToTarget = targetLocation.toVector().subtract(entityLoc).length();

        if (distanceToTarget > 14) { // distance of 15-20 seems to be where entities 'crash'
            spigotEntity.teleport(targetLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
        } else if (distanceToTarget > 3) {
            double speedBonusPercentage = Math.min(Math.pow(distanceToTarget, 2), 100) / 100;
            double speedBonus = speedBonusPercentage * 0.3;
            nmsEntity.getNavigation().a(path, 1.4 + speedBonus);
        }
    }
}
