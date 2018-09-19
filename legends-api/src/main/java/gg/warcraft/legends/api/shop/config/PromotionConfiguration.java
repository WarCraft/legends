package gg.warcraft.legends.api.shop.config;

import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.monolith.api.entity.EquipmentSlot;
import gg.warcraft.monolith.api.item.ItemType;

import java.util.Map;

public interface PromotionConfiguration extends ShopItemConfiguration {

    ItemType getItemType();

    EquipmentSlot getEquipmentSlot();

    Map<LegendsAttribute, Integer> getAttributes();
}
