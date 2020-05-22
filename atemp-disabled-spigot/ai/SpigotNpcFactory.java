package gg.warcraft.legends.spigot.OLD.ai;

import com.google.inject.Inject;
import gg.warcraft.legends.api.ai.NpcFactory;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class SpigotNpcFactory implements NpcFactory {
    private final Server server;

    @Inject
    public SpigotNpcFactory(Server server) {
        this.server = server;
    }

    @Override
    public UUID create(UUID entityId, String name) {
        Entity entity = server.getEntity(entityId);
        if (entity == null) {
            return null;
        }

        try {
            EntityLiving nmsEntity = ((CraftLivingEntity) entity).getHandle();
            if (nmsEntity instanceof EntityInsentient) {
                ((CraftLivingEntity) entity).setCollidable(false);
                nmsEntity.setInvulnerable(true);
                EntityInsentient insentientEntity = (EntityInsentient) nmsEntity;
                if (insentientEntity instanceof EntityAgeable) {
                    ((EntityAgeable) insentientEntity).ageLocked = true;
                }

                PathfinderUtils.resetSelectorsFor(nmsEntity);
//      OLD     PathfinderGoalSelector goal = PathfinderUtils.resetAndGetGoalSelectorFor(nmsEntity);
                PathfinderGoalSelector goal = ((EntityInsentient) nmsEntity).goalSelector;

                goal.a(0, new PathfinderGoalLookAtPlayer(insentientEntity, EntityHuman.class, 8.0F));
                goal.a(1, new PathfinderGoalRandomLookaround(insentientEntity));

                insentientEntity.setCustomName(new ChatComponentText(name));
            } else {
                System.out.println("Failed to make npc");
                System.out.println(entity.getType() + " is not an instance of an EntityInsentient");
            }
        } catch (Exception ex) {
            System.out.println("Failed to set npc goal selectors");
            ex.printStackTrace();
        }

        return entityId;
    }
}
