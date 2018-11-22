package gg.warcraft.legends.api.item.equipment.upgrade;

import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.monolith.api.item.Item;

import java.util.Map;

public interface EquipmentUpgradeBuilder {

    EquipmentUpgradeBuilder withAttribute(LegendsAttribute attribute, int amount);

    EquipmentUpgradeBuilder withAttributes(Map<LegendsAttribute, Integer> attributes);

    Item build();
}
