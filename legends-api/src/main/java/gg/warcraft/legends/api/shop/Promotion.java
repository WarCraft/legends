package gg.warcraft.legends.api.shop;

import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.monolith.api.entity.EquipmentSlot;
import gg.warcraft.monolith.api.world.item.ItemType;

import java.util.Map;

public interface Promotion extends ShopItem {

    ItemType getItemType();

    EquipmentSlot getEquipmentSlot();

    Map<LegendsAttribute, Integer> getAttributes();
}
