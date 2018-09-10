package gg.warcraft.legends.api.shop.service;

import gg.warcraft.legends.api.shop.Decoration;
import gg.warcraft.legends.api.shop.DecorationPack;
import gg.warcraft.legends.api.shop.MiniPet;
import gg.warcraft.legends.api.shop.ShopItem;

public interface ShopQueryService {

    ShopItem getItem(String id);

    Decoration getDecoration(String id);

    DecorationPack getDecorationPack(String id);

    MiniPet getMiniPet(String id);
}
