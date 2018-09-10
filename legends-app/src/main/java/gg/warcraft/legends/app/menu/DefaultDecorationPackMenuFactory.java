package gg.warcraft.legends.app.menu;

import gg.warcraft.legends.api.menu.DecorationPackMenuFactory;
import gg.warcraft.monolith.api.menu.Menu;

import java.util.UUID;

public class DefaultDecorationPackMenuFactory implements DecorationPackMenuFactory {

    @Override
    public Menu createDecorationItemsMenu(UUID playerId) {
        return null;
    }
}
