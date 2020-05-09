package gg.warcraft.legends.app.shop;

import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.shop.ShopItem;
import gg.warcraft.legends.api.shop.ShopItemType;
import gg.warcraft.monolith.api.world.item.ItemType;
import gg.warcraft.monolith.api.world.item.ItemTypeOrVariant;

import java.util.List;

public class SimpleShopItem implements ShopItem {
    private final String id;
    private final ShopItemType type;
    private final ItemRarity rarity;
    private final int price;
    private final String name;
    private final ItemTypeOrVariant icon;
    private final String skull;
    private final List<String> lore;

    public SimpleShopItem(String id, ShopItemType type, ItemRarity rarity, int price, String name, ItemTypeOrVariant icon,
                          List<String> lore) {
        this.id = id;
        this.type = type;
        this.rarity = rarity;
        this.price = price;
        this.name = name;
        this.icon = icon;
        this.skull = null;
        this.lore = lore;
    }

    public SimpleShopItem(String id, ShopItemType type, ItemRarity rarity, int price, String name, String skull,
                          List<String> lore) {
        this.id = id;
        this.type = type;
        this.rarity = rarity;
        this.price = price;
        this.name = name;
        this.icon = null;
        this.skull = skull;
        this.lore = lore;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public ShopItemType getType() {
        return type;
    }

    @Override
    public ItemRarity getRarity() {
        return rarity;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ItemTypeOrVariant getIcon() {
        return icon;
    }

    @Override
    public String getSkull() {
        return skull;
    }

    @Override
    public List<String> getLore() {
        return lore;
    }
}
