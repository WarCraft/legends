package gg.warcraft.legends.api.item.crate;

import gg.warcraft.monolith.api.world.item.Item;

public interface LootCrateReaderFactory {

    LootCrateReader createLootCrateReader(Item item);
}
