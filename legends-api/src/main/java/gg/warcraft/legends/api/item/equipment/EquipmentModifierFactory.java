package gg.warcraft.legends.api.item.equipment;

import gg.warcraft.monolith.api.item.Item;

public interface EquipmentModifierFactory {

    EquipmentModifier createEquipmentModifier(Item item);
}
