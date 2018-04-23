package gg.warcraft.legends.world.placeable;

import gg.warcraft.legends.world.placeable.collector.FloorBlockCollector;
import gg.warcraft.legends.world.placeable.spoofer.SimpleBlockSpoofer;
import gg.warcraft.legends.world.placeable.validator.OneUpDownMaxGroundTargetValidator;
import gg.warcraft.monolith.player.MonolithPlayer;
import gg.warcraft.monolith.util.DirectionUtils;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public class InFrontFootGroundTargeter extends SimpleGroundTargeter implements Runnable {

    public InFrontFootGroundTargeter(MonolithPlayer placer, Placeable placeable) {
        super(placer, placeable, InFrontFootGroundTargeter::getTarget, new FloorBlockCollector(),
                new OneUpDownMaxGroundTargetValidator(), new SimpleBlockSpoofer(placer));
    }

    private static Location getTarget(MonolithPlayer placer) {
        BlockFace direction = DirectionUtils.getDirectionFor(placer);
        return placer.getLocation().getBlock().getRelative(BlockFace.DOWN).getRelative(direction).getLocation();
    }
}
