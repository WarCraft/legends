package gg.warcraft.legends.menu;

import gg.warcraft.legends.entity.LegendsRace;
import gg.warcraft.legends.menu.objective.HumanObjectiveMenu;
import gg.warcraft.legends.menu.objective.OrcObjectiveMenu;
import gg.warcraft.legends.menu.shop.DecorationMenu;
import gg.warcraft.legends.menu.shop.MiniPetMenu;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.monolith.menu.Button;
import gg.warcraft.monolith.menu.ButtonBuilder;
import gg.warcraft.monolith.menu.ButtonSlot;
import gg.warcraft.monolith.menu.Menu;
import gg.warcraft.monolith.util.RunnableUtils;
import org.bukkit.Material;

public class MainMenu extends Menu {
    private final LegendsPlayer player;

    public MainMenu(LegendsPlayer viewer) {
        super(viewer, "Main Menu");
        this.player = viewer;
        update(ButtonSlot.FIRST, new PlayerButton(viewer));
        update(ButtonSlot.SECOND, new RaceButton(viewer));
        update(ButtonSlot.THIRD, new SpecializationButton(viewer));

        Button miniPetsButton = new ButtonBuilder("MHF_Pig")
                .setTitle("Mini pets")
                .setAction(() -> RunnableUtils.runOnNextTick(() -> player.openInventory(new MiniPetMenu(player))))
                .addLore("Mini pets are cute little creatures that will follow and cheer you on wherever you go!")
                .addLore("")
                .addLore("[CLICK] to look at your mini pets")
                .build();

        Button decorationButton = new ButtonBuilder(Material.WOOD)
                .setTitle("Decoration packs")
                .setAction(() -> RunnableUtils.runOnNextTick(() -> player.openInventory(new DecorationMenu(player))))
                .addLore("Did you know you can rent a house? Did you also know you can completely decorate it how you want it!?")
                .addLore("")
                .addLore("[CLICK] to look at your decorations")
                .build();

        update(ButtonSlot.FOURTH, miniPetsButton);
        update(ButtonSlot.FIFTH, decorationButton);

        // NOTE disabled for now: update(ButtonSlot.NINTH, new ReturnButton(viewer));
        if (player.getRace() == LegendsRace.HUMAN) {
            update(ButtonSlot.NINTH, new Button(Material.NETHER_STAR, "Objectives",
                    () -> RunnableUtils.runOnNextTick(() -> player.openInventory(new HumanObjectiveMenu(viewer)))));
        } else if (player.getRace() == LegendsRace.ORC) {
            update(ButtonSlot.NINTH, new Button(Material.NETHER_STAR, "Objectives",
                    () -> RunnableUtils.runOnNextTick(() -> player.openInventory(new OrcObjectiveMenu(viewer)))));
        }
    }
}
