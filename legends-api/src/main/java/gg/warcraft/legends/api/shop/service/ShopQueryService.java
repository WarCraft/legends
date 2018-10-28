package gg.warcraft.legends.api.shop.service;

import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.shop.Decoration;
import gg.warcraft.legends.api.shop.DecorationPack;
import gg.warcraft.legends.api.shop.MiniPet;
import gg.warcraft.legends.api.shop.Promotion;
import gg.warcraft.legends.api.shop.ShopItem;
import gg.warcraft.legends.api.shop.ShopItemType;

public interface ShopQueryService {

    ShopItem getItem(String id);

    ShopItem getRandomItem(ShopItemType type, ItemRarity rarity);

    Decoration getDecoration(String id);

    DecorationPack getDecorationPack(String id);

    MiniPet getMiniPet(String id);

    Promotion getPromotion(String id);
}
