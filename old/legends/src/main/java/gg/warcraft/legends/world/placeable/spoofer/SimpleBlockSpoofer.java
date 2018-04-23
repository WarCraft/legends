package gg.warcraft.legends.world.placeable.spoofer;

import gg.warcraft.legends.world.BlockData;
import gg.warcraft.monolith.player.MonolithPlayer;
import org.bukkit.block.Block;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimpleBlockSpoofer implements BlockSpoofer {

    private final List<MonolithPlayer> players;

    private Set<Block> currentlySpoofed;

    public SimpleBlockSpoofer(MonolithPlayer player) {
        this(Collections.singletonList(player));
    }

    public SimpleBlockSpoofer(List<MonolithPlayer> players) {
        this.players = players;
        this.currentlySpoofed = new HashSet<>();
    }

    @Override
    public void spoof(Map<Block, BlockData> blocks) {
        blocks.forEach((block, blockData) -> {
            players.forEach(player -> {
                player.sendBlockChange(block.getLocation(), blockData.getMaterial(), blockData.getData());
            });
        });
        currentlySpoofed.removeAll(blocks.keySet());
        undo();
        currentlySpoofed = blocks.keySet();
    }

    @Override
    public void undo() {
        players.forEach(player -> {
            currentlySpoofed.forEach(block -> {
                player.sendBlockChange(block.getLocation(), block.getType(), block.getData());
            });
        });
        currentlySpoofed.clear();
    }
}
