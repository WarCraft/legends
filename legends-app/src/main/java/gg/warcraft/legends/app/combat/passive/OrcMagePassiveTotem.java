package gg.warcraft.legends.app.combat.passive;

import gg.warcraft.legends.combat.mage.orc.TotemFireballFactory;
import gg.warcraft.monolith.entity.EntityService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.util.MonolithTask;
import gg.warcraft.monolith.util.RunnableUtils;
import gg.warcraft.monolith.util.TimeUtils;
import gg.warcraft.monolith.world.BlockBackup;
import gg.warcraft.monolith.world.WorldService;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class OrcMagePassiveTotem {
    private static final int DURATION = 10 * TimeUtils.TICKS_PER_SECOND;
    private static final WorldService worldService = WorldService.getInstance();
    private static final EntityService entityService = EntityService.getInstance();
    private static final Random random = new Random(System.currentTimeMillis());
    private static final TotemFireballFactory totemFireballFactory = new TotemFireballFactory();

    private final MonolithEntity caster;
    private final BlockBackup totemBackup;
    private final Block totem;
    private final MonolithTask shootTask;

    public OrcMagePassiveTotem(MonolithEntity caster) {
        this.caster = caster;
        List<Block> surroundingBlocks = getSurroundingBlocks(caster);
        Optional<Block> spawnBlockOptional = surroundingBlocks.stream()
                .filter(block -> block.getType() == Material.AIR)
                .findFirst();

        totem = spawnBlockOptional.orElse(caster.getLocation().getBlock());
        totemBackup = worldService.createBackup(totem);

        totem.setType(Material.REDSTONE_TORCH_ON);

        shootTask = RunnableUtils.runTask(this::shootFireball, 1, 20);
        RunnableUtils.runLater(this::cancelTotem, DURATION);
    }

    private List<Block> getSurroundingBlocks(MonolithEntity caster) {
        Block block = caster.getLocation().getBlock();
        List<Block> blocks = new ArrayList<>();
        blocks.add(block.getRelative(BlockFace.NORTH));
        blocks.add(block.getRelative(BlockFace.EAST));
        blocks.add(block.getRelative(BlockFace.SOUTH));
        blocks.add(block.getRelative(BlockFace.WEST));
        blocks.add(block.getRelative(BlockFace.NORTH_EAST));
        blocks.add(block.getRelative(BlockFace.SOUTH_EAST));
        blocks.add(block.getRelative(BlockFace.SOUTH_WEST));
        blocks.add(block.getRelative(BlockFace.NORTH_WEST));
        return blocks;
    }

    private void shootFireball() {
        List<MonolithEntity> nearbyEntities = entityService.getNearbyMonolithEntities(totem.getLocation(), 30).stream()
                .filter(entity -> entity.getRace() != caster.getRace())
                .collect(Collectors.toList());
        if (nearbyEntities.isEmpty()) {
            return;
        }
        int randomTargetNumber = random.nextInt(nearbyEntities.size());
        MonolithEntity randomTarget = nearbyEntities.get(randomTargetNumber);

        Vector targetLocationVector = randomTarget.getLocation().add(0, 0.5, 0).toVector();
        Vector totemLocationVector = totem.getLocation().add(0.5, 0.5, 0.5).toVector();
        Vector castDirection = targetLocationVector.subtract(totemLocationVector).normalize();
        Vector halfCastDirection = castDirection.clone().multiply(0.5);
        totemFireballFactory.create(caster, totem.getLocation().clone().add(0.5, 0.5, 0.5).add(halfCastDirection), castDirection);
    }

    private void cancelTotem() {
        shootTask.cancel();
        worldService.restoreBackup(totemBackup);
    }
}
