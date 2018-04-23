package gg.warcraft.legends.world.placeable.collector;

import gg.warcraft.monolith.util.BoundingBlockBox;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class FloorBlockCollector implements BlockCollector {

    @Override
    public List<Block> collectFrom(BoundingBlockBox area) {
        List<Block> blocks = new ArrayList<>();
        World world = area.getLocation().getWorld();
        double y = area.getLowerBoundary();
        for (double x = area.getWestBoundary(); x <= area.getEastBoundary(); x += 1) {
            for (double z = area.getNorthBoundary(); z <= area.getSouthBoundary(); z += 1) {
                blocks.add(world.getBlockAt((int) x, (int) y, (int) z));
            }
        }
        return blocks;
    }
}
