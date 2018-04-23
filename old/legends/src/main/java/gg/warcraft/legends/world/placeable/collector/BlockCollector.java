package gg.warcraft.legends.world.placeable.collector;

import gg.warcraft.monolith.util.BoundingBlockBox;
import org.bukkit.block.Block;

import java.util.List;

public interface BlockCollector {

    List<Block> collectFrom(BoundingBlockBox area);
}
