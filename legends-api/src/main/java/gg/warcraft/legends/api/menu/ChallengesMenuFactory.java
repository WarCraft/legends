package gg.warcraft.legends.api.menu;

import gg.warcraft.monolith.api.menu.Menu;

import java.util.UUID;

public interface ChallengesMenuFactory {

    Menu createChallengesMenu(UUID playerId);

    Menu createDailyChallengesMenu(UUID playerId);

    Menu createWarChallengesMenu(UUID playerId);

    Menu createAlltimeChallengesMenu(UUID playerId);

    Menu createSpecialChallengesMenu(UUID playerId);
}
