package gg.warcraft.legends.app.item.config.service;

import com.google.inject.Singleton;
import gg.warcraft.legends.api.item.config.EquipmentConfiguration;
import gg.warcraft.legends.api.item.config.service.EquipmentConfigurationRepository;

@Singleton
public class DefaultEquipmentConfigurationRepository implements EquipmentConfigurationRepository {
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
