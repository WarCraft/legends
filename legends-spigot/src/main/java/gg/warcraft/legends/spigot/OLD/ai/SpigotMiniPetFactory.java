package gg.warcraft.legends.spigot.OLD.ai;

import com.google.inject.Inject;
import gg.warcraft.legends.api.ai.MiniPetFactory;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SpigotMiniPetFactory implements MiniPetFactory {
    private final Server server;

    @Inject
    public SpigotMiniPetFactory(Server server) {
        this.server = server;
    }

    @Override
    public UUID create(UUID entityId, UUID ownerId) {
        Entity baseEntity = server.getEntity(entityId);
        if (!(baseEntity instanceof LivingEntity)) {
            return null;
        }

        Entity owner = server.getEntity(ownerId);
        LivingEntity entity = (LivingEntity) baseEntity;
        entity.setInvulnerable(true);
        try {
            EntityLiving nmsEntity = ((CraftLivingEntity) entity).getHandle();
            if (nmsEntity instanceof EntityInsentient) {
                EntityInsentient insentientEntity = (EntityInsentient) nmsEntity;
                if (insentientEntity instanceof EntityAgeable) {
                    ((EntityAgeable) insentientEntity).ageLocked = true;
                }

                PathfinderGoalSelector goal = PathfinderUtils.resetAndGetGoalSelectorFor(nmsEntity);

                goal.a(0, new PathfinderGoalFloat(insentientEntity));
                goal.a(1, new PathfinderGoalFollowOwnerReliable(entity, (Player) owner));
                goal.a(2, new PathfinderGoalLookAtPlayer(insentientEntity, EntityHuman.class, 8.0F));
                goal.a(2, new PathfinderGoalRandomLookaround(insentientEntity));

                insentientEntity.setCustomName(new ChatComponentText(ChatColor.LIGHT_PURPLE + owner.getName() + "'s pet"));
            } else {
                System.out.println("Failed to make pet");
                System.out.println(entity.getType() + " is not an instance of an EntityInsentient");
            }
        } catch (IllegalAccessException ex) {
            System.out.println("Failed to set pet goal selectors");
            ex.printStackTrace();
        }

        return entityId;
    }
}
