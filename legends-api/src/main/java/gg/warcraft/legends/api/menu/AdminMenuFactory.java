package gg.warcraft.legends.api.menu;

import gg.warcraft.monolith.api.menu.Menu;

import java.util.UUID;

public interface AdminMenuFactory {

    Menu createFishMenu(UUID playerId);
}
