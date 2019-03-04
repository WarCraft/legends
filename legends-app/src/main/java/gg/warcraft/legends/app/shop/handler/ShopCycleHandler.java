package gg.warcraft.legends.app.shop.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.shop.ShopItem;
import gg.warcraft.legends.api.shop.ShopItemType;
import gg.warcraft.legends.api.shop.service.ShopRepository;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.core.event.DailyTickEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ShopCycleHandler {
    private static final List<ShopItemType> PREMIUM_TYPES;

    static {
        PREMIUM_TYPES = new ArrayList<>();
        PREMIUM_TYPES.add(ShopItemType.DECORATION_PACK);
        PREMIUM_TYPES.add(ShopItemType.MINI_PET);
        PREMIUM_TYPES.add(ShopItemType.SPECIALIZATION);
    }

    private final ShopRepository shopRepository;
    private final Random random;

    @Inject
    public ShopCycleHandler(ShopRepository shopRepository, TaskService taskService, Random random) {
        this.shopRepository = shopRepository;
        this.random = random;

        taskService.runNextTick(() -> {
            if (shopRepository.getAvailableItems().isEmpty()) {
                cycleItems();
            }
        });
    }

    private ShopItemType getRandomPremiumType() {
        if (random.nextFloat() < 0.2f) {
            return ShopItemType.DECORATION;
        } else {
            List<ShopItemType> premiumTypes = new ArrayList<>(PREMIUM_TYPES);
            Collections.shuffle(premiumTypes);
            return premiumTypes.get(0);
        }
    }

    private ItemRarity getRandomPremiumRarity() {
        if (random.nextFloat() < 0.2f) {
            return ItemRarity.LEGENDARY;
        } else {
            return ItemRarity.EPIC;
        }
    }

    private ShopItemType getRandomCommonType() {
        return ShopItemType.DECORATION;
    }

    private ItemRarity getRandomCommonRarity() {
        return ItemRarity.RARE;
    }

    private ShopItem getRandomItem(List<ShopItem> items) {
        if (items.isEmpty()) {
            return null;
        }
        return items.get(random.nextInt(items.size()));
    }

    private void cycleItems() {
        List<ShopItem> newItems = new ArrayList<>();

        for (int i = 0; i < 5; i += 1) {
            List<ShopItem> list = shopRepository.getItemsOfType(getRandomPremiumType(), getRandomPremiumRarity());
            ShopItem item = getRandomItem(list);
            if (item != null) {
                newItems.add(item);
            }
        }

        for (int i = 0; i < 5; i += 1) {
            List<ShopItem> list = shopRepository.getItemsOfType(getRandomCommonType(), getRandomCommonRarity());
            ShopItem item = getRandomItem(list);
            if (item != null) {
                newItems.add(item);
            }
        }

        shopRepository.saveAvailableItems(newItems);
    }

    @Subscribe
    public void onDailyTickEvent(DailyTickEvent event) {
        cycleItems();
    }
}
