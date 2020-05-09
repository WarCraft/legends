package gg.warcraft.legends.app.shop;

import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.shop.Decoration;
import gg.warcraft.legends.api.shop.ShopItemType;
import gg.warcraft.monolith.api.world.item.ItemType;
import gg.warcraft.monolith.api.world.item.ItemTypeOrVariant;

import java.util.List;

public class SimpleDecoration extends SimpleShopItem implements Decoration {
    private final ItemTypeOrVariant itemType;

    public SimpleDecoration(String id, ShopItemType type, ItemRarity rarity, int price, String name, ItemTypeOrVariant icon,
                            List<String> lore, ItemTypeOrVariant itemType) {
        super(id, type, rarity, price, name, icon, lore);
        this.itemType = itemType;
    }

    @Override
    public ItemTypeOrVariant getItemType() {
        return itemType;
    }
}
