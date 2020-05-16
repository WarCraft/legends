package gg.warcraft.legends.api.item.equipment;

import gg.warcraft.monolith.api.item.ItemReader;

public interface EquipmentReader extends ItemReader {

    int getAvailableUpgrades();

    int getUpgradeCount();
}
