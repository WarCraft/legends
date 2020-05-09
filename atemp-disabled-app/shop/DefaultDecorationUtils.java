package gg.warcraft.legends.app.shop;

import com.google.inject.Inject;
import gg.warcraft.legends.api.shop.DecorationId;
import gg.warcraft.legends.api.shop.DecorationUtils;
import gg.warcraft.monolith.api.world.item.Item;
import gg.warcraft.monolith.api.world.item.ItemService;

public class DefaultDecorationUtils implements DecorationUtils {
    private static final int STACK_SIZE = 64;

    private final ItemService itemService;

    @Inject
    public DefaultDecorationUtils(ItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    public Item createItem(DecorationId decorationId) {
//        ItemType type = decorationId.getIcon();
//        String name = decorationId.getName();
//        ItemBuilder builder = itemService.createItemBuilder(type, name);
//        builder.withLore(decorationId.getLore());
//        return builder.build();
        return null;
    }

    @Override
    public Item createStack(DecorationId decorationId) {
//        ItemType type = decorationId.getIcon();
//        String name = decorationId.getName();
//        ItemBuilder builder = itemService.createItemBuilder(type, name);
//        builder.withLore(decorationId.getLore());
//        builder.withStackSize(STACK_SIZE);
//        return builder.build();
        return null;
    }

    @Override
    public Item createMenuItem(DecorationId decorationId, boolean unlocked) {
//        ItemType type = decorationId.getIcon();
//        String name = decorationId.getName();
//        ItemBuilder builder = itemService.createItemBuilder(type, name);
//        List<String> lore = decorationId.getMenuLore(unlocked);
//        builder.withLore(lore);
//        return builder.build();
        return null;
    }
}
