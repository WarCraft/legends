package gg.warcraft.legends.api.item.equipment;

import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.monolith.api.world.item.Item;

import java.util.List;
import java.util.Map;

public interface EquipmentBuilder {

    EquipmentBuilder addFlavorText(String flavorText);

    EquipmentBuilder withAttribute(LegendsAttribute attribute, int amount);

    EquipmentBuilder withAttributes(Map<LegendsAttribute, Integer> attributes);

    EquipmentBuilder withAvailableUpgrades(int count);

    EquipmentBuilder withUpgradeCount(int count);

    EquipmentBuilder withFlavorText(List<String> flavorText);

    Item build();
}
