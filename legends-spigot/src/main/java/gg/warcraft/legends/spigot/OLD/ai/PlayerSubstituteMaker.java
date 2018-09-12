package gg.warcraft.legends.spigot.OLD.ai;

import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_12_R1.PathfinderGoalSelector;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class PlayerSubstituteMaker {

    public static void makeSubstitute(LivingEntity entity, Player substituted) {
        try {
            EntityLiving nmsEntity = ((CraftLivingEntity) entity).getHandle();
            PathfinderGoalSelector goal = PathfinderUtils.getGoalSelectorFor(nmsEntity);
            PathfinderGoalSelector target = PathfinderUtils.getTargetSelectorFor(nmsEntity);

            if (nmsEntity instanceof EntityInsentient) {
                EntityInsentient insentientEntity = (EntityInsentient) nmsEntity;

                goal.a(0, new PathfinderGoalRandomLookaround(insentientEntity));
            }
        } catch (IllegalAccessException ex) {
            System.out.println("Failed to create player substitute mob");
            ex.printStackTrace();
        }
    }
}
