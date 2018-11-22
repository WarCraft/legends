package gg.warcraft.legends.api.item.equipment;

import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.monolith.api.item.Item;

public interface EquipmentModifier {

    EquipmentModifier addAttribute(LegendsAttribute attribute, int amount);

    EquipmentModifier addAvailableUpgrades(int count);

    EquipmentModifier addUpgradeCount(int count);

    Item build();
}
