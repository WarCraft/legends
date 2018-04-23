package gg.warcraft.legends.world.placeable;

import gg.warcraft.legends.world.placeable.collector.FloorBlockCollector;
import gg.warcraft.legends.world.placeable.spoofer.SimpleBlockSpoofer;
import gg.warcraft.legends.world.placeable.validator.AlwaysTrueGroundTargetValidator;
import gg.warcraft.monolith.player.MonolithPlayer;
import gg.warcraft.monolith.util.MaterialUtils;
import org.bukkit.Location;

public class MouseGroundTargeter extends SimpleGroundTargeter implements Runnable {

    public MouseGroundTargeter(MonolithPlayer placer, Placeable placeable) {
        super(placer, placeable, MouseGroundTargeter::getTarget, new FloorBlockCollector(),
                new AlwaysTrueGroundTargetValidator(), new SimpleBlockSpoofer(placer));
    }

    private static Location getTarget(MonolithPlayer placer) {
        return placer.getTargetBlock(MaterialUtils.NON_SOLIDS, 30).getLocation();
    }
}
