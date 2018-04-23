package gg.warcraft.legends;

import gg.warcraft.monolith.util.RunnableUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.Optional;

public class Barrierifier implements Listener {

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (!event.hasBlock()) {
            return;
        }

        Block startingBlock = event.getClickedBlock();
        if (startingBlock.getType() != Material.PINK_GLAZED_TERRACOTTA) {
            return;
        }

        if (event.getPlayer().getInventory().getItemInMainHand().getType() != Material.BARRIER) {
            return;
        }

        event.setCancelled(true);
        Runnable runnable = new Runnable() {
            private Block block = startingBlock;
            private int iterations = 0;

            @Override
            public void run() {
                iterations += 1;
                block.setType(Material.BARRIER);

                Block below = block.getRelative(BlockFace.DOWN);
                while (below.getType() == Material.AIR && below.getY() >= 0) {
                    below.setType(Material.BARRIER);
                    below = below.getRelative(BlockFace.DOWN);
                }

                Block above = block.getRelative(BlockFace.UP);
                while (above.getY() <= 100) {
                    if (above.getType() == Material.AIR) {
                        above.setType(Material.BARRIER);
                    }
                    above = above.getRelative(BlockFace.UP);
                }

                Optional<Block> next = Arrays.stream(BlockFace.values())
                        .map(block::getRelative)
                        .filter(block -> block.getType() == Material.PINK_GLAZED_TERRACOTTA)
                        .findFirst();

                if (next.isPresent()) {
                    block = next.get();
                    RunnableUtils.runOnNextTick(this);
                } else {
                    System.out.println("Barrier run ended after " + iterations + " iterations");
                }
            }
        };

        RunnableUtils.runOnNextTick(runnable);
    }
}
