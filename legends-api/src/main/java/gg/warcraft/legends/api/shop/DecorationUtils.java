package gg.warcraft.legends.api.shop;

import gg.warcraft.monolith.api.world.item.Item;

public interface DecorationUtils {

    Item createItem(DecorationId decorationId);

    Item createStack(DecorationId decorationId);

    Item createMenuItem(DecorationId decorationId, boolean unlocked);
}
