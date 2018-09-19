package gg.warcraft.legends.app.shop;

import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.shop.Promotion;
import gg.warcraft.legends.api.shop.ShopItemType;
import gg.warcraft.monolith.api.entity.EquipmentSlot;
import gg.warcraft.monolith.api.item.ItemType;

import java.util.List;
import java.util.Map;

public class SimplePromotion extends SimpleShopItem implements Promotion {
    private final ItemType itemType;
    private final EquipmentSlot equipmentSlot;
    private final Map<LegendsAttribute, Integer> attributes;

    public SimplePromotion(String id, ShopItemType type, ItemRarity rarity, String name, ItemType icon,
                           List<String> lore, ItemType itemType, EquipmentSlot equipmentSlot,
                           Map<LegendsAttribute, Integer> attributes) {
        super(id, type, rarity, name, icon, lore);
        this.itemType = itemType;
        this.equipmentSlot = equipmentSlot;
        this.attributes = attributes;
    }

    @Override
    public ItemType getItemType() {
        return itemType;
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return equipmentSlot;
    }

    @Override
    public Map<LegendsAttribute, Integer> getAttributes() {
        return attributes;
    }
}
