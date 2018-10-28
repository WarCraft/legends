package gg.warcraft.legends.app.shop;

import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.shop.Decoration;
import gg.warcraft.legends.api.shop.DecorationPack;
import gg.warcraft.legends.api.shop.ShopItemType;
import gg.warcraft.monolith.api.item.ItemType;

import java.util.ArrayList;
import java.util.List;

public class SimpleDecorationPack extends SimpleShopItem implements DecorationPack {
    private final List<Decoration> decorations;

    public SimpleDecorationPack(String id, ShopItemType type, ItemRarity rarity, int price, String name, ItemType icon,
                                List<String> lore, List<Decoration> decorations) {
        super(id, type, rarity, price, name, icon, lore);
        this.decorations = decorations;
    }

    @Override
    public List<Decoration> getDecorations() {
        return new ArrayList<>(decorations);
    }
}
