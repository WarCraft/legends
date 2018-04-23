package gg.warcraft.legends.world.placeable;

import gg.warcraft.legends.world.BlockData;
import gg.warcraft.legends.world.placeable.collector.BlockCollector;
import gg.warcraft.legends.world.placeable.spoofer.BlockSpoofer;
import gg.warcraft.legends.world.placeable.validator.GroundTargetValidator;
import gg.warcraft.monolith.Monolith;
import gg.warcraft.monolith.event.EventService;
import gg.warcraft.monolith.player.MonolithPlayer;
import gg.warcraft.monolith.util.BoundingBlockBox;
import gg.warcraft.monolith.util.Cancellable;
import gg.warcraft.monolith.util.DirectionUtils;
import gg.warcraft.monolith.util.MaterialUtils;
import gg.warcraft.monolith.util.RunnableUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class SimpleGroundTargeter implements GroundTargeter, Runnable {
    private static final String METADATA_KEY = GroundTargeter.class.getCanonicalName();
    private static final byte GREEN = (byte) 5;
    private static final byte RED = (byte) 14;

    static {
        EventService.getInstance().registerForEvents(new GroundTargeterListener());
    }

    private final MonolithPlayer placer;
    private final Placeable placeable;
    private final Function<MonolithPlayer, Location> targeter;
    private final BlockCollector collector;
    private final GroundTargetValidator validator;
    private final BlockSpoofer spoofer;
    private final Cancellable task;

    private Location currentTarget;
    private boolean isValidArea;

    public SimpleGroundTargeter(MonolithPlayer placer, Placeable placeable, Function<MonolithPlayer, Location> targeter,
                                BlockCollector collector, GroundTargetValidator validator, BlockSpoofer spoofer) {
        this.placer = placer;
        this.placeable = placeable;
        this.targeter = targeter;
        this.collector = collector;
        this.validator = validator;
        this.spoofer = spoofer;
        this.task = RunnableUtils.runTask(this, 1, 1);
        MetadataValue metadata = new FixedMetadataValue(Monolith.getInstance(), this);
        placer.setMetadata(METADATA_KEY, metadata);
    }

    @Override
    public void target(Location location, BlockFace direction) {
        if (location == null || MaterialUtils.NON_SOLIDS.contains(location.getBlock().getType())) {
            spoofer.undo();
        } else {
            BoundingBlockBox area = new BoundingBlockBox(placeable.getOffsets(), location, direction);
            List<Block> blocks = collector.collectFrom(area);
            isValidArea = blocks.stream().allMatch(block -> validator.isValidLocation(block.getLocation()));
            byte colorByte = isValidArea ? GREEN : RED;
            Map<Block, BlockData> toSpoof = new HashMap<>();
            blocks.forEach(block -> toSpoof.put(block, new BlockData(Material.WOOL, colorByte)));
            spoofer.spoof(toSpoof);
        }
    }

    @Override
    public boolean isValid() {
        return isValidArea;
    }

    @Override
    public void place() {
        cancel();
        placeable.place(placer, currentTarget.add(0, 1, 0), DirectionUtils.getDirectionFor(placer));
    }

    @Override
    public void cancel() {
        task.cancel();
        spoofer.undo();
        placer.removeMetadata(METADATA_KEY, Monolith.getInstance());
    }

    @Override
    public void run() {
        if (placer.isOnline()) {
            Location target = targeter.apply(placer);
            if (target != currentTarget) {
                target(target, DirectionUtils.getDirectionFor(placer)); // TODO abstract direction into own functionality
                currentTarget = target;
            }
        } else {
            cancel();
        }
    }
}
