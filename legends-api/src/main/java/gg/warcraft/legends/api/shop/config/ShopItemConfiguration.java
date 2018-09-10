package gg.warcraft.legends.api.shop.config;

import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.shop.ShopItemType;
import gg.warcraft.monolith.api.item.ItemType;

import java.util.List;

public interface ShopItemConfiguration {

    String getId();

    ShopItemType getType();

    ItemRarity getRarity();

    String getName();

    ItemType getIcon();

    String getSkull();

    List<String> getLore();
}
