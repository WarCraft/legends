package gg.warcraft.legends.world.placeable;

import gg.warcraft.monolith.player.MonolithPlayer;
import gg.warcraft.monolith.util.BlockOffsets;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public interface Placeable {

    BlockOffsets getOffsets();

    void place(MonolithPlayer placer, Location location, BlockFace direction);
}
