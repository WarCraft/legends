package gg.warcraft.legends.api.menu;

import gg.warcraft.legends.api.shop.DecorationPackId;
import gg.warcraft.monolith.api.menu.Menu;

import java.util.UUID;

public interface DecorationPackMenuFactory {

    Menu createDecorationItemsMenu(UUID playerId, DecorationPackId decorationPackId);
}
