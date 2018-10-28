package gg.warcraft.legends.app.shop.service;

import com.google.inject.Singleton;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.shop.ShopItem;
import gg.warcraft.legends.api.shop.ShopItemType;
import gg.warcraft.legends.api.shop.service.ShopRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class DefaultShopRepository implements ShopRepository {
    private final Map<String, ShopItem> items;
    private final Map<ShopItemType, Map<ItemRarity, List<ShopItem>>> itemsByType;

    public DefaultShopRepository() {
        this.items = new HashMap<>();
        this.itemsByType = new HashMap<>();
    }

    @Override
    public ShopItem getItem(String id) {
        return items.get(id);
    }

    @Override
    public List<ShopItem> getItemsOfType(ShopItemType type, ItemRarity rarity) {
        return itemsByType.getOrDefault(type, Collections.emptyMap())
                .getOrDefault(rarity, Collections.emptyList());
    }

    @Override
    public void save(ShopItem item) {
        items.put(item.getId(), item);
        Map<ItemRarity, List<ShopItem>> itemsOfType =
                itemsByType.computeIfAbsent(item.getType(), key -> new HashMap<>());
        List<ShopItem> itemsOfTypeAndRarity =
                itemsOfType.computeIfAbsent(item.getRarity(), key -> new ArrayList<>());
        itemsOfTypeAndRarity.add(item);
    }
}
