package gg.warcraft.legends.api.item;

import gg.warcraft.monolith.api.world.item.ItemType;
import gg.warcraft.monolith.api.world.item.ItemTypeOrVariant;

public interface ConsumableBuilderFactory {

    ConsumableBuilder createConsumableBuilder(ItemTypeOrVariant itemType, String name, ItemRarity rarity);
}
