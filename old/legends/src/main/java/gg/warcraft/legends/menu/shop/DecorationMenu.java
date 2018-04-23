package gg.warcraft.legends.menu.shop;

import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.monolith.menu.BackButton;
import gg.warcraft.monolith.menu.Button;
import gg.warcraft.monolith.menu.ButtonBuilder;
import gg.warcraft.monolith.menu.ButtonSlot;
import gg.warcraft.monolith.menu.Menu;
import gg.warcraft.monolith.util.RunnableUtils;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public class DecorationMenu extends Menu {

    public DecorationMenu(LegendsPlayer player) {
        super(player, "Decoration Packs");

        Button basicButton = new ButtonBuilder(Material.WOOD)
                .setTitle("Basic")
                .setAction(() -> RunnableUtils.runOnNextTick(() -> player.openInventory(new BasicDecorationPackMenu(player))))
                .addLore("Basic decorations will get you started with making your house your own.")
                .addLore("")
                .addLore("[CLICK] to look at basic decorations")
                .build();

        Button woodButton = new ButtonBuilder(new MaterialData(Material.WOOD, (byte) 1))
                .setTitle("Wood")
                .setAction(() -> RunnableUtils.runOnNextTick(() -> player.openInventory(new WoodDecorationPackMenu(player))))
                .addLore("Wood")
                .addLore("")
                .addLore("[CLICK] to look at wood decorations")
                .build();

        Button stoneButton = new ButtonBuilder(Material.STONE)
                .setTitle("Stone")
                .setAction(() -> RunnableUtils.runOnNextTick(() -> player.openInventory(new StoneDecorationPackMenu(player))))
                .addLore("Stone")
                .addLore("")
                .addLore("[CLICK] to look at stone decorations")
                .build();

        Button fabricsButton = new ButtonBuilder(Material.WOOL)
                .setTitle("Fabrics")
                .setAction(() -> RunnableUtils.runOnNextTick(() -> player.openInventory(new FabricsDecorationPackMenu(player))))
                .addLore("Fabrics")
                .addLore("")
                .addLore("[CLICK] to look at fabrics decorations")
                .build();

        Button flowersButton = new ButtonBuilder(Material.YELLOW_FLOWER)
                .setTitle("Flowers")
                .setAction(() -> RunnableUtils.runOnNextTick(() -> player.openInventory(new FlowersDecorationPackMenu(player))))
                .addLore("Flowers")
                .addLore("")
                .addLore("[CLICK] to look at flowers decorations")
                .build();

        update(ButtonSlot.FIRST, basicButton);
        update(ButtonSlot.SECOND, woodButton);
        update(ButtonSlot.THIRD, stoneButton);
        update(ButtonSlot.FOURTH, fabricsButton);
        update(ButtonSlot.FIFTH, flowersButton);

        update(ButtonSlot.NINTH, new BackButton(player, this));
    }
}
