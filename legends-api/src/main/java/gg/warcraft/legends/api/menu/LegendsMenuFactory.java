package gg.warcraft.legends.api.menu;

import gg.warcraft.legends.api.shop.DecorationPackId;
import gg.warcraft.monolith.api.menu.Menu;

import java.util.UUID;

public interface LegendsMenuFactory {

    Menu createMainMenu(UUID playerId);

    Menu createSpecializationMenu(UUID playerId);

    Menu createTeamMenu(UUID playerId);

    Menu createDailyLoginMenu(UUID playerId);

    Menu createDecorationsMenu(UUID playerId);

    Menu createDecorationPackMenu(UUID playerId, DecorationPackId decorationPackId);

    Menu createMiniPetsMenu(UUID playerId);

    Menu createCollectablesMenuFactory(UUID playerId);
}
