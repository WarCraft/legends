package gg.warcraft.legends.menu;

import gg.warcraft.legends.entity.LegendsAttribute;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.monolith.chat.ChatService;
import gg.warcraft.monolith.event.EventService;
import gg.warcraft.monolith.menu.Button;
import gg.warcraft.monolith.player.PlayerDisplayNameChangedEvent;
import gg.warcraft.monolith.player.PlayerService;
import gg.warcraft.monolith.util.RunnableUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerButton extends Button {
    private static final ChatService chatService = ChatService.getInstance();
    private static final PlayerService playerService = PlayerService.getInstance();
    private final LegendsPlayer player;

    public PlayerButton(LegendsPlayer player) {
        super(player.getName(), player.getName());
        this.player = player;

        List<String> lore = new ArrayList<>();
        lore.add("This is your character.");
        lore.add("Your current name is " + ChatColor.YELLOW + player.getDisplayName());
        lore.add("You are described as");
        int maxLineLength = 50;
        String remainingDescription = player.getDescription();
        while (remainingDescription.length() > 0) {
            int lineLength = Math.min(remainingDescription.length(), maxLineLength);
            String currentDescription = remainingDescription.substring(0, lineLength);
            lore.add(ChatColor.YELLOW + currentDescription);
            remainingDescription = remainingDescription.substring(lineLength);
        }
        lore.add("");
        lore.add("Current attributes:");
        int strength = (int) player.getAttributeValue(LegendsAttribute.STRENGTH);
        if (strength != 0) {
            lore.add("Strength +" + strength);
        }
        int dexterity = (int) player.getAttributeValue(LegendsAttribute.DEXTERITY);
        if (dexterity != 0) {
            lore.add("Dexterity +" + dexterity);
        }
        int intelligence = (int) player.getAttributeValue(LegendsAttribute.INTELLIGENCE);
        if (intelligence != 0) {
            lore.add("Intelligence +" + intelligence);
        }
        int crit = (int) player.getAttributeValue(LegendsAttribute.CRIT);
        if (crit != 0) {
            lore.add("Crit +" + crit);
        }
        int vitality = (int) player.getAttributeValue(LegendsAttribute.VITALITY);
        if (vitality != 0) {
            lore.add("Vitality +" + vitality);
        }
        lore.add("");
        lore.add("You have " + ChatColor.YELLOW + player.getInGameCurrency() + ChatColor.GRAY + " gold.");
        lore.add("");
        lore.add("[SHIFT LEFT CLICK] to change your name");
        lore.add("[SHIFT RIGHT CLICK] to change your description");
        setLoreStrict(lore);
    }

    private void updatePlayerName(Player player, String message) {
        if (player == this.player.getPlayer()) {
            if (playerService.isNameAvailable(message)) {
                if (message.length() > 16) {
                    player.sendMessage(ChatColor.YELLOW + "Sorry that name is too long. Your name can be a maximum of 16 characters.");
                } else if (message.length() < 2) {
                    player.sendMessage(ChatColor.YELLOW + "Sorry that name is too short. Your name must have a minimum of 2 characters.");
                } else {
                    if (message.matches("[a-zA-Z]*")) {
                        this.player.setDisplayName(message);
                        player.sendMessage(ChatColor.YELLOW + "Your name is now: " + message);
                        EventService.getInstance().publishEvent(new PlayerDisplayNameChangedEvent(player));
                    } else {
                        player.sendMessage(ChatColor.YELLOW + "Sorry that is not a valid name. Your name can only consist of letters.");
                    }
                }
            } else {
                player.sendMessage(ChatColor.YELLOW + "Sorry that name is already used by another player.");
            }
        }
    }

    private void updatePlayerDescription(Player player, String message) {
        if (player == this.player.getPlayer()) {
            this.player.setDescription(message);
            player.sendMessage(ChatColor.YELLOW + "Your description is now: " + message);
        }
    }

    @Override
    public void click(InventoryClickEvent event) {
        if (event.isShiftClick()) {
            if (event.isLeftClick()) {
                chatService.registerChatListener(player.getPlayer(), this::updatePlayerName);
                player.sendMessage(ChatColor.YELLOW + "Please type your new name:");
            } else {
                chatService.registerChatListener(player.getPlayer(), this::updatePlayerDescription);
                player.sendMessage(ChatColor.YELLOW + "Please type your new description:");
            }
            RunnableUtils.runOnNextTick(player::closeInventory);
        }
    }
}
