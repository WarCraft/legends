package gg.warcraft.legends.menu.shop;

import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.legends.shop.Decoration;
import gg.warcraft.monolith.Staff;
import gg.warcraft.monolith.menu.Button;
import gg.warcraft.monolith.menu.ButtonBuilder;
import gg.warcraft.monolith.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Supplier;

public class DecorationPackMenu extends Menu {

    public DecorationPackMenu(LegendsPlayer player, String title, List<Decoration> decorations, Supplier<Boolean> hasUnlocked) {
        super(player, title, decorations.size());
        for (int i = 0; i < decorations.size(); i += 1) {
            Decoration decoration = decorations.get(i);
            Button button = new ButtonBuilder(decoration.getMaterialData())
                    .setAction(() -> giveStackToPlayer(hasUnlocked.get() || Staff.isMod(player), player, decoration))
                    .addLore(getInteractLore(hasUnlocked.get() || Staff.isMod(player)))
                    .build();
            update(i, button);
        }
    }

    private String getInteractLore(boolean hasUnlocked) {
        if (hasUnlocked) {
            return ChatColor.GREEN + "[CLICK] to add a stack to your inventory";
        } else {
            return ChatColor.GOLD + "You can unlock this decoration pack at shop.warcraft.gg!";
        }
    }

    private void giveStackToPlayer(boolean hasUnlocked, LegendsPlayer player, Decoration decoration) {
        if (!hasUnlocked) {
            player.sendMessage(ChatColor.GOLD + "You can unlock this decoration pack at shop.warcraft.gg!");
            return;
        }
        ItemStack item = decoration.createStack();
        if (!player.giveItem(item, false)) {
            player.sendMessage(ChatColor.YELLOW + "Whoops your inventory is full!");
        }
    }
}
