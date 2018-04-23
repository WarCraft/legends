package gg.warcraft.legends.config;

import gg.warcraft.legends.siege.SiegeWeapon;
import gg.warcraft.monolith.Monolith;
import gg.warcraft.monolith.config.ConfigurationService;

import java.util.List;

public interface LegendsConfigurationService extends ConfigurationService {

    static LegendsConfigurationService getInstance() {
        return (LegendsConfigurationService) Monolith.getInstance().getService(Monolith.Service.CONFIGURATION);
    }

    ServerConfiguration getServerConfiguration();

    List<ObjectiveConfiguration> getObjectiveConfigurations();

    SiegeConfiguration getSiegeConfigurationFor(Class<? extends SiegeWeapon> siege);
}
