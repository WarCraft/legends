package gg.warcraft.legends.api.shop.service;

import gg.warcraft.legends.api.shop.ShopItem;

public interface ShopRepository {

    ShopItem getItem(String id);

    void save(ShopItem item);
}
