package gg.warcraft.legends.siege.bomb;

import gg.warcraft.legends.siege.SiegeExplosion;
import gg.warcraft.monolith.Monolith;
import gg.warcraft.monolith.util.BlockCopySession;
import gg.warcraft.monolith.util.BlockOffsets;
import gg.warcraft.monolith.util.BoundingBlockBox;
import gg.warcraft.monolith.util.RunnableUtils;
import gg.warcraft.monolith.util.TimeUtils;
import gg.warcraft.monolith.world.BlockBackup;
import gg.warcraft.monolith.world.WorldService;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public class SimpleBomb implements Bomb {
    private static final float EXPLOSION_STRENGTH = 1;
    private static final BoundingBlockBox source;

    static {
        source = new BoundingBlockBox(
                new BlockOffsets(0, 0, 0, 0, 0, 0),
                new Location(Monolith.getInstance().getWorld(), -51, 4, -6012),
                BlockFace.NORTH);
    }

    private final WorldService worldService;
    private final BoundingBlockBox area;
    private final BlockBackup backup;

    public SimpleBomb(BoundingBlockBox area) {
        this(WorldService.getInstance(), area);
    }

    public SimpleBomb(WorldService worldService, BoundingBlockBox area) {
        this.worldService = worldService;
        this.area = area;
        BlockCopySession copySession = new BlockCopySession(source, area);
        this.backup = worldService.createBackup(copySession);

        RunnableUtils.runLater(this::explode, 3 * TimeUtils.TICKS_PER_SECOND);
    }

    @Override
    public void explode() {
        new SiegeExplosion(area.getLocation(), EXPLOSION_STRENGTH);
        worldService.restoreBackup(backup);
    }
}
