package gg.warcraft.legends.app.shop;

import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.shop.MiniPet;
import gg.warcraft.legends.api.shop.ShopItemType;
import gg.warcraft.monolith.api.entity.EntityType;

import java.util.List;

public class SimpleMiniPet extends SimpleShopItem implements MiniPet {
    private final EntityType entityType;
    private final boolean rainbow;

    public SimpleMiniPet(String id, ShopItemType type, ItemRarity rarity, int price, String name, String skull,
                         List<String> lore, EntityType entityType, boolean rainbow) {
        super(id, type, rarity, price, name, skull, lore);
        this.entityType = entityType;
        this.rainbow = rainbow;
    }

    @Override
    public EntityType getEntityType() {
        return entityType;
    }

    @Override
    public boolean isRainbow() {
        return rainbow;
    }
}
