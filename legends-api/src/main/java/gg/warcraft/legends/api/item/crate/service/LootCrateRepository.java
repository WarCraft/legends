package gg.warcraft.legends.api.item.crate.service;

import gg.warcraft.legends.api.item.crate.LootCrate;
import gg.warcraft.legends.api.item.crate.LootCrateType;

public interface LootCrateRepository {

    LootCrate get(LootCrateType type);

    void save(LootCrate crate);
}
