package gg.warcraft.legends.world.placeable;

import gg.warcraft.legends.world.placeable.collector.FloorBlockCollector;
import gg.warcraft.legends.world.placeable.spoofer.SimpleBlockSpoofer;
import gg.warcraft.legends.world.placeable.validator.OneUpDownMaxGroundTargetValidator;
import gg.warcraft.monolith.player.MonolithPlayer;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public class FootGroundTargeter extends SimpleGroundTargeter implements Runnable {

    public FootGroundTargeter(MonolithPlayer placer, Placeable placeable) {
        super(placer, placeable, FootGroundTargeter::getTarget, new FloorBlockCollector(),
                new OneUpDownMaxGroundTargetValidator(), new SimpleBlockSpoofer(placer));
    }

    private static Location getTarget(MonolithPlayer placer) {
        return placer.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation();
    }
}
