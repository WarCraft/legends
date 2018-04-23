package gg.warcraft.legends.world.placeable;

import gg.warcraft.monolith.util.Cancellable;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public interface GroundTargeter extends Cancellable {

    void target(Location location, BlockFace direction);

    boolean isValid();

    void place();
}
