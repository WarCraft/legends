package gg.warcraft.legends.api.item.equipment;

import gg.warcraft.monolith.api.world.item.Item;

public interface EquipmentReaderFactory {

    EquipmentReader createEquipmentReader(Item item);
}
