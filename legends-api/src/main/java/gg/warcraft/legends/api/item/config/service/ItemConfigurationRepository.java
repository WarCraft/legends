package gg.warcraft.legends.api.item.config.service;

import gg.warcraft.legends.api.item.config.EquipmentConfiguration;

public interface ItemConfigurationRepository {

    EquipmentConfiguration getConfiguration();

    void save(EquipmentConfiguration configuration);
}
