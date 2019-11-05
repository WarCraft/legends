package gg.warcraft.legends.app.shop;

import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.shop.ShopItemType;
import gg.warcraft.legends.api.shop.Specialization;
import gg.warcraft.monolith.api.world.item.ItemType;

import java.util.List;

public class SimpleSpecialization extends SimpleShopItem implements Specialization {

    public SimpleSpecialization(String id, ShopItemType type, ItemRarity rarity,
                                int price, String name, ItemType icon, List<String> lore) {
        super(id, type, rarity, price, name, icon, lore);
    }
}
