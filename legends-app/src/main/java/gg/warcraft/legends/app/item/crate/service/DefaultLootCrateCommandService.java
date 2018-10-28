package gg.warcraft.legends.app.item.crate.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import gg.warcraft.combat.api.specialization.Specialization;
import gg.warcraft.legends.api.combat.LegendsSpecialization;
import gg.warcraft.legends.api.combat.LegendsSpecializationType;
import gg.warcraft.legends.api.config.ServerConfiguration;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.item.ConsumableBuilder;
import gg.warcraft.legends.api.item.ConsumableBuilderFactory;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.item.crate.LootCrate;
import gg.warcraft.legends.api.item.crate.LootCrateDropTable;
import gg.warcraft.legends.api.item.crate.LootCrateDropType;
import gg.warcraft.legends.api.item.crate.LootCrateType;
import gg.warcraft.legends.api.item.crate.config.LootCrateDropTableConfiguration;
import gg.warcraft.legends.api.item.crate.service.LootCrateCommandService;
import gg.warcraft.legends.api.item.crate.service.LootCrateRepository;
import gg.warcraft.legends.api.item.equipment.EquipmentGenerator;
import gg.warcraft.legends.api.item.equipment.EquipmentGeneratorFactory;
import gg.warcraft.legends.app.item.crate.SimpleLootCrate;
import gg.warcraft.legends.app.item.crate.SimpleLootCrateDropTable;
import gg.warcraft.monolith.api.core.PluginLogger;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.item.Item;
import gg.warcraft.monolith.api.item.ItemType;
import gg.warcraft.monolith.api.util.ColorCode;
import gg.warcraft.monolith.api.util.StringUtils;

import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

@Singleton
public class DefaultLootCrateCommandService implements LootCrateCommandService {
    private final LootCrateRepository lootCrateRepository;
    private final PlayerCommandService playerCommandService;
    private final LegendsPlayerQueryService playerQueryService;
    private final EquipmentGeneratorFactory equipmentGeneratorFactory;
    private final ConsumableBuilderFactory consumableBuilderFactory;
    private final StringUtils stringUtils;
    private final Logger pluginLogger;
    private final Random random;

    @Inject
    public DefaultLootCrateCommandService(LootCrateRepository lootCrateRepository,
                                          PlayerCommandService playerCommandService,
                                          LegendsPlayerQueryService playerQueryService,
                                          EquipmentGeneratorFactory equipmentGeneratorFactory,
                                          ConsumableBuilderFactory consumableBuilderFactory,
                                          StringUtils stringUtils, @PluginLogger Logger pluginLogger) {
        this.lootCrateRepository = lootCrateRepository;
        this.playerCommandService = playerCommandService;
        this.playerQueryService = playerQueryService;
        this.equipmentGeneratorFactory = equipmentGeneratorFactory;
        this.consumableBuilderFactory = consumableBuilderFactory;
        this.stringUtils = stringUtils;
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
                pluginLogger.warning("Skipping crate " + crateType + ", it's drop table does not add up to 100%");
                return;
            }

            // validate drop table distributions
            for (LootCrateDropTableConfiguration table : crateConfiguration.getDropTable()) {
                int totalDistributionChance = table.getDistribution().entrySet().stream()
                        .mapToInt(Map.Entry::getValue)
                        .sum();
                if (totalDistributionChance != 100) {
                    pluginLogger.warning("Skipping crate " + crateType + ", it's " + table.getType() +
                            " distribution does not add up to 100%");
                    return;
                }
            }

            // create loot crate
            String crateName = crateConfiguration.getName();
            ItemRarity crateRarity = crateConfiguration.getRarity();
            ItemType itemType = crateConfiguration.getItemType();
            SortedMap<Integer, LootCrateDropTable> dropTables = new TreeMap<>();
            AtomicInteger cumulativeDropTableChance = new AtomicInteger();
            crateConfiguration.getDropTable().forEach(dropTableConfiguration -> {
                LootCrateDropType dropType = dropTableConfiguration.getType();
                SortedMap<Integer, ItemRarity> distribution = new TreeMap<>();
                AtomicInteger cumulativeDropChance = new AtomicInteger();
                dropTableConfiguration.getDistribution().forEach((dropRarity, dropChance) ->
                        distribution.put(cumulativeDropChance.getAndAdd(dropChance), dropRarity));
                LootCrateDropTable dropTable = new SimpleLootCrateDropTable(dropType, distribution);

                int dropChance = dropTableConfiguration.getChance();
                int dropTableChance = cumulativeDropTableChance.getAndAdd(dropChance);
                dropTables.put(dropTableChance, dropTable);
            });
            LootCrate crate = new SimpleLootCrate(crateName, crateType, crateRarity, itemType, dropTables);

            lootCrateRepository.save(crate);
        });
    }

    private void sendReceivedNotification(UUID playerId, String itemName) {
        String strippedName = stringUtils.removeChatCodes(itemName);
        String prefix = stringUtils.isVowel(strippedName.charAt(0)) ? "an " : "a ";
        playerCommandService.sendNotification(playerId, "You received " + prefix + itemName + ColorCode.YELLOW + "!");
    }

    private void awardRandomConsumable(UUID playerId, ItemRarity rarity) {
        ItemType type;
        String name;
        switch (rarity) {
            case RARE:
                type = ItemType.GOLD_CARROT;
                name = "Golden Carrot";
                break;
            case EPIC:
                type = ItemType.GOLD_APPLE;
                name = "Golden Apple";
                break;
            case LEGENDARY:
            case ANCIENT:
                type = ItemType.ENCHANTED_GOLD_APPLE;
                name = "Enchanted Golden Apple";
                break;
            case COMMON:
            default:
                type = ItemType.CARROT;
                name = "Carrot";
                break;
        }

        ConsumableBuilder builder = consumableBuilderFactory.createConsumableBuilder(type, name, rarity);
        Item consumable = builder.build();
        playerCommandService.giveItem(playerId, consumable, false);
        sendReceivedNotification(playerId, consumable.getName());
    }

    private void awardRandomEquipment(UUID playerId, ItemRarity rarity) {
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        if (player == null) {
            pluginLogger.warning("Failed to award random equipment to player " + playerId + ", player not found.");
            return;
        }

        EquipmentGenerator equipmentGenerator = equipmentGeneratorFactory.createEquipmentGenerator();
        Specialization specialization = player.getSpecialization();
        LegendsSpecializationType specializationType = null;
        for (LegendsSpecialization legendsSpecialization : LegendsSpecialization.values()) {
            if (legendsSpecialization.getName().equals(specialization.getName())) {
                specializationType = legendsSpecialization.getType();
            }
        }
        if (specializationType == null) {
            pluginLogger.warning("Failed to award random equipment to player " + playerId +
                    ", could not determine specialization type for " + specialization.getName());
            return;
        }

        Item equipment = equipmentGenerator.generate(specializationType, rarity, player.getTeam());
        playerCommandService.giveItem(playerId, equipment, false);
        sendReceivedNotification(playerId, equipment.getName());
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
    public boolean openCrate(UUID playerId, LootCrateType type) {
        LootCrate crate = lootCrateRepository.get(type);
        if (crate == null) {
            pluginLogger.warning("Failed to open crate with type " + type + " for player "
                    + playerId + ", it doesn't exist");
            return false;
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
        return true;
    }
}
