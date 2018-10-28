package gg.warcraft.legends.app.item.crate.service;

import com.google.common.util.concurrent.AtomicDouble;
import com.google.inject.Inject;
import gg.warcraft.combat.api.specialization.Specialization;
import gg.warcraft.legends.api.combat.LegendsSpecialization;
import gg.warcraft.legends.api.combat.LegendsSpecializationType;
import gg.warcraft.legends.api.config.ServerConfiguration;
import gg.warcraft.legends.api.entity.player.LegendsCurrency;
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
import gg.warcraft.legends.api.shop.DecorationId;
import gg.warcraft.legends.api.shop.DecorationPackId;
import gg.warcraft.legends.api.shop.MiniPetId;
import gg.warcraft.legends.api.shop.ShopItem;
import gg.warcraft.legends.api.shop.ShopItemType;
import gg.warcraft.legends.api.shop.profile.ShopProfile;
import gg.warcraft.legends.api.shop.profile.service.ShopProfileCommandService;
import gg.warcraft.legends.api.shop.profile.service.ShopProfileQueryService;
import gg.warcraft.legends.api.shop.service.ShopQueryService;
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
import java.util.logging.Logger;

public class DefaultLootCrateCommandService implements LootCrateCommandService {
    private final LootCrateRepository lootCrateRepository;
    private final PlayerCommandService playerCommandService;
    private final LegendsPlayerQueryService playerQueryService;
    private final EquipmentGeneratorFactory equipmentGeneratorFactory;
    private final ConsumableBuilderFactory consumableBuilderFactory;
    private final ShopQueryService shopQueryService;
    private final ShopProfileQueryService shopProfileQueryService;
    private final ShopProfileCommandService shopProfileCommandService;
    private final StringUtils stringUtils;
    private final Logger pluginLogger;
    private final Random random;

    @Inject
    public DefaultLootCrateCommandService(LootCrateRepository lootCrateRepository,
                                          PlayerCommandService playerCommandService,
                                          LegendsPlayerQueryService playerQueryService,
                                          EquipmentGeneratorFactory equipmentGeneratorFactory,
                                          ConsumableBuilderFactory consumableBuilderFactory,
                                          ShopQueryService shopQueryService,
                                          ShopProfileQueryService shopProfileQueryService,
                                          ShopProfileCommandService shopProfileCommandService,
                                          StringUtils stringUtils, Random random,
                                          @PluginLogger Logger pluginLogger) {
        this.lootCrateRepository = lootCrateRepository;
        this.playerCommandService = playerCommandService;
        this.playerQueryService = playerQueryService;
        this.equipmentGeneratorFactory = equipmentGeneratorFactory;
        this.consumableBuilderFactory = consumableBuilderFactory;
        this.shopQueryService = shopQueryService;
        this.shopProfileQueryService = shopProfileQueryService;
        this.shopProfileCommandService = shopProfileCommandService;
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
        sendReceivedNotification(playerId, equipment.getName());
        return true;
    }

    private boolean awardRandomEquipmentUpgrade(UUID playerId, ItemRarity rarity) {
        sendReceivedNotification(playerId, "Imaginary Equipment Upgrade");
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
        ShopItem decorationPack = shopQueryService.getRandomItem(ShopItemType.DECORATION_PACK, rarity);
        if (decorationPack == null) {
            pluginLogger.warning("Failed to award random decoration pack to player " + playerId +
                    ", none exist for rarity " + rarity);
            return false;
        }

        DecorationPackId decorationPackId = DecorationPackId.valueOf(decorationPack.getId());
        ShopProfile profile = shopProfileQueryService.getProfile(playerId);
        if (profile.hasDecorationPack(decorationPackId)) {
            int reimbursement = (int) (decorationPack.getPrice() * 0.33);
            playerCommandService.addCurrency(playerId, LegendsCurrency.FRAGMENTS, reimbursement);
            sendReimbursementNotification(playerId, decorationPack.getName(), "Decoration Pack", reimbursement);
        } else {
            shopProfileCommandService.unlockDecorationPack(decorationPackId, playerId, false);
            sendUnlockedNotification(playerId, decorationPack.getName(), "Decoration Pack");
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
        // TODO
//        ShopItem specialization = shopQueryService.getRandomItem(ShopItemType.SPECIALIZATION, rarity);
//        SpecializationId specializationId = SpecializationId.valueOf(specialization.getId());
//        ShopProfile profile = shopProfileQueryService.getProfile(playerId);
//        if (profile.hasSpecialization(specializationId)) {
//            int reimbursement = (int) (specialization.getPrice() * 0.33);
//            playerCommandService.addCurrency(playerId, LegendsCurrency.FRAGMENTS, reimbursement);
//            sendReimbursementNotification(playerId, specialization.getName(), "Specialization", reimbursement);
//        } else {
//            shopProfileCommandService.unlockSpecialization(specializationId, playerId, false);
//            sendUnlockedNotification(playerId, specialization.getName(), "Specialization");
//        }
        sendReceivedNotification(playerId, "Imaginary Specialization");
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

        switch (dropType) {
            case CONSUMABLE:
                return awardRandomConsumable(playerId, dropRarity);
            case EQUIPMENT:
                return awardRandomEquipment(playerId, dropRarity);
            case EQUIPMENT_UPGRADE:
                return awardRandomEquipmentUpgrade(playerId, dropRarity);
            case DECORATION:
                return awardRandomDecoration(playerId, dropRarity);
            case DECORATION_PACK:
                return awardRandomDecorationPack(playerId, dropRarity);
            case MINI_PET:
                return awardRandomMiniPet(playerId, dropRarity);
            case SPECIALIZATION:
                return awardRandomSpecialization(playerId, dropRarity);
            default:
                return false;
        }
    }
}
