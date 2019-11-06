package gg.warcraft.legends.api.shop.config;

import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.monolith.api.entity.EquipmentSlot;

import java.util.Map;

public interface PromotionConfiguration extends ShopItemConfiguration {

    String getItemType();

    EquipmentSlot getEquipmentSlot();

    Map<LegendsAttribute, Integer> getAttributes();
}
