package gg.warcraft.legends.app.item.crate.service;

import com.google.common.util.concurrent.AtomicDouble;
import com.google.inject.Inject;
import gg.warcraft.combat.api.specialization.Specialization;
import gg.warcraft.legends.api.combat.LegendsSpecialization;
import gg.warcraft.legends.api.combat.LegendsSpecializationType;
import gg.warcraft.legends.api.config.ServerConfiguration;
import gg.warcraft.legends.api.entity.player.LegendsCurrency;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
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
import gg.warcraft.legends.api.item.equipment.upgrade.EquipmentUpgradeGenerator;
import gg.warcraft.legends.api.item.equipment.upgrade.EquipmentUpgradeGeneratorFactory;
import gg.warcraft.legends.api.shop.Decoration;
import gg.warcraft.legends.api.shop.DecorationId;
import gg.warcraft.legends.api.shop.DecorationPack;
import gg.warcraft.legends.api.shop.DecorationPackId;
import gg.warcraft.legends.api.shop.MiniPetId;
import gg.warcraft.legends.api.shop.ShopItem;
import gg.warcraft.legends.api.shop.ShopItemType;
import gg.warcraft.legends.api.shop.SpecializationId;
import gg.warcraft.legends.api.shop.profile.ShopProfile;
import gg.warcraft.legends.api.shop.profile.service.ShopProfileCommandService;
import gg.warcraft.legends.api.shop.profile.service.ShopProfileQueryService;
import gg.warcraft.legends.api.shop.service.ShopQueryService;
import gg.warcraft.legends.app.item.crate.SimpleLootCrate;
import gg.warcraft.legends.app.item.crate.SimpleLootCrateDropTable;
import gg.warcraft.monolith.api.core.EventService;
import gg.warcraft.monolith.api.core.PluginLogger;
import gg.warcraft.monolith.api.util.ColorCode;
import gg.warcraft.monolith.api.util.StringUtils;
import gg.warcraft.monolith.api.world.item.Item;
import gg.warcraft.monolith.api.world.item.ItemType;

import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Logger;

public class DefaultLootCrateCommandService implements LootCrateCommandService {
    private final LootCrateRepository lootCrateRepository;
    private final LegendsPlayerCommandService playerCommandService;
    private final LegendsPlayerQueryService playerQueryService;
    private final EquipmentGeneratorFactory equipmentGeneratorFactory;
    private final EquipmentUpgradeGeneratorFactory equipmentUpgradeGeneratorFactory;
    private final ConsumableBuilderFactory consumableBuilderFactory;
    private final ShopQueryService shopQueryService;
    private final ShopProfileQueryService shopProfileQueryService;
    private final ShopProfileCommandService shopProfileCommandService;
    private final EventService eventService;
    private final StringUtils stringUtils;
    private final Logger pluginLogger;
    private final Random random;

    @Inject
    public DefaultLootCrateCommandService(LootCrateRepository lootCrateRepository,
                                          LegendsPlayerCommandService playerCommandService,
                                          LegendsPlayerQueryService playerQueryService,
                                          EquipmentGeneratorFactory equipmentGeneratorFactory,
                                          EquipmentUpgradeGeneratorFactory equipmentUpgradeGeneratorFactory,
                                          ConsumableBuilderFactory consumableBuilderFactory,
                                          ShopQueryService shopQueryService,
                                          ShopProfileQueryService shopProfileQueryService,
                                          ShopProfileCommandService shopProfileCommandService,
                                          EventService eventService,
                                          StringUtils stringUtils, Random random,
                                          @PluginLogger Logger pluginLogger) {
        this.lootCrateRepository = lootCrateRepository;
        this.playerCommandService = playerCommandService;
        this.playerQueryService = playerQueryService;
        this.equipmentGeneratorFactory = equipmentGeneratorFactory;
        this.equipmentUpgradeGeneratorFactory = equipmentUpgradeGeneratorFactory;
        this.consumableBuilderFactory = consumableBuilderFactory;
        this.shopQueryService = shopQueryService;
        this.shopProfileQueryService = shopProfileQueryService;
        this.shopProfileCommandService = shopProfileCommandService;
        this.eventService = eventService;
        this.stringUtils = stringUtils;
        this.pluginLogger = pluginLogger;
        this.random = random;
    }

    @Override
    public void readConfiguration(ServerConfiguration configuration) {
        pluginLogger.info("Initializing loot crates");

        configuration.getLootCrates().forEach(crateConfiguration -> {
            LootCrateType crateType = crateConfiguration.getType();

            // validate drop table
            float totalDropChance = (float) crateConfiguration.getDropTable().stream()
                    .mapToDouble(LootCrateDropTableConfiguration::getChance)
                    .sum();
            if (totalDropChance != 100) {
                pluginLogger.warning("Skipping crate " + crateType + ", it's drop table does not add up to 100%");
                return;
            }

            // validate drop table distributions
            for (LootCrateDropTableConfiguration table : crateConfiguration.getDropTable()) {
                float totalDistributionChance = (float) table.getDistribution().entrySet().stream()
                        .mapToDouble(Map.Entry::getValue)
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
            SortedMap<Float, LootCrateDropTable> dropTables = new TreeMap<>();
            AtomicDouble cumulativeDropTableChance = new AtomicDouble();
            crateConfiguration.getDropTable().forEach(dropTableConfiguration -> {
                LootCrateDropType dropType = dropTableConfiguration.getType();
                SortedMap<Float, ItemRarity> distribution = new TreeMap<>();
                AtomicDouble cumulativeDropChance = new AtomicDouble();
                dropTableConfiguration.getDistribution().forEach((dropRarity, dropChance) ->
                        distribution.put((float) cumulativeDropChance.getAndAdd(dropChance), dropRarity));
                LootCrateDropTable dropTable = new SimpleLootCrateDropTable(dropType, distribution);

                float dropChance = dropTableConfiguration.getChance();
                float dropTableChance = (float) cumulativeDropTableChance.getAndAdd(dropChance);
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

    private void sendCurrencyNotification(UUID playerId, int amount, String currencyName) {
        playerCommandService.sendNotification(playerId, "You received " + amount + " " + currencyName + ColorCode.YELLOW + "!");
    }

    private void sendUnlockedNotification(UUID playerId, String unlockName, String unlockType) {
        String strippedName = stringUtils.removeChatCodes(unlockName);
        String prefix = stringUtils.isVowel(strippedName.charAt(0)) ? "an " : "a ";
        playerCommandService.sendNotification(playerId, "You unlocked " + prefix + unlockName + ColorCode.YELLOW +
                " " + unlockType + "!");
    }

    private void sendReimbursementNotification(UUID playerId, String unlockName, String unlockType, int amount) {
        String strippedName = stringUtils.removeChatCodes(unlockName);
        String prefix = stringUtils.isVowel(strippedName.charAt(0)) ? "an " : "a ";
        playerCommandService.sendNotification(playerId, "You opened " + prefix + unlockName + ColorCode.YELLOW +
                " " + unlockType + ", but you already have this unlocked so were granted " + ColorCode.GOLD +
                amount + " Fragments " + ColorCode.YELLOW + "instead.");
    }

    private void sendPackReimbursementNotification(UUID playerId, String unlockName, int count, int amount) {
        String strippedName = stringUtils.removeChatCodes(unlockName);
        String prefix = stringUtils.isVowel(strippedName.charAt(0)) ? "an " : "a ";
        playerCommandService.sendNotification(playerId, "You opened " + prefix + unlockName + ColorCode.YELLOW +
                " Decoration Pack" + ", but you already had " + count + " of its decorations unlocked so were granted " +
                ColorCode.GOLD + amount + " Fragments " + ColorCode.YELLOW + "in their stead.");
    }

    private boolean awardRandomConsumable(UUID playerId, ItemRarity rarity) {
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
        return true;
    }

    private boolean awardRandomEquipment(UUID playerId, ItemRarity rarity) {
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        if (player == null) {
            pluginLogger.warning("Failed to award random equipment to player " + playerId + ", player not found.");
            return false;
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
            return false;
        }

        Item equipment = equipmentGenerator.generate(specializationType, rarity, player.getTeam());
        playerCommandService.giveItem(playerId, equipment, false);
        sendReceivedNotification(playerId, equipment.name());
        return true;
    }

    private boolean awardRandomEquipmentUpgrade(UUID playerId, ItemRarity rarity) {
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        if (player == null) {
            pluginLogger.warning("Failed to award random equipment upgrade to player " + playerId + ", player not found.");
            return false;
        }

        EquipmentUpgradeGenerator equipmentUpgradeGenerator =
                equipmentUpgradeGeneratorFactory.createEquipmentUpgradeGenerator();
        Specialization specialization = player.getSpecialization();
        LegendsSpecializationType specializationType = null;
        for (LegendsSpecialization legendsSpecialization : LegendsSpecialization.values()) {
            if (legendsSpecialization.getName().equals(specialization.getName())) {
                specializationType = legendsSpecialization.getType();
            }
        }
        if (specializationType == null) {
            pluginLogger.warning("Failed to award random equipment upgrade to player " + playerId +
                    ", could not determine specialization type for " + specialization.getName());
            return false;
        }

        Item equipmentUpgrade = equipmentUpgradeGenerator.generate(specializationType, rarity);
        playerCommandService.giveItem(playerId, equipmentUpgrade, false);
        sendReceivedNotification(playerId, equipmentUpgrade.name());
        return true;
    }

    private boolean awardRandomDecoration(UUID playerId, ItemRarity rarity) {
        ShopItem decoration = shopQueryService.getRandomItem(ShopItemType.DECORATION, rarity);
        if (decoration == null) {
            pluginLogger.warning("Failed to award random decoration to player " + playerId + ", none exist for rarity " +
                    rarity);
            return false;
        }

        DecorationId decorationId = DecorationId.valueOf(decoration.getId());
        ShopProfile profile = shopProfileQueryService.getProfile(playerId);
        if (profile.hasDecoration(decorationId)) {
            int reimbursement = (int) (decoration.getPrice() * 0.33);
            playerCommandService.addCurrency(playerId, LegendsCurrency.FRAGMENTS, reimbursement);
            sendReimbursementNotification(playerId, decoration.getName(), "Decoration", reimbursement);
        } else {
            shopProfileCommandService.unlockDecoration(decorationId, playerId, false);
            sendUnlockedNotification(playerId, decoration.getName(), "Decoration");
        }
        return true;
    }

    private boolean awardRandomDecorationPack(UUID playerId, ItemRarity rarity) {
        DecorationPack decorationPack = (DecorationPack) shopQueryService.getRandomItem(ShopItemType.DECORATION_PACK, rarity);
        if (decorationPack == null) {
            pluginLogger.warning("Failed to award random decoration pack to player " + playerId +
                    ", none exist for rarity " + rarity);
            return false;
        }

        DecorationPackId decorationPackId = DecorationPackId.valueOf(decorationPack.getId());
        ShopProfile profile = shopProfileQueryService.getProfile(playerId);

        float decorationCount = decorationPack.getDecorations().size();
        float duplicateCount = 0;
        for (Decoration decoration : decorationPack.getDecorations()) {
            DecorationId decorationId = DecorationId.valueOf(decoration.getId());
            if (profile.hasDecoration(decorationId)) {
                duplicateCount += 1;
            }
        }
        float reimbursement = decorationPack.getPrice() * 0.33f * (duplicateCount / decorationCount);

        shopProfileCommandService.unlockDecorationPack(decorationPackId, playerId, false);
        sendUnlockedNotification(playerId, decorationPack.getName(), "Decoration Pack");
        if (duplicateCount > 0) {
            playerCommandService.addCurrency(playerId, LegendsCurrency.FRAGMENTS, (int) reimbursement);
            sendPackReimbursementNotification(playerId, decorationPack.getName(), (int) duplicateCount, (int) reimbursement);
        }
        return true;
    }

    private boolean awardRandomMiniPet(UUID playerId, ItemRarity rarity) {
        ShopItem miniPet = shopQueryService.getRandomItem(ShopItemType.MINI_PET, rarity);
        if (miniPet == null) {
            pluginLogger.warning("Failed to award random mini pet to player " + playerId + ", none exist for rarity " +
                    rarity);
            return false;
        }

        MiniPetId miniPetId = MiniPetId.valueOf(miniPet.getId());
        ShopProfile profile = shopProfileQueryService.getProfile(playerId);
        if (profile.hasMiniPet(miniPetId)) {
            int reimbursement = (int) (miniPet.getPrice() * 0.33);
            playerCommandService.addCurrency(playerId, LegendsCurrency.FRAGMENTS, reimbursement);
            sendReimbursementNotification(playerId, miniPet.getName(), "Mini Pet", reimbursement);
        } else {
            shopProfileCommandService.unlockMiniPet(miniPetId, playerId, false);
            sendUnlockedNotification(playerId, miniPet.getName(), "Mini Pet");
        }
        return true;
    }

    private boolean awardRandomSpecialization(UUID playerId, ItemRarity rarity) {
        ShopItem specialization = shopQueryService.getRandomItem(ShopItemType.SPECIALIZATION, rarity);
        if (specialization == null) {
            pluginLogger.warning("Failed to award random specialization to player " + playerId +
                    ", none exist for rarity " + rarity);
            return false;
        }

        SpecializationId specializationId = SpecializationId.valueOf(specialization.getId());
        ShopProfile profile = shopProfileQueryService.getProfile(playerId);
        if (profile.hasSpecialization(specializationId)) {
            int reimbursement = (int) (specialization.getPrice() * 0.33);
            playerCommandService.addCurrency(playerId, LegendsCurrency.FRAGMENTS, reimbursement);
            sendReimbursementNotification(playerId, specialization.getName(), "Specialization", reimbursement);
        } else {
            shopProfileCommandService.unlockSpecialization(specializationId, playerId, false);
            sendUnlockedNotification(playerId, specialization.getName(), "Specialization");
        }
        return true;
    }

    private boolean awardGold(UUID playerId, ItemRarity rarity) {
        int amount = 0;
        switch (rarity) {
            case COMMON:
                amount = 50;
                break;
            case RARE:
                amount = 150;
                break;
            case EPIC:
                amount = 500;
                break;
            case LEGENDARY:
                amount = 1000;
                break;
            case ANCIENT:
                amount = 1500;
                break;
        }
        playerCommandService.addGold(playerId, amount);
        sendCurrencyNotification(playerId, amount, "gold");
        return true;
    }

    @Override
    public boolean openCrate(UUID playerId, LootCrateType type) {
        LootCrate crate = lootCrateRepository.get(type);
        if (crate == null) {
            pluginLogger.warning("Failed to open crate with type " + type + " for player " + playerId +
                    ", crate doesn't exist");
            return false;
        }

        float dropTypeRoll = 100 * random.nextFloat();
        LootCrateDropTable dropTable = crate.viewDropTable().floorEntry(dropTypeRoll).getValue();
        LootCrateDropType dropType = dropTable.getType();

        float dropRarityRoll = 100 * random.nextFloat();
        ItemRarity dropRarity = dropTable.viewDistribution().floorEntry(dropRarityRoll).getValue();

        boolean success = false;
        switch (dropType) {
            case CONSUMABLE:
                success = awardRandomConsumable(playerId, dropRarity);
                break;
            case EQUIPMENT:
                success = awardRandomEquipment(playerId, dropRarity);
                break;
            case EQUIPMENT_UPGRADE:
                success = awardRandomEquipmentUpgrade(playerId, dropRarity);
                break;
            case DECORATION:
                success = awardRandomDecoration(playerId, dropRarity);
                break;
            case DECORATION_PACK:
                success = awardRandomDecorationPack(playerId, dropRarity);
                break;
            case MINI_PET:
                success = awardRandomMiniPet(playerId, dropRarity);
                break;
            case SPECIALIZATION:
                success = awardRandomSpecialization(playerId, dropRarity);
                break;
            case GOLD:
                success = awardGold(playerId, dropRarity);
                break;
        }

        return success;
    }
}
