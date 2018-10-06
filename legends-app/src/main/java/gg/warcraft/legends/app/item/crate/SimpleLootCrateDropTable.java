package gg.warcraft.legends.app.item.crate;

import com.google.common.collect.ImmutableSortedMap;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.item.crate.LootCrateDropTable;
import gg.warcraft.legends.api.item.crate.LootCrateDropType;

import java.util.SortedMap;

public class SimpleLootCrateDropTable implements LootCrateDropTable {
    private final LootCrateDropType type;
    private final ImmutableSortedMap<Integer, ItemRarity> distribution;

    public SimpleLootCrateDropTable(LootCrateDropType type, SortedMap<Integer, ItemRarity> distribution) {
        this.type = type;
        this.distribution = ImmutableSortedMap.copyOf(distribution);
    }

    @Override
    public LootCrateDropType getType() {
        return type;
    }

    @Override
    public ImmutableSortedMap<Integer, ItemRarity> viewDistribution() {
        return distribution;
    }
}
