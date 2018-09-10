package gg.warcraft.legends.app.shop.service;

import com.google.inject.Singleton;
import gg.warcraft.legends.api.shop.ShopItem;
import gg.warcraft.legends.api.shop.service.ShopRepository;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class DefaultShopRepository implements ShopRepository {
    private final Map<String, ShopItem> items;

    public DefaultShopRepository() {
        this.items = new HashMap<>();
    }

    @Override
    public ShopItem getItem(String id) {
        return items.get(id);
    }

    @Override
    public void save(ShopItem item) {
        items.put(item.getId(), item);
    }
}
