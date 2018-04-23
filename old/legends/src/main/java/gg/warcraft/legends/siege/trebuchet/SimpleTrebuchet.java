package gg.warcraft.legends.siege.trebuchet;

import de.slikey.effectlib.util.VectorUtils;
import gg.warcraft.monolith.Monolith;
import gg.warcraft.monolith.event.EventService;
import gg.warcraft.monolith.util.BlockCopySession;
import gg.warcraft.monolith.util.BlockOffsets;
import gg.warcraft.monolith.util.BoundingBlockBox;
import gg.warcraft.monolith.util.DirectionUtils;
import gg.warcraft.monolith.util.MonolithTask;
import gg.warcraft.monolith.util.RunnableUtils;
import gg.warcraft.monolith.world.BlockBackup;
import gg.warcraft.monolith.world.WorldService;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class SimpleTrebuchet implements Trebuchet, BlockCopySession.Observer {
    private static final String METADATA_KEY = SimpleTrebuchet.class.getCanonicalName();
    private static final long DESPAWN_COOLDOWN_TICKS = 5 * 60 * 20;
    private static final int MAX_TURN = 6;
    private static final int MAX_CHARGE = 12;
    private static final double SEVEN_POINT_FIVE_DEGREE_IN_RADIANS = 0.1309;
    private static final BoundingBlockBox source;

    static {
        source = new BoundingBlockBox(
                new BlockOffsets(7, 7, 2, 2, 12, 0),
                new Location(Monolith.getInstance().getWorld(), -44, 4, -6019),
                BlockFace.NORTH);
    }

    private final WorldService worldService;
    private final BoundingBlockBox area;
    private final List<BlockState> buttons;
    private final BlockBackup backup;

    private Location safeSpot;
    private boolean loaded;
    private int turn;
    private int charge;
    private MonolithTask restoreTask;

    public SimpleTrebuchet(BoundingBlockBox area) {
        this(WorldService.getInstance(), area);
    }

    public SimpleTrebuchet(WorldService worldService, BoundingBlockBox area) {
        this.worldService = worldService;
        this.area = area;
        this.buttons = new ArrayList<>();
        BlockCopySession copySession = new BlockCopySession(source, area);
        copySession.addObserver(this);
        this.backup = worldService.createBackup(copySession);
        this.loaded = false;
        this.turn = 0;
        this.charge = 1;

        // TODO refactor out of here and TrebPlot classes
        if (safeSpot != null) {
            double height = source.getUpperBoundary() - source.getLowerBoundary();
            BlockOffsets safeOffsets = new BlockOffsets(
                    area.getOffsets().getFrontOffset(),
                    area.getOffsets().getBackOffset(),
                    area.getOffsets().getRightOffset(),
                    area.getOffsets().getLeftOffset(),
                    height,
                    0
            );
            BoundingBlockBox safeArea = new BoundingBlockBox(safeOffsets, area.getLocation(), area.getFacing());
            safeArea.getNearbyLivingEntities().forEach(entity -> {
                Vector direction = entity.getLocation().getDirection();
                safeSpot.setDirection(direction);
                entity.teleport(safeSpot);
            });
        } else {
            System.out.println("Failed to TP entities to safe spot for SimpleTrebuchet, safe spot was null!");
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

    private void playSound(Sound sound) {
        World world = area.getLocation().getWorld();
        world.playSound(area.getLocation(), sound, SoundCategory.AMBIENT, 1, 1);
    }

    @Override
    public void loadProjectile(PlayerInteractEvent event) {
        if (!loaded) {
            Player player = event.getPlayer();
            if (player.getInventory().containsAtLeast(TrebuchetPlotBuildMaterialMenu.resourceStone, 1)) {
                player.getInventory().removeItem(TrebuchetPlotBuildMaterialMenu.createStoneItemStack(1));
                loaded = true;
                playSound(Sound.BLOCK_STONE_HIT);
                event.getPlayer().sendMessage(ChatColor.YELLOW + "The trebuchet has been loaded with a stone projectile.");
            } else {
                player.sendMessage(ChatColor.YELLOW + "Whoops you don't have a stone to load into the trebuchet!");
                return;
            }
        } else {
            event.getPlayer().sendMessage(ChatColor.YELLOW + "The trebuchet has already been loaded with a stone projectile.");
        }

        resetRestoreTask();
    }

    private String getTurnMessage() {
        StringBuilder message = new StringBuilder(ChatColor.YELLOW + "[");

        if (turn == 0) {
            message.append("------|------] The trebuchet has been centered.");
        } else if (turn == -MAX_TURN) {
            message.append("|------------] The trebuchet can't turn any further.");
        } else if (turn == MAX_TURN) {
            message.append("------------|] The trebuchet can't turn any further.");
        } else if (turn < 0) {
            int tempTurn = -turn;
            for (int i = 0; i < MAX_CHARGE - tempTurn; i += 1) {
                message.append("-");
            }
            message.append("|");
            for (int i = 0; i < tempTurn - 1; i += 1) {
                message.append("-");
            }
            message.append("-------");
            message.append("] The trebuchet has turned slightly.");
        } else {
            message.append("-------");
            for (int i = 0; i < turn - 1; i += 1) {
                message.append("-");
            }
            message.append("|");
            for (int i = 0; i < MAX_TURN - turn; i += 1) {
                message.append("-");
            }
            message.append("] The trebuchet has turned slightly.");
        }
        return message.toString();
    }

    @Override
    public void turnLeft(PlayerInteractEvent event) {
        turn = Math.max(turn - 1, -MAX_TURN);
        playSound(Sound.BLOCK_LADDER_STEP);
        event.getPlayer().sendMessage(getTurnMessage());

        resetRestoreTask();
    }

    @Override
    public void turnRight(PlayerInteractEvent event) {
        turn = Math.min(turn + 1, MAX_TURN);
        playSound(Sound.BLOCK_LADDER_STEP);
        event.getPlayer().sendMessage(getTurnMessage());

        resetRestoreTask();
    }

    @Override
    public void charge(PlayerInteractEvent event) {
        charge = Math.min(charge + 1, MAX_CHARGE);

        if (charge == MAX_CHARGE) {
            event.getPlayer().sendMessage(ChatColor.YELLOW + "[-----------|] The trebuchet is at max charge.");
        } else {
            StringBuilder message = new StringBuilder(ChatColor.YELLOW + "[");
            for (int i = 0; i < charge - 1; i += 1) {
                message.append("-");
            }
            message.append("|");
            for (int i = 0; i < MAX_CHARGE - charge; i += 1) {
                message.append("-");
            }
            message.append("] The trebuchet has been charged.");
            event.getPlayer().sendMessage(message.toString());
        }

        resetRestoreTask();
    }

    public static double velocityModifier = 0.15;
    public static double maxVariance = 0.0174533; // 1 degree in radians
    public static Random random = new Random(System.currentTimeMillis());

    public double getVariance() {
        double variance = maxVariance * random.nextDouble();
        if (random.nextBoolean()) {
            return variance;
        } else {
            return -variance;
        }
    }

    @Override
    public void fire(PlayerInteractEvent event) {
        if (!loaded) {
            event.getPlayer().sendMessage(ChatColor.YELLOW + "The trebuchet hasn't been loaded with a projectile yet!");
            return;
        }

        Location spawn = area.getLocation().clone();
        spawn.add(0, 13, 0);
        spawn.add(DirectionUtils.toVector(area.getFacing()).multiply(6));

        Vector velocity = DirectionUtils.toVector(area.getFacing());
        velocity.multiply(velocityModifier * charge);
        double yOffset = 1 * ((double) charge / (double) MAX_CHARGE); // offsets Y from 0 to 1 depending on the charge level
        velocity.add(new Vector(0, yOffset, 0));
        VectorUtils.rotateAroundAxisY(velocity, (turn * -SEVEN_POINT_FIVE_DEGREE_IN_RADIANS) + getVariance());

        new TrebuchetProjectile(spawn, velocity, 50, 3);
        playSound(Sound.BLOCK_PISTON_EXTEND);
        loaded = false;
        charge = 1;

        resetRestoreTask();
    }

    private void initializeButton(BlockState button) {
        buttons.add(button);
        if (buttons.size() != 3) {
            return;
        }

        // TODO FIXME cleanup
        PlayerInteractEventListener listener;
        MetadataValue metadata;
        switch (area.getFacing()) {
            case NORTH:
                buttons.sort(Comparator.comparingInt(BlockState::getX));
                listener = this::turnLeft;
                metadata = new FixedMetadataValue(Monolith.getInstance(), listener);
                buttons.get(0).setMetadata(METADATA_KEY, metadata);
                listener = this::charge;
                metadata = new FixedMetadataValue(Monolith.getInstance(), listener);
                buttons.get(1).setMetadata(METADATA_KEY, metadata);
                listener = this::turnRight;
                metadata = new FixedMetadataValue(Monolith.getInstance(), listener);
                buttons.get(2).setMetadata(METADATA_KEY, metadata);
                break;
            case EAST:
                buttons.sort(Comparator.comparingInt(BlockState::getZ));
                listener = this::turnLeft;
                metadata = new FixedMetadataValue(Monolith.getInstance(), listener);
                buttons.get(0).setMetadata(METADATA_KEY, metadata);
                listener = this::charge;
                metadata = new FixedMetadataValue(Monolith.getInstance(), listener);
                buttons.get(1).setMetadata(METADATA_KEY, metadata);
                listener = this::turnRight;
                metadata = new FixedMetadataValue(Monolith.getInstance(), listener);
                buttons.get(2).setMetadata(METADATA_KEY, metadata);
                break;
            case SOUTH:
                buttons.sort(Comparator.comparingInt(BlockState::getX));
                listener = this::turnLeft;
                metadata = new FixedMetadataValue(Monolith.getInstance(), listener);
                buttons.get(2).setMetadata(METADATA_KEY, metadata);
                listener = this::charge;
                metadata = new FixedMetadataValue(Monolith.getInstance(), listener);
                buttons.get(1).setMetadata(METADATA_KEY, metadata);
                listener = this::turnRight;
                metadata = new FixedMetadataValue(Monolith.getInstance(), listener);
                buttons.get(0).setMetadata(METADATA_KEY, metadata);
                break;
            case WEST:
                buttons.sort(Comparator.comparingInt(BlockState::getZ));
                listener = this::turnLeft;
                metadata = new FixedMetadataValue(Monolith.getInstance(), listener);
                buttons.get(2).setMetadata(METADATA_KEY, metadata);
                listener = this::charge;
                metadata = new FixedMetadataValue(Monolith.getInstance(), listener);
                buttons.get(1).setMetadata(METADATA_KEY, metadata);
                listener = this::turnRight;
                metadata = new FixedMetadataValue(Monolith.getInstance(), listener);
                buttons.get(0).setMetadata(METADATA_KEY, metadata);
                break;
        }
    }

    @Override
    public void onBlockChange(BlockState previous, BlockState current) {
        PlayerInteractEventListener listener;
        MetadataValue metadata;
        switch (current.getType()) {
            case CAULDRON:
                listener = this::loadProjectile;
                metadata = new FixedMetadataValue(Monolith.getInstance(), listener);
                current.getBlock().setMetadata(METADATA_KEY, metadata);
                break;
            case STONE_BUTTON:
                initializeButton(current);
                break;
            case LEVER:
                listener = this::fire;
                metadata = new FixedMetadataValue(Monolith.getInstance(), listener);
                current.getBlock().setMetadata(METADATA_KEY, metadata);
                break;
            case BEDROCK:
                if (previous.getType() != Material.BEDROCK) {
                    safeSpot = current.getLocation().add(0.5, 0, 0.5);
                    previous.update(true, false);
                }
                break;
        }
    }

    private interface PlayerInteractEventListener {
        void onPlayerInteractEvent(PlayerInteractEvent event);
    }

    static {
        EventService.getInstance().registerForEvents(new Listener() {
            @EventHandler(ignoreCancelled = true)
            public void onPlayerInteractEvent(PlayerInteractEvent event) {
                List<MetadataValue> metadata = event.getClickedBlock().getMetadata(METADATA_KEY);
                if (!metadata.isEmpty()) {
                    event.setCancelled(true);
                    PlayerInteractEventListener listener = (PlayerInteractEventListener) metadata.get(0).value();
                    listener.onPlayerInteractEvent(event);
                }
            }
        });
    }
}
