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
    private static final String MONOLITH_COMBAT_PACKAGE = "gg.warcraft.monolith.combat.";
    private static final String LEGENDS_COMBAT_PACKAGE = "gg.warcraft.legends.combat.";
    private static final String WARCRAFT_ORGANIZATION = "WarCraft";
    private static final String LEGENDS_CONFIG_REPOSITORY = "legends-config";
    private static final String WEBHOOK_PATH = "/legends-config";
    private static final int WEBHOOK_PORT = 60000;

    private final LoggerService loggerService;

    private YamlConfiguration abilityConfigurations;
    private YamlConfiguration objectiveConfigurations;
    private YamlConfiguration siegeConfigurations;

    public DefaultLegendsConfigurationService() {
        this(LoggerService.getInstance());
    }

    public DefaultLegendsConfigurationService(LoggerService loggerService) {
        this.loggerService = loggerService;
        initializeGitHubWebhook();
        updateConfigurations();
    }

    private void initializeGitHubWebhook() {
        port(WEBHOOK_PORT);
        post(WEBHOOK_PATH, (request, response) -> {
            // TODO remove logging
            System.out.println("GitHub Webhook fired, headers: " + request.headers());
            if (request.userAgent().contains("GitHub-Hookshot")) { // TODO make secure
                updateConfigurations();
                response.status(204);
            } else {
                response.status(403);
            }
            return response;
        });
    }

    private GHRepository setupNewGitHubConnection() throws IOException {
        GitHub github = GitHub.connectAnonymously();
        GHOrganization warcraft = github.getOrganization(WARCRAFT_ORGANIZATION);
        return warcraft.getRepository(LEGENDS_CONFIG_REPOSITORY);
    }

    private void updateConfigurations() {
        try {
            GHRepository repository = setupNewGitHubConnection();
            updateAbilityConfigurations(repository);
            updateObjectiveConfigurations(repository);
            // TODO updateSiegeConfigurations(repository);
        } catch (IOException ex) {
            loggerService.severe("Failed to update configurations. Could not initialize GitHub connection: " + ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void updateAbilityConfigurations(GHRepository repository) {
        try {
            GHContent content = repository.getFileContent("abilities.yml");
            try (InputStreamReader reader = new InputStreamReader(content.read())) {
                abilityConfigurations = YamlConfiguration.loadConfiguration(reader);
            }
        } catch (IOException | NullPointerException ex) {
            loggerService.severe("Could not load abilities configurations: " + ex.getMessage());
        }
    }

    private void updateObjectiveConfigurations(GHRepository repository) {
        try {
            GHContent content = repository.getFileContent("objectives.yml");
            try (InputStreamReader reader = new InputStreamReader(content.read())) {
                objectiveConfigurations = YamlConfiguration.loadConfiguration(reader);
            }
        } catch (IOException | NullPointerException ex) {
            loggerService.severe("Could not load objective configurations: " + ex.getMessage());
        }
    }

    private void updateSiegeConfigurations(GHRepository repository) {
        try {
            GHContent content = repository.getFileContent("siege.yml");
            try (InputStreamReader reader = new InputStreamReader(content.read())) {
                siegeConfigurations = YamlConfiguration.loadConfiguration(reader);
            }
        } catch (IOException | NullPointerException ex) {
            loggerService.severe("Could not load siege configurations: " + ex.getMessage());
        }
    }

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
    public SiegeConfiguration getSiegeConfigurationFor(Class<? extends SiegeWeapon> siege) {
        return null; // TODO
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
