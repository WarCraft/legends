package gg.warcraft.legends.api.shop.config;

import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.shop.ShopItemType;

import java.util.List;
import java.util.Map;

public interface ShopConfiguration {

    Map<ShopItemType, Map<ItemRarity, Integer>> getPrices();

    List<DecorationConfiguration> getDecorations();

    List<DecorationPackConfiguration> getDecorationPacks();

    List<MiniPetConfiguration> getMiniPets();

    List<PromotionConfiguration> getPromotions();
}
