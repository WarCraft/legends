package gg.warcraft.legends.spigot.OLD.ai;

import com.google.common.collect.Sets;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;

import java.lang.reflect.Field;
import java.util.*;

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

    public static void resetSelectorsFor(EntityLiving entity)  {
        EntityInsentient nmsEntity = (EntityInsentient) entity;
        PathfinderGoalSelector goalSelector = nmsEntity.goalSelector;
        PathfinderGoalSelector targetSelector = nmsEntity.targetSelector;

        try {
            Field brField = EntityLiving.class.getDeclaredField("br");
            brField.setAccessible(true);
            BehaviorController<?> controller = (BehaviorController<?>) brField.get(nmsEntity);

            Field memoriesField = BehaviorController.class.getDeclaredField("memories");
            memoriesField.setAccessible(true);
            memoriesField.set(controller, new HashMap<>());

            Field sensorsField = BehaviorController.class.getDeclaredField("sensors");
            sensorsField.setAccessible(true);
            sensorsField.set(controller, new LinkedHashMap<>());

            Field cField = BehaviorController.class.getDeclaredField("c");
            cField.setAccessible(true);
            cField.set(controller, new TreeMap<>());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IllegalStateException();
        }


        try {
            Field dField;
            dField = PathfinderGoalSelector.class.getDeclaredField("d");
            dField.setAccessible(true);
            dField.set(goalSelector, new LinkedHashSet<>());
            dField.set(targetSelector, new LinkedHashSet<>());

            Field cField;
            cField = PathfinderGoalSelector.class.getDeclaredField("c");
            cField.setAccessible(true);
            dField.set(goalSelector, new LinkedHashSet<>());
            cField.set(targetSelector, new EnumMap<>(PathfinderGoal.Type.class));

            Field fField;
            fField = PathfinderGoalSelector.class.getDeclaredField("f");
            fField.setAccessible(true);
            dField.set(goalSelector, new LinkedHashSet<>());
            fField.set(targetSelector, EnumSet.noneOf(PathfinderGoal.Type.class));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IllegalStateException();
        }
    }
}
