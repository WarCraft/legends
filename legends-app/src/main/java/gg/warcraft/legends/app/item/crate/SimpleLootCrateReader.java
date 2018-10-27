package gg.warcraft.legends.app.item.crate;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.legends.api.item.crate.LootCrate;
import gg.warcraft.legends.api.item.crate.LootCrateReader;
import gg.warcraft.legends.api.item.crate.LootCrateType;
import gg.warcraft.legends.api.item.crate.service.LootCrateRepository;
import gg.warcraft.monolith.api.entity.attribute.Attribute;
import gg.warcraft.monolith.api.item.Item;
import gg.warcraft.monolith.api.item.ItemReader;
import gg.warcraft.monolith.api.item.ItemReaderFactory;

public class SimpleLootCrateReader implements LootCrateReader {
    private final Item item;
    private final LootCrateRepository lootCrateRepository;
    private final ItemReader reader;

    @Inject
    public SimpleLootCrateReader(LootCrateRepository lootCrateRepository, ItemReaderFactory itemReaderFactory,
                                 @Assisted Item item) {
        this.item = item;
        this.lootCrateRepository = lootCrateRepository;
        this.reader = itemReaderFactory.createItemReader(item);
    }

    @Override
    public LootCrateType getCrateType() {
        String name = item.getName();
        for (LootCrateType type : LootCrateType.values()) {
            LootCrate crate = lootCrateRepository.get(type);
            if (crate != null && name.contains(crate.getName())) {
                return type;
            }
        }

        return null;
    }

    @Override
    public String getType() {
        return reader.getType();
    }

    @Override
    public int getAttribute(Attribute attribute) {
        return reader.getAttribute(attribute);
    }
}
