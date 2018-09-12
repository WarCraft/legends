package gg.warcraft.legends.spigot.OLD.world.menu;

import gg.warcraft.legends.data.DataService;
import gg.warcraft.monolith.chat.ChatService;
import gg.warcraft.monolith.menu.Button;
import gg.warcraft.monolith.menu.ButtonBuilder;
import gg.warcraft.monolith.menu.ButtonSlot;
import gg.warcraft.monolith.menu.Menu;
import gg.warcraft.monolith.util.RunnableUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class CommandBlockMenu extends Menu {
    private final Player player;
    private final Block block;

    private String currentCommand;

    public CommandBlockMenu(Player player, Block target) {
        super(player, "Command Block");
        this.player = player;
        this.block = target;
        currentCommand = DataService.getInstance().getCommandBlockCommand(block);

        RunnableUtils.runOnNextTick(this::refreshView);
    }

    private void setCommandBlock(String command) {
        currentCommand = command;
        DataService.getInstance().addCommandBlock(block, command);
    }

    private void refreshView() {
        Button newLine = new ButtonBuilder(Material.BOOK_AND_QUILL)
                .setTitle("Set the command to be executed by the player right clicking this block")
                .setAction(() -> {
                    ChatService.getInstance().registerChatListener(player, (player, message) -> {
                        setCommandBlock(message);
                        RunnableUtils.runOnNextTick(this::refreshView);
                    });
                    player.sendMessage(ChatColor.YELLOW + "Type the command:");
                    RunnableUtils.runOnNextTick(player::closeInventory);
                })
                .addLore("")
                .addLore("Current command:")
                .addLore(currentCommand != null ? ChatColor.YELLOW + currentCommand : ChatColor.GRAY + "-")
                .build();
        update(ButtonSlot.FIRST, newLine);

        Button delete = new ButtonBuilder(Material.BARRIER)
                .setTitle("Delete Command")
                .setAction(this::removeCommandBlock)
                .addLore("")
                .addLore("Unbind the command from the block")
                .build();
        update(ButtonSlot.NINTH, delete);

        player.updateInventory();
    }

    private void removeCommandBlock() {
        DataService.getInstance().removeCommandBlock(block);
        currentCommand = null;
        RunnableUtils.runOnNextTick(this::refreshView);
    }
}
