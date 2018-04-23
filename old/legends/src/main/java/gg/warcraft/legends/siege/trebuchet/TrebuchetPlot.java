package gg.warcraft.legends.siege.trebuchet;

import gg.warcraft.monolith.Monolith;
import gg.warcraft.monolith.event.EventService;
import gg.warcraft.monolith.util.BlockCopySession;
import gg.warcraft.monolith.util.BlockOffsets;
import gg.warcraft.monolith.util.BoundingBlockBox;
import gg.warcraft.monolith.util.MonolithTask;
import gg.warcraft.monolith.util.RunnableUtils;
import gg.warcraft.monolith.world.BlockBackup;
import gg.warcraft.monolith.world.WorldService;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.util.Vector;

import java.util.List;

public class TrebuchetPlot implements BlockCopySession.Observer {
    private static final String METADATA_KEY = TrebuchetPlot.class.getCanonicalName();
    private static final long DESPAWN_COOLDOWN_TICKS = 5 * 60 * 20;
    private static final int REQUIRED_WOOD = 20;
    private static final int REQUIRED_STONE = 5;

    private static final BoundingBlockBox source;

    static {
        source = new BoundingBlockBox(
                new BlockOffsets(7, 7, 2, 2, 0, 0),
                new Location(Monolith.getInstance().getWorld(), -35, 5, -6019),
                BlockFace.NORTH);
    }

    private final WorldService worldService;
    private final BoundingBlockBox location;
    private final BlockBackup backup;

    private Location safeSpot;
    private int wood;
    private int stone;
    private MonolithTask restoreTask;

    public TrebuchetPlot(BoundingBlockBox location) {
        this(WorldService.getInstance(), location);
    }

    public TrebuchetPlot(WorldService worldService, BoundingBlockBox location) {
        this.worldService = worldService;
        this.location = location;
        BlockCopySession copySession = new BlockCopySession(source, location);
        copySession.addObserver(this);
        this.backup = worldService.createBackup(copySession);

        if (safeSpot != null) {
            double height = source.getUpperBoundary() - source.getLowerBoundary();
            BlockOffsets safeOffsets = new BlockOffsets(
                    location.getOffsets().getFrontOffset(),
                    location.getOffsets().getBackOffset(),
                    location.getOffsets().getRightOffset(),
                    location.getOffsets().getLeftOffset(),
                    height,
                    0
            );
            BoundingBlockBox safeArea = new BoundingBlockBox(safeOffsets, location.getLocation(), location.getFacing());
            safeArea.getNearbyLivingEntities().forEach(entity -> {
                Vector direction = entity.getLocation().getDirection();
                safeSpot.setDirection(direction);
                entity.teleport(safeSpot);
            });
        } else {
            System.out.println("Failed to TP entities to safe spot for TrebuchetPlot, safe spot was null!");
        }

        resetRestoreTask();
    }

    // treb should restore itself after 5 minutes of inactivity
    private void resetRestoreTask() {
        if (restoreTask != null) {
            restoreTask.cancel();
        }
        restoreTask = RunnableUtils.runLater(() -> WorldService.getInstance().restoreBackup(backup), DESPAWN_COOLDOWN_TICKS);
    }

    public void addWood(int amount) {
        wood += amount;
        if (wood > REQUIRED_WOOD) {
            wood = REQUIRED_WOOD;
        }
        if (wood >= REQUIRED_WOOD && stone >= REQUIRED_STONE) {
            buildTrebuchet();
        }

        resetRestoreTask();
    }

    public void addStone(int amount) {
        stone += amount;
        if (stone > REQUIRED_STONE) {
            stone = REQUIRED_STONE;
        }
        if (stone >= REQUIRED_STONE && wood >= REQUIRED_WOOD) {
            buildTrebuchet();
        }

        resetRestoreTask();
    }

    public int getRemainingWood() {
        return REQUIRED_WOOD - wood;
    }

    public int getRemainingStone() {
        return REQUIRED_STONE - stone;
    }

    private void buildTrebuchet() {
        restoreTask.cancel();
        worldService.restoreBackup(backup);
        new SimpleTrebuchet(location);
    }

    @Override
    public void onBlockChange(BlockState previous, BlockState current) {
        if (current.getType() == Material.CHEST) {
            MetadataValue metadata = new FixedMetadataValue(Monolith.getInstance(), this);
            current.getBlock().setMetadata(METADATA_KEY, metadata);
            return;
        }

        if (current.getType() == Material.BEDROCK && previous.getType() != Material.BEDROCK) {
            safeSpot = current.getLocation().add(0.5, 0, 0.5);
            previous.update(true, false);
            return;
        }
    }

    static {
        EventService.getInstance().registerForEvents(new Listener() {
            @EventHandler(ignoreCancelled = true)
            public void onPlayerInteractEvent(PlayerInteractEvent event) {
                List<MetadataValue> metadata = event.getClickedBlock().getMetadata(METADATA_KEY);
                if (!metadata.isEmpty()) {
                    event.setCancelled(true);
                    TrebuchetPlot plot = (TrebuchetPlot) metadata.get(0).value();
                    event.getPlayer().openInventory(new TrebuchetPlotBuildMaterialMenu(event.getPlayer(), plot));
                }
            }
        });
    }
}
