/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.crate

import gg.warcraft.legends.api.LegendsConfig
import gg.warcraft.legends.api.item.ItemRarity
import gg.warcraft.legends.api.item.ItemRarity._
import gg.warcraft.legends.api.player.currency.LegendsCurrency
import gg.warcraft.monolith.api.core.Message
import gg.warcraft.monolith.api.item.{Carrot, GoldenApple, GoldenCarrot}
import gg.warcraft.monolith.api.player.PlayerService
import gg.warcraft.monolith.api.player.currency.CurrencyService
import gg.warcraft.monolith.api.util.string._

import java.util.UUID
import java.util.logging.Logger

class LootCrateService(implicit
    logger: Logger,
    playerService: PlayerService,
    currencyService: CurrencyService
) {
  private var _crates: Map[String, LootCrate] = Map.empty
  def crates: Map[String, LootCrate] = _crates

  def readConfig(config: LegendsConfig): Unit =
    config.lootCrates.foreach { crate =>
      val totalDropChance = crate.dropTables.map { _.chance }.sum
      if (totalDropChance == 100f) {
        val dropChances = crate.dropTables.map { _.distribution.values.sum }.sum
        if (dropChances == 100f * crate.dropTables.size) {
          _crates += (crate.id -> crate)
        } else logger.severe(s"One of ${crate.id}'s tables does not sum to 100%!'")
      } else logger.severe(s"${crate.id}'s drop tables do not sum to 100%!'")
    }

  private def notifyReceived(playerId: UUID, name: String): Unit = {
    val strippedName = name.stripChatCodes
    val prefix = if (strippedName(0).isVowel) "an" else "a"
    val message = Message.server(s"You received $prefix $strippedName!")
    playerService.sendMessage(playerId, message)
  }

  private def notifyUnlocked(playerId: UUID, name: String, typed: String): Unit = {
    val strippedName = name.stripChatCodes
    val message = Message.server(s"You unlocked the $strippedName $typed!")
    playerService.sendMessage(playerId, message)
  }

  private def notifyDuplicate(playerId: UUID, name: String, typed: String): Unit = {
    val strippedName = name.stripChatCodes
    val prefix = if (strippedName(0).isVowel) "an" else "a"
    val message = Message.server(
      s"You opened $prefix $strippedName $typed, but you already have this unlocked."
    )
    playerService.sendMessage(playerId, message)
  }

  private def notifyDuplicatePack(playerId: UUID, name: String): Unit = {
    val strippedName = name.stripChatCodes
    val prefix = if (strippedName(0).isVowel) "an" else "a"
    val message = Message.server(
      s"You opened $prefix $strippedName Decoration Pack, but you already have some of its content unlocked."
    )
    playerService.sendMessage(playerId, message)
  }

  private def awardConsumable(playerId: UUID, rarity: ItemRarity): Boolean = {
    val tooltip = List("Consumable")
    val item = rarity match {
      case Common => // TODO add default values to item case classes
        Carrot(s"${Common.color}Carrot", tooltip, 1, Set.empty, true)
      case Rare =>
        GoldenCarrot(s"${Rare.color}Golden Carrot", tooltip, 1, Set.empty, true)
      case Epic =>
        GoldenApple(false, s"${Epic.color}Golden Apple", tooltip, 1, Set.empty, true)
      case Legendary =>
        GoldenApple(
          true,
          s"${Legendary.color}Enchanted Golden Apple",
          tooltip,
          1,
          Set.empty,
          true
        )
    }
    playerService.giveItem(playerId, item)
    // TODO receiveSendNotification(playerId, item.name)
    true
  }

  private def awardGold(playerId: UUID, rarity: ItemRarity): Boolean = {
    val amount = rarity match {
      case Common    => 25
      case Rare      => 100
      case Epic      => 300
      case Legendary => 600
    }
    currencyService.addCurrency(LegendsCurrency.Gold, amount, playerId)
    true
  }

  def openCrate(playerId: UUID, crateType: String): Boolean = {

    /*
       LegendsPlayer player = playerQueryService.getPlayer(playerId);
        Inventory inventory = player.getInventory();
        if (inventory.hasSpace(ITEMS_PER_CRATE)) {
            if(itemService.takeFrom(playerId, ((StackableItem) itemInHand).withCount(1))) {
                for (int i = 0; i < ITEMS_PER_CRATE; i += 1) {
                    lootCrateCommandService.openCrate(playerId, crateType);
                }
                LootCrateOpenedEvent event = new SimpleLootCrateOpenedEvent(playerId);
                eventService.publish(event);
            } else {
                System.out.println("Failed to take item from " + playerId + " inventory for loot crate " + itemInHand);
            }
        } else {
            playerCommandService.sendNotification(playerId, "Please make sure you have at least " + ITEMS_PER_CRATE +
                    " empty slots in your inventory before opening a Loot Crate.");
        }
        return true;
     */
    false
  }
}

/*
public class DefaultLootCrateCommandService implements LootCrateCommandService {





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

 */
