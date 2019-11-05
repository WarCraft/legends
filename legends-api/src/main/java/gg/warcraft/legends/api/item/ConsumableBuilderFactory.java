package gg.warcraft.legends.api.item;

import gg.warcraft.monolith.api.world.item.ItemType;

public interface ConsumableBuilderFactory {

    ConsumableBuilder createConsumableBuilder(ItemType itemType, String name, ItemRarity rarity);
}
