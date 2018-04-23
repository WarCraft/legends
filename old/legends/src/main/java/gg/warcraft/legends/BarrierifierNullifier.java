package gg.warcraft.legends;

import gg.warcraft.monolith.util.RunnableUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class BarrierifierNullifier implements Listener {

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (!event.hasBlock()) {
            return;
        }

        Block startingBlock = event.getClickedBlock();
        if (startingBlock.getType() != Material.BARRIER) {
            return;
        }

        if (event.getPlayer().getInventory().getItemInMainHand().getType() != Material.FLINT) {
            return;
        }

        event.setCancelled(true);
        Runnable runnable = new Runnable() {
            private Block block = startingBlock;

            @Override
            public void run() {
                block.setType(Material.AIR);

                Block below = block.getRelative(BlockFace.DOWN);
                while (below.getType() == Material.BARRIER && below.getY() >= 0) {
                    below.setType(Material.AIR);
                    below = below.getRelative(BlockFace.DOWN);
                }

                Block above = block.getRelative(BlockFace.UP);
                while (above.getY() <= 100) {
                    if (above.getType() == Material.BARRIER) {
                        above.setType(Material.AIR);
                    }
                    above = above.getRelative(BlockFace.UP);
                }

                System.out.println("1 Barrier line RESET");
            }
        };

        RunnableUtils.runOnNextTick(runnable);
    }
}
