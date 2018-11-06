package gg.warcraft.legends.spigot.OLD.ai;

import com.google.inject.Inject;
import gg.warcraft.legends.api.ai.CombatPetFactory;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.EntityTameableAnimal;
import net.minecraft.server.v1_12_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_12_R1.PathfinderGoalFollowOwner;
import net.minecraft.server.v1_12_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_12_R1.PathfinderGoalLeapAtTarget;
import net.minecraft.server.v1_12_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_12_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_12_R1.PathfinderGoalOwnerHurtByTarget;
import net.minecraft.server.v1_12_R1.PathfinderGoalOwnerHurtTarget;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_12_R1.PathfinderGoalSelector;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
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

        Entity owner = server.getEntity(ownerId);
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
                tameableEntity.setCustomName(ChatColor.LIGHT_PURPLE + owner.getName() + "'s pet");

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
                target.a(3, new PathfinderGoalHurtByTarget(tameableEntity, true));
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
