package gg.warcraft.legends.app.item.crate.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import gg.warcraft.legends.api.config.ServerConfiguration;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.item.crate.LootCrate;
import gg.warcraft.legends.api.item.crate.LootCrateDropTable;
import gg.warcraft.legends.api.item.crate.LootCrateDropType;
import gg.warcraft.legends.api.item.crate.LootCrateType;
import gg.warcraft.legends.api.item.crate.config.LootCrateDropTableConfiguration;
import gg.warcraft.legends.api.item.crate.service.LootCrateCommandService;
import gg.warcraft.legends.api.item.crate.service.LootCrateRepository;
import gg.warcraft.legends.app.item.crate.SimpleLootCrate;
import gg.warcraft.legends.app.item.crate.SimpleLootCrateDropTable;
import gg.warcraft.monolith.api.core.PluginLogger;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.item.ItemType;

import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

@Singleton
public class DefaultLootCrateCommandService implements LootCrateCommandService {
    private final PlayerCommandService playerCommandService;
    private final LootCrateRepository lootCrateRepository;
    private final Logger pluginLogger;
    private final Random random;

    @Inject
    public DefaultLootCrateCommandService(PlayerCommandService playerCommandService,
                                          LootCrateRepository lootCrateRepository,
                                          @PluginLogger Logger pluginLogger) {
        this.playerCommandService = playerCommandService;
        this.lootCrateRepository = lootCrateRepository;
        this.pluginLogger = pluginLogger;
        this.random = new Random(System.currentTimeMillis());
    }

    @Override
    public void readConfiguration(ServerConfiguration configuration) {
        pluginLogger.info("Initializing loot crates");

        configuration.getLootCrates().forEach(crateConfiguration -> {
            LootCrateType crateType = crateConfiguration.getType();

            // validate drop table
            int totalDropChance = crateConfiguration.getDropTable().stream()
                    .mapToInt(LootCrateDropTableConfiguration::getChance)
                    .sum();
            if (totalDropChance != 100) {
                pluginLogger.warning("Skipping crate " + crateType +
                        ", it's drop table does not add up to 100%");
                return;
            }

            // validate drop table distributions
            for (LootCrateDropTableConfiguration table : crateConfiguration.getDropTable()) {
                int totalDistributionChance = table.getDistribution().entrySet().stream()
                        .mapToInt(Map.Entry::getValue)
                        .sum();
                if (totalDistributionChance != 100) {
                    pluginLogger.warning("Skipping crate " + crateType + ", it's " +
                            table.getType() + " distribution does not add up to 100%");
                    return;
                }
            }

            // create loot crate
            String crateName = crateConfiguration.getName();
            ItemRarity crateRarity = crateConfiguration.getRarity();
            ItemType itemType = crateConfiguration.getItemType();
            SortedMap<Integer, LootCrateDropTable> dropTables = new TreeMap<>();
            crateConfiguration.getDropTable().forEach(dropTableConfiguration -> {
                LootCrateDropType dropType = dropTableConfiguration.getType();
                SortedMap<Integer, ItemRarity> distribution = new TreeMap<>();
                AtomicInteger cumulativeDropChance = new AtomicInteger();
                dropTableConfiguration.getDistribution().forEach((dropRarity, dropChance) ->
                        distribution.put(cumulativeDropChance.getAndAdd(dropChance), dropRarity));
                LootCrateDropTable dropTable = new SimpleLootCrateDropTable(dropType, distribution);

                int dropChance = dropTableConfiguration.getChance();
                dropTables.put(dropChance, dropTable);
            });
            LootCrate crate = new SimpleLootCrate(crateName, crateType, crateRarity, itemType, dropTables);

            lootCrateRepository.save(crate);
        });
    }

    private void awardRandomConsumable(UUID playerId, ItemRarity rarity) {
        playerCommandService.sendMessage(playerId, "You got an imaginary " + rarity + " consumable!");
    }

    private void awardRandomEquipment(UUID playerId, ItemRarity rarity) {
        playerCommandService.sendMessage(playerId, "You got an imaginary " + rarity + " equipment!");
    }

    private void awardRandomEquipmentUpgrade(UUID playerId, ItemRarity rarity) {
        playerCommandService.sendMessage(playerId, "You got an imaginary " + rarity + " equipment upgrade!");
    }

    private void awardRandomDecoration(UUID playerId, ItemRarity rarity) {
        playerCommandService.sendMessage(playerId, "You got an imaginary " + rarity + " decoration!");
    }

    private void awardRandomDecorationPack(UUID playerId, ItemRarity rarity) {
        playerCommandService.sendMessage(playerId, "You got an imaginary " + rarity + " decoration pack!");
    }

    private void awardRandomMiniPet(UUID playerId, ItemRarity rarity) {
        playerCommandService.sendMessage(playerId, "You got an imaginary " + rarity + " mini pet!");
    }

    @Override
    public void openCrate(UUID playerId, LootCrateType type) {
        LootCrate crate = lootCrateRepository.get(type);
        if (crate == null) {
            pluginLogger.warning("Failed to open crate with type " + type + " for player "
                    + playerId + ", it doesn't exist");
            return;
        }

        int dropTypeRoll = random.nextInt(100);
        LootCrateDropTable dropTable = crate.viewDropTable().floorEntry(dropTypeRoll).getValue();
        LootCrateDropType dropType = dropTable.getType();

        int dropRarityRoll = random.nextInt(100);
        ItemRarity dropRarity = dropTable.viewDistribution().floorEntry(dropRarityRoll).getValue();

        switch (dropType) {
            case CONSUMABLE:
                awardRandomConsumable(playerId, dropRarity);
                break;
            case EQUIPMENT:
                awardRandomEquipment(playerId, dropRarity);
                break;
            case EQUIPMENT_UPGRADE:
                awardRandomEquipmentUpgrade(playerId, dropRarity);
                break;
            case DECORATION:
                awardRandomDecoration(playerId, dropRarity);
                break;
            case DECORATION_PACK:
                awardRandomDecorationPack(playerId, dropRarity);
                break;
            case MINI_PET:
                awardRandomMiniPet(playerId, dropRarity);
                break;
        }
    }
}
