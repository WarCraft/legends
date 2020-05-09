package gg.warcraft.legends.app.shop;

import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.shop.Promotion;
import gg.warcraft.legends.api.shop.ShopItemType;
import gg.warcraft.monolith.api.entity.EquipmentSlot;
import gg.warcraft.monolith.api.world.item.ItemType;
import gg.warcraft.monolith.api.world.item.ItemTypeOrVariant;

import java.util.List;
import java.util.Map;

public class SimplePromotion extends SimpleShopItem implements Promotion {
    private final ItemTypeOrVariant itemType;
    private final EquipmentSlot equipmentSlot;
    private final Map<LegendsAttribute, Integer> attributes;

    public SimplePromotion(String id, ShopItemType type, ItemRarity rarity, int price, String name, ItemTypeOrVariant icon,
                           List<String> lore, ItemTypeOrVariant itemType, EquipmentSlot equipmentSlot,
                           Map<LegendsAttribute, Integer> attributes) {
        super(id, type, rarity, price, name, icon, lore);
        this.itemType = itemType;
        this.equipmentSlot = equipmentSlot;
        this.attributes = attributes;
    }

    @Override
    public ItemTypeOrVariant getItemType() {
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
