package gg.warcraft.legends.api.item.config.service;

import gg.warcraft.legends.api.item.config.EquipmentConfiguration;

public interface EquipmentConfigurationRepository {

    EquipmentConfiguration getConfiguration();

    void save(EquipmentConfiguration configuration);
}
