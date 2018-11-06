package gg.warcraft.legends.spigot.OLD.ai;

import com.google.common.collect.Sets;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.PathfinderGoalSelector;

import java.lang.reflect.Field;

public class PathfinderUtils {
    private static Field pathfinderB;
    private static Field pathfinderC;
    private static Field goalSelectorField;
    private static Field targetSelectorField;

    static {
        try {
            pathfinderB = PathfinderGoalSelector.class.getDeclaredField("b");
            pathfinderB.setAccessible(true);

            pathfinderC = PathfinderGoalSelector.class.getDeclaredField("c");
            pathfinderC.setAccessible(true);

            goalSelectorField = EntityInsentient.class.getDeclaredField("goalSelector");
            goalSelectorField.setAccessible(true);

            targetSelectorField = EntityInsentient.class.getDeclaredField("targetSelector");
            targetSelectorField.setAccessible(true);
        } catch (Exception e) {
            System.out.println("Failed to initialize static goal selectors in PathfinderUtils");
            e.printStackTrace();
        }
    }

    public static PathfinderGoalSelector resetAndGetGoalSelectorFor(EntityLiving nmsEntity) throws IllegalAccessException {
        PathfinderGoalSelector goal = (PathfinderGoalSelector) goalSelectorField.get(nmsEntity);
        pathfinderB.set(goal, Sets.newLinkedHashSet());
        pathfinderC.set(goal, Sets.newLinkedHashSet());
        return goal;
    }

    public static PathfinderGoalSelector resetAndGetTargetSelectorFor(EntityLiving nmsEntity) throws IllegalAccessException {
        PathfinderGoalSelector target = (PathfinderGoalSelector) targetSelectorField.get(nmsEntity);
        pathfinderB.set(target, Sets.newLinkedHashSet());
        pathfinderC.set(target, Sets.newLinkedHashSet());
        return target;
    }
}
