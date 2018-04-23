package gg.warcraft.legends.siege.trebuchet;

import org.bukkit.event.player.PlayerInteractEvent;

public interface Trebuchet {

    void loadProjectile(PlayerInteractEvent event);

    void turnLeft(PlayerInteractEvent event);

    void turnRight(PlayerInteractEvent event);

    void charge(PlayerInteractEvent event);

    void fire(PlayerInteractEvent event);
}
