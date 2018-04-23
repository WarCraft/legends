package gg.warcraft.legends.world.placeable.spoofer;

import gg.warcraft.legends.world.BlockData;
import org.bukkit.block.Block;

import java.util.Map;

public interface BlockSpoofer {

    void spoof(Map<Block, BlockData> blocks);

    void undo();
}
