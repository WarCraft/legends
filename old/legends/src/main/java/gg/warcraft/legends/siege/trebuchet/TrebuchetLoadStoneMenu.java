package gg.warcraft.legends.siege.trebuchet;

import gg.warcraft.monolith.menu.Button;
import gg.warcraft.monolith.menu.ButtonSlot;
import gg.warcraft.monolith.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TrebuchetLoadStoneMenu extends Menu {

    public TrebuchetLoadStoneMenu(Player viewer, Trebuchet trebuchet) {
        super(viewer, "Trebuchet");
        // TODO fix event passed as null, maybe update Treb methods to take a PlayerAction interface instead of the event as they just want the player anyways
        update(ButtonSlot.FIRST, new Button(Material.STONE, "Insert stone projectile", () -> trebuchet.loadProjectile(null)));
    }
}
