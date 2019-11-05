package gg.warcraft.legends.api.shop;

import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.monolith.api.world.item.ItemType;

import java.util.List;

public interface ShopItem {

    String getId();

    ShopItemType getType();

    ItemRarity getRarity();

    int getPrice();

    String getName();

    ItemType getIcon();

    String getSkull();

    List<String> getLore();
}
