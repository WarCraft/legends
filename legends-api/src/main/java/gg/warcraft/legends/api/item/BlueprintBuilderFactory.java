package gg.warcraft.legends.api.item;

import gg.warcraft.monolith.api.item.ItemType;

public interface BlueprintBuilderFactory {

    BlueprintBuilder createBlueprintBuilder(ItemType itemType, String name, ItemRarity rarity);
}
