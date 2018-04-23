package gg.warcraft.legends.siege;

import gg.warcraft.monolith.Monolith;
import gg.warcraft.monolith.event.EventService;
import gg.warcraft.monolith.util.RunnableUtils;
import gg.warcraft.monolith.util.TimeUtils;
import gg.warcraft.monolith.world.BlockBackup;
import gg.warcraft.monolith.world.WorldService;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class SiegeExplosion {
    private static final String METADATA_KEY = SiegeExplosion.class.getCanonicalName();
    private static final long RESPAWN_TIMER = 10 * TimeUtils.TICKS_PER_MINUTE;

    static {
        EventService.getInstance().registerForEvents(new Listener() {
            @EventHandler
            public void onBlockExplodeEvent(BlockExplodeEvent event) {
                List<MetadataValue> metadata = event.getBlock().getMetadata(METADATA_KEY);
                if (!metadata.isEmpty()) {
                    List<Block> blocks = event.blockList();
                    BlockBackup backup = WorldService.getInstance().createBackup(blocks);
                    event.setYield(0);
                    blocks.forEach(block -> block.setType(Material.AIR));
                    event.getBlock().removeMetadata(METADATA_KEY, Monolith.getInstance());

                    RunnableUtils.runLater(() -> WorldService.getInstance().restoreBackup(backup), RESPAWN_TIMER);
                }
            }
        });
    }

    public SiegeExplosion(Location location, float strength) {
        FixedMetadataValue metadata = new FixedMetadataValue(Monolith.getInstance(), this);
        location.getBlock().setMetadata(METADATA_KEY, metadata);
        location.getWorld().createExplosion(location, strength);
    }
}
