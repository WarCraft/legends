package gg.warcraft.legends.spigot.OLD.ai;

import net.minecraft.server.v1_12_R1.EntityAgeable;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_12_R1.PathfinderGoalSelector;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

public class NpcMaker {

    public static void makeNpc(LivingEntity entity, String name) {
        entity.setInvulnerable(true);
        try {
            EntityLiving nmsEntity = ((CraftLivingEntity) entity).getHandle();
            if (nmsEntity instanceof EntityInsentient) {
                EntityInsentient insentientEntity = (EntityInsentient) nmsEntity;
                if (insentientEntity instanceof EntityAgeable) {
                    ((EntityAgeable) insentientEntity).ageLocked = true;
                }

                PathfinderGoalSelector goal = PathfinderUtils.resetAndGetGoalSelectorFor(nmsEntity);

                goal.a(0, new PathfinderGoalLookAtPlayer(insentientEntity, EntityHuman.class, 8.0F));
                goal.a(1, new PathfinderGoalRandomLookaround(insentientEntity));

                insentientEntity.setCustomName(name);
            } else {
                System.out.println("Failed to make npc");
                System.out.println(entity.getType() + " is not an instance of an EntityInsentient");
            }
        } catch (IllegalAccessException ex) {
            System.out.println("Failed to set npc goal selectors");
            ex.printStackTrace();
        }
    }
}
