package gg.warcraft.legends.api.shop.service;

import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.shop.ShopItem;
import gg.warcraft.legends.api.shop.ShopItemType;

import java.util.List;

public interface ShopRepository {

    ShopItem getItem(String id);

    List<ShopItem> getItemsOfType(ShopItemType type, ItemRarity rarity);

    void save(ShopItem item);
}
