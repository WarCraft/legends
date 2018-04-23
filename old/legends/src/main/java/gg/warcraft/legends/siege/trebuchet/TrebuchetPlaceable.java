package gg.warcraft.legends.siege.trebuchet;

import gg.warcraft.legends.world.placeable.Placeable;
import gg.warcraft.monolith.player.MonolithPlayer;
import gg.warcraft.monolith.util.BlockOffsets;
import gg.warcraft.monolith.util.BoundingBlockBox;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public class TrebuchetPlaceable implements Placeable {

    private final BlockOffsets offsets;

    public TrebuchetPlaceable() {
        offsets = new BlockOffsets(7, 7, 2, 2, 0, 0); // TODO retrieve value from config service
    }

    @Override
    public BlockOffsets getOffsets() {
        return offsets;
    }

    @Override
    public void place(MonolithPlayer builder, Location location, BlockFace direction) {
        BoundingBlockBox area = new BoundingBlockBox(getOffsets(), location, direction);
        new TrebuchetPlot(area);
    }
}
