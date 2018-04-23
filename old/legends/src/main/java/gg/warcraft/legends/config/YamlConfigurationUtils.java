package gg.warcraft.legends.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class YamlConfigurationUtils extends YamlConfiguration {

    private YamlConfigurationUtils() {

    }

    public static ConfigurationSection clone(ConfigurationSection section) {
        ConfigurationSection clone = new YamlConfiguration();
        YamlConfigurationUtils utils = new YamlConfigurationUtils();
        utils.convertMapsToSections(section.getValues(true), clone);
        return clone;
    }
}
