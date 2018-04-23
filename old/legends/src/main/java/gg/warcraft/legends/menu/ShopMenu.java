package gg.warcraft.legends.menu;

import gg.warcraft.legends.menu.shop.DecorationMenu;
import gg.warcraft.legends.menu.shop.MiniPetMenu;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.monolith.menu.BackButton;
import gg.warcraft.monolith.menu.Button;
import gg.warcraft.monolith.menu.ButtonBuilder;
import gg.warcraft.monolith.menu.ButtonSlot;
import gg.warcraft.monolith.menu.Menu;
import gg.warcraft.monolith.util.RunnableUtils;
import org.bukkit.Material;

public class ShopMenu extends Menu {
    private final LegendsPlayer player;

    public ShopMenu(LegendsPlayer viewer) {
        super(viewer, "WarCraft Shop");
        this.player = viewer;

        Button miniPetsButton = new ButtonBuilder("MHF_Pig")
                .setTitle("Mini pets")
                .setAction(() -> RunnableUtils.runOnNextTick(() -> player.openInventory(new MiniPetMenu(player))))
                .addLore("Mini pets are cute little creatures that will follow and cheer you on wherever you go!")
                .addLore("")
                .addLore("[CLICK] to look at your mini pets")
                .build();

        Button decorationButton = new ButtonBuilder(Material.WOOD_DOOR)
                .setTitle("Decoration packs")
                .setAction(() -> RunnableUtils.runOnNextTick(() -> player.openInventory(new DecorationMenu(player))))
                .addLore("Did you know you can rent a house? But did you know you can completely decorate it how you want it!?")
                .addLore("")
                .addLore("[CLICK] to look at your decorations")
                .build();

        update(ButtonSlot.FIRST, miniPetsButton);
        update(ButtonSlot.SECOND, decorationButton);

        update(ButtonSlot.NINTH, new BackButton(viewer, this));
    }
}
