package gg.warcraft.legends.api.shop;

import gg.warcraft.monolith.api.world.item.ItemType;
import gg.warcraft.monolith.api.world.item.ItemTypeOrVariant;

public interface Decoration extends ShopItem {

    ItemTypeOrVariant getItemType();
}
