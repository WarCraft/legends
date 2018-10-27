package gg.warcraft.legends.api.item.crate;

import gg.warcraft.monolith.api.item.ItemReader;

public interface LootCrateReader extends ItemReader {

    LootCrateType getCrateType();
}
