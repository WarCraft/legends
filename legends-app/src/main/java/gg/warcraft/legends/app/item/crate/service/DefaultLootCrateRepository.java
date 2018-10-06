package gg.warcraft.legends.app.item.crate.service;

import com.google.inject.Singleton;
import gg.warcraft.legends.api.item.crate.LootCrate;
import gg.warcraft.legends.api.item.crate.LootCrateType;
import gg.warcraft.legends.api.item.crate.service.LootCrateRepository;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class DefaultLootCrateRepository implements LootCrateRepository {
    private final Map<LootCrateType, LootCrate> crates;

    public DefaultLootCrateRepository() {
        this.crates = new HashMap<>();
    }

    @Override
    public LootCrate get(LootCrateType type) {
        return crates.get(type);
    }

    @Override
    public void save(LootCrate crate) {
        crates.put(crate.getType(), crate);
    }
}
