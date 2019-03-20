package gg.warcraft.legends.app.shop.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.shop.ShopItem;
import gg.warcraft.legends.api.shop.ShopItemType;
import gg.warcraft.legends.api.shop.service.ShopRepository;
import gg.warcraft.monolith.api.core.PersistenceService;
import gg.warcraft.monolith.api.core.TaskService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Singleton
public class DefaultShopRepository implements ShopRepository {
    private static final String AVAILABLE_ITEMS_KEY = "shop:availableitems";

    private final PersistenceService persistenceService;
    private final Map<String, ShopItem> items;
    private final Map<ShopItemType, Map<ItemRarity, List<ShopItem>>> itemsByType;
    private final List<ShopItem> availableItems;

    @Inject
    public DefaultShopRepository(PersistenceService persistenceService, TaskService taskService) {
        this.persistenceService = persistenceService;
        this.items = new HashMap<>();
        this.itemsByType = new HashMap<>();
        this.availableItems = new ArrayList<>();

        taskService.runNextTick(() ->
                availableItems.addAll(persistenceService.getList(AVAILABLE_ITEMS_KEY).stream()
                        .map(this::getItem)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())));
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
    public List<ShopItem> getAvailableItems() {
        return new ArrayList<>(availableItems);
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

    @Override
    public void saveAvailableItems(List<ShopItem> items) {
        List<String> availableItemIds = items.stream()
                .map(ShopItem::getId)
                .collect(Collectors.toList());
        persistenceService.setList(AVAILABLE_ITEMS_KEY, availableItemIds);

        availableItems.clear();
        availableItems.addAll(items);
    }
}
