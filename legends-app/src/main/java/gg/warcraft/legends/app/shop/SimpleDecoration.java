package gg.warcraft.legends.app.shop;

import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.shop.Decoration;
import gg.warcraft.legends.api.shop.ShopItemType;
import gg.warcraft.monolith.api.item.ItemType;

import java.util.List;

public class SimpleDecoration extends SimpleShopItem implements Decoration {
    private final ItemType itemType;

    public SimpleDecoration(String id, ShopItemType type, ItemRarity rarity, int price, String name, ItemType icon,
                            List<String> lore, ItemType itemType) {
        super(id, type, rarity, price, name, icon, lore);
        this.itemType = itemType;
    }

    @Override
    public ItemType getItemType() {
        return itemType;
    }
}
