package gg.warcraft.legends.app.menu;

import gg.warcraft.legends.api.menu.DailyLoginMenuFactory;
import gg.warcraft.monolith.api.menu.Menu;

import java.util.UUID;

public class DefaultDailyLoginMenuFactory implements DailyLoginMenuFactory {

    @Override
    public Menu createDailyLoginMenu(UUID playerId) {
        return null;
    }
}
