/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.ai

import java.lang.reflect.Field

import net.minecraft.server.v1_15_R1.{
  EntityInsentient, EntityLiving, PathfinderGoalSelector
}

import scala.collection.mutable
import scala.util.chaining._

object SpigotPathfinderUtils {
  private val pathfinderB: Field =
    classOf[PathfinderGoalSelector].getDeclaredField("b")
  pathfinderB.setAccessible(true)

  private val pathfinderC: Field =
    classOf[PathfinderGoalSelector].getDeclaredField("c")
  pathfinderC.setAccessible(true)

  private val goalSelectorField: Field =
    classOf[EntityInsentient].getDeclaredField("goalSelector")
  goalSelectorField.setAccessible(true)

  private val targetSelectorField: Field =
    classOf[EntityInsentient].getDeclaredField("targetSelector")
  targetSelectorField.setAccessible(true)

  def resetGoalSelector(entity: EntityLiving): PathfinderGoalSelector =
    goalSelectorField.get(entity).asInstanceOf[PathfinderGoalSelector]
        .tap { goal =>
          pathfinderB.set(goal, new mutable.HashSet[Any]())
          pathfinderC.set(goal, new mutable.HashSet[Any]())
        }

  def resetTargetSelector(entity: EntityLiving): PathfinderGoalSelector =
    targetSelectorField.get(entity).asInstanceOf[PathfinderGoalSelector]
        .tap { target =>
          pathfinderB.set(target, new mutable.HashSet[Any]())
          pathfinderC.set(target, new mutable.HashSet[Any]())
        }

  def resetSelectors(entity: EntityLiving): Unit = {
    val nmsEntity = entity.asInstanceOf[EntityInsentient]
    val goalSelector = nmsEntity.goalSelector
    val targetSelector = nmsEntity.targetSelector

    /*
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
     */

    /*
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
     */
  }
}
