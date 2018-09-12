package gg.warcraft.legends.spigot.OLD.ai;

import de.slikey.effectlib.util.ReflectionUtils;
import org.bukkit.entity.Entity;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static de.slikey.effectlib.util.ReflectionUtils.PackageType;

public interface CustomEntity {

    enum Type {
        // MONOLITH_ZOMBIE("Zombie", 54, MonolithZombie.class),
        // MONOLITH_GOLEM("VillagerGolem", 99, MonolithGolem.class)
        ;

        Type(String NMSEntityName, int NMSEntityID, Class<? extends Entity> customEntityClass) {
            registerCustomEntity(NMSEntityName, NMSEntityID, customEntityClass);
        }

        @SuppressWarnings("unchecked")
        private void registerCustomEntity(String NMSEntityName, int NMSEntityID, Class<? extends Entity> customEntityClass) {
            try {
                Field c = ReflectionUtils.getField("EntityTypes", PackageType.MINECRAFT_SERVER, true, "c");
                ((Map<Object, Object>) c.get(null)).put(NMSEntityName, customEntityClass);

                Field d = ReflectionUtils.getField("EntityTypes", PackageType.MINECRAFT_SERVER, true, "d");
                ((Map<Object, Object>) d.get(null)).put(customEntityClass, NMSEntityName);

                Field f = ReflectionUtils.getField("EntityTypes", PackageType.MINECRAFT_SERVER, true, "f");
                ((Map<Object, Object>) f.get(null)).put(customEntityClass, NMSEntityID);
            } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException ex) {
                throw new IllegalArgumentException("Could not register custom entity '" + customEntityClass + "': " + ex.getMessage());
            }
        }
    }

    @SuppressWarnings("unchecked")
    default void clearPathFinderGoalSelectors(Object goalSelector, Object targetSelector) {
        try {
            Field b = ReflectionUtils.getField("PathfinderGoalSelector", PackageType.MINECRAFT_SERVER, true, "b");
            ((List<Object>) b.get(goalSelector)).clear();
            ((List<Object>) b.get(targetSelector)).clear();

            Field c = ReflectionUtils.getField("PathfinderGoalSelector", PackageType.MINECRAFT_SERVER, true, "c");
            ((List<Object>) c.get(goalSelector)).clear();
            ((List<Object>) c.get(targetSelector)).clear();
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException ex) {
            throw new IllegalArgumentException("Could not clear pathfinder goals: " + ex.getMessage());
        }
    }
}
