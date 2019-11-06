package gg.warcraft.legends.spigot.OLD.ai;

import com.google.inject.Inject;
import gg.warcraft.legends.api.ai.CombatPetFactory;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;

public class SpigotCombatPetFactory implements CombatPetFactory {
    private final Server server;

    @Inject
    public SpigotCombatPetFactory(Server server) {
        this.server = server;
    }

    @Override
    public UUID create(UUID entityId, UUID ownerId) {
        Entity baseEntity = server.getEntity(entityId);
        if (!(baseEntity instanceof LivingEntity)) {
            return null;
        }

        Entity owner = server.getPlayer(ownerId);
        if (owner == null) {
            owner = server.getEntity(ownerId);
            if (owner == null) {
                return null;
            }
        }

        LivingEntity entity = (LivingEntity) baseEntity;
        try {
            EntityLiving nmsEntity = ((CraftLivingEntity) entity).getHandle();
            if (nmsEntity instanceof EntityTameableAnimal) {
                // TODO does this do anything?
                double baseFollowRange = entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).getBaseValue();
                entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(baseFollowRange * 1.75);

                EntityTameableAnimal tameableEntity = (EntityTameableAnimal) nmsEntity;
                tameableEntity.setTamed(true);
                tameableEntity.setOwnerUUID(ownerId);
                tameableEntity.setCustomName(new ChatComponentText(ChatColor.LIGHT_PURPLE + owner.getName() + "'s pet"));

                PathfinderGoalSelector goal = PathfinderUtils.resetAndGetGoalSelectorFor(nmsEntity);
                PathfinderGoalSelector target = PathfinderUtils.resetAndGetTargetSelectorFor(nmsEntity);

                goal.a(0, new PathfinderGoalFloat(tameableEntity));
                goal.a(1, new PathfinderGoalLeapAtTarget(tameableEntity, 0.4F));
                // NOTE is 1.3D movement speed? (seems like it)
                goal.a(2, new PathfinderGoalMeleeAttack(tameableEntity, 1.3D, false));

                goal.a(3, new PathfinderGoalFollowOwner(tameableEntity, 1.3D, 3.0F, 2.0F));
                // goal.a(2, new PathfinderGoalFollowOwnerReliable(entity, owner));
                goal.a(4, new PathfinderGoalLookAtPlayer(tameableEntity, EntityHuman.class, 8.0F));
                goal.a(4, new PathfinderGoalRandomLookaround(tameableEntity));

                target.a(1, new PathfinderGoalOwnerHurtTarget(tameableEntity));
                target.a(2, new PathfinderGoalOwnerHurtByTarget(tameableEntity));
                target.a(3, new PathfinderGoalHurtByTarget(tameableEntity, LivingEntity.class));
            } else {
                System.out.println("Failed to make combat pet, " + entity.getType() +
                        " is not an instance of an EntityTameableAnimal");
            }
        } catch (IllegalAccessException ex) {
            System.out.println("Failed to set combat pet goal selectors");
            ex.printStackTrace();
        }

        return entityId;
    }
}
