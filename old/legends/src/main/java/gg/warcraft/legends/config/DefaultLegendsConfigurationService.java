package gg.warcraft.legends.config;

import gg.warcraft.legends.siege.SiegeWeapon;
import gg.warcraft.monolith.LoggerService;
import gg.warcraft.monolith.Monolith;
import gg.warcraft.monolith.combat.MonolithAbility;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.config.SimpleAbilityConfiguration;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import static spark.Spark.port;
import static spark.Spark.post;

public class DefaultLegendsConfigurationService implements LegendsConfigurationService, Listener {

    @Override
    public ServerConfiguration getServerConfiguration() {
        World world = Monolith.getInstance().getWorld();
        return new SimpleServerConfiguration(world);
    }

    @Override
    public List<ObjectiveConfiguration> getObjectiveConfigurations() {
        return objectiveConfigurations.getKeys(false).stream()
                .map(key -> objectiveConfigurations.getConfigurationSection(key))
                .map(SimpleObjectiveConfiguration::new)
                .collect(Collectors.toList());
    }

    @Override
    public AbilityConfiguration getAbilityConfigurationFor(Class<? extends MonolithAbility> ability) {
        String path = ability.getCanonicalName()
                .replace(MONOLITH_COMBAT_PACKAGE, "")
                .replace(LEGENDS_COMBAT_PACKAGE, "");
        ConfigurationSection configuration = abilityConfigurations.getConfigurationSection(path);
        ConfigurationSection configurationClone = YamlConfigurationUtils.clone(configuration);
        return new SimpleAbilityConfiguration(configurationClone);
    }
}
