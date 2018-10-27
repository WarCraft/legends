package gg.warcraft.legends.app.item.config.service;

import gg.warcraft.legends.api.item.config.EquipmentConfiguration;
import gg.warcraft.legends.api.item.config.service.ItemConfigurationRepository;

public class DefaultItemConfigurationRepository implements ItemConfigurationRepository {
    private EquipmentConfiguration configuration;

    @Override
    public EquipmentConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public void save(EquipmentConfiguration configuration) {
        this.configuration = configuration;
    }
}
