package gg.warcraft.legends.world;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.List;

public class Hologram {
    private static final double LINE_HEIGHT = 0.33;

    public Hologram(Location location, List<String> text) {
        World world = location.getWorld();

        // remove existing stands in case text updated
        world.getNearbyEntities(location, 1, 1 + LINE_HEIGHT * text.size(), 1).forEach(entity -> {
            if (entity.getType() == EntityType.ARMOR_STAND) {
                entity.remove();
            }
        });

        double yOffset = 0;
        for (String line : Lists.reverse(text)) {
            if (line != null && !line.isEmpty()) {
                Location armorStandLocation = location.clone();
                armorStandLocation.add(0, yOffset, 0);

                ArmorStand armorStand = (ArmorStand) world.spawnEntity(armorStandLocation, EntityType.ARMOR_STAND);
                armorStand.setSmall(true);
                armorStand.setCollidable(false);
                armorStand.setGravity(false);
                armorStand.setVisible(false);
                armorStand.setInvulnerable(true);
                armorStand.setCustomName(line);
                armorStand.setCustomNameVisible(true);
            }

            yOffset += LINE_HEIGHT;
        }
    }
}
