package gg.warcraft.legends.api.menu;

import gg.warcraft.monolith.api.menu.Menu;

import java.util.UUID;

public interface LeaderboardsMenuFactory {

    Menu createLeaderboardsMenu(UUID playerId);

    Menu createDailyLeaderboardsMenu(UUID playerId);

    Menu createWarLeaderboardsMenu(UUID playerId);

    Menu createAlltimeLeaderboardsMenu(UUID playerId);
}
