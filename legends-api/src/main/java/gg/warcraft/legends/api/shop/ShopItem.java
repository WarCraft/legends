package gg.warcraft.legends.api.shop;

import gg.warcraft.monolith.api.item.ItemType;

import java.util.List;

public interface ShopItem {

    int getId();

    ItemType getIcon();

    String getName();

    int getCost();

    List<String> getLore();
}
