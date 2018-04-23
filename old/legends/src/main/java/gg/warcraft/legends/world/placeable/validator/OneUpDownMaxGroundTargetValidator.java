package gg.warcraft.legends.world.placeable.validator;

import gg.warcraft.monolith.javafx.geometry.BoundingBox;
import gg.warcraft.monolith.javafx.geometry.Point3D;
import gg.warcraft.monolith.util.MaterialUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class OneUpDownMaxGroundTargetValidator implements GroundTargetValidator {
    private static final BoundingBox humanCapital = new BoundingBox(-1570, 50, 141, 308, 105, 295);
    private static final BoundingBox orcCapital = new BoundingBox(186, 42, 112, 364, 76, 429);

    private boolean isInCapital(Block block) {
        Point3D blockPoint = new Point3D(block.getX(), block.getY(), block.getZ());
        return humanCapital.contains(blockPoint) || orcCapital.contains(blockPoint);
    }

    @Override
    public boolean isValidLocation(Location location) {
        Block block = location.getBlock();

        if (isInCapital(block)) {
            return false;
        }

        if (MaterialUtils.NON_SOLIDS.contains(block.getType()) &&
                MaterialUtils.NON_SOLIDS.contains(block.getRelative(BlockFace.DOWN).getType())) {
            return false;
        } else {
            if (!MaterialUtils.NON_SOLIDS.contains(block.getRelative(BlockFace.UP).getType()) &&
                    !MaterialUtils.NON_SOLIDS.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getType())) {
                return false;
            }
        }
        return true;
    }
}
