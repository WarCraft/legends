package gg.warcraft.legends.app.shop;

import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.shop.Promotion;
import gg.warcraft.legends.api.shop.ShopItemType;
import gg.warcraft.monolith.api.item.ItemType;

import java.util.List;

public class SimplePromotion extends SimpleShopItem implements Promotion {

    public SimplePromotion(String id, ShopItemType type, ItemRarity rarity, String name, ItemType icon,
                           List<String> lore) {
        super(id, type, rarity, name, icon, lore);
    }
}
