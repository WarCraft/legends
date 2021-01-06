/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.crate

import gg.warcraft.combat.api.combatclass.CombatClassService
import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.combat.api.util.entity._
import gg.warcraft.legends.api.LegendsConfig
import gg.warcraft.legends.api.item.ItemRarity
import gg.warcraft.legends.api.item.ItemRarity._
import gg.warcraft.legends.api.item.equipment.EquipmentGenerator
import gg.warcraft.legends.api.item.equipment.config.EquipmentConfigRepository
import gg.warcraft.legends.api.item.equipment.upgrade.EquipmentUpgradeGenerator
import gg.warcraft.legends.api.player.currency.LegendsCurrency._
import gg.warcraft.legends.api.shop.profile.ShopProfileService
import gg.warcraft.legends.api.shop.{DecorationPack, ShopItem, ShopService}
import gg.warcraft.legends.api.util.combatclass.CombatClassOps
import gg.warcraft.monolith.api.core.Message
import gg.warcraft.monolith.api.core.event.EventService
import gg.warcraft.monolith.api.item.{
  GoldenApple, ItemService, ItemType, ItemTypeOrVariant
}
import gg.warcraft.monolith.api.player.currency.CurrencyService
import gg.warcraft.monolith.api.player.{Player, PlayerService}
import gg.warcraft.monolith.api.util.string._

import java.util.UUID
import java.util.logging.Logger
import scala.util.Random

class LootCrateService(implicit
    logger: Logger,
    playerService: PlayerService,
    currencyService: CurrencyService,
    itemService: ItemService,
    equipmentConfigRepository: EquipmentConfigRepository,
    combatClassService: CombatClassService,
    combatDataService: CombatDataService,
    shopService: ShopService,
    shopProfileService: ShopProfileService,
    eventService: EventService
) {
  private final val itemsPerCrate = 3
  private final val missingCombatClassOrTeam = Message.server(
    "Please choose a Combat Class and Faction before opening any loot crates."
  )
  private final val noInventorySpace = Message.server(
    s"Please make sure you have at least $itemsPerCrate free inventory slots."
  )

  private val equipmentGenerator = new EquipmentGenerator()
  private val equipUpgradeGenerator = new EquipmentUpgradeGenerator()

  private var _crates: Map[String, LootCrate] = Map.empty
  def crates: Map[String, LootCrate] = _crates

  def readConfig(config: LegendsConfig): Unit =
    config.lootCrates
      .map { _.parse() }
      .foreach { crate =>
        val totalDropChance = crate.dropTables.map { _.chance }.sum
        if (totalDropChance == 100f) {
          val dropChances =
            crate.dropTables.map { _.distribution.values.sum }.sum
          if (dropChances == 100f * crate.dropTables.size) {
            _crates += (crate.id -> crate)
          } else logger.severe(s"One of ${crate.id}'s tables does not sum to 100%!'")
        } else logger.severe(s"${crate.id}'s drop tables do not sum to 100%!'")
      }

  private def notifyReceived(playerId: UUID, name: String): Unit = {
    val strippedName = name.stripChatCodes
    val prefix =
      if (strippedName(0).isDigit) ""
      else if (strippedName(0).isVowel) "an "
      else "a "
    val message = Message.server(s"You received $prefix$strippedName!")
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
      s"You opened $prefix $strippedName Decoration Pack, but you already have some of its contents unlocked."
    )
    playerService.sendMessage(playerId, message)
  }

  private def notifyReimbursement(playerId: UUID, amount: Int): Unit = {
    val message = Message.server(s"You received $amount $Fragments in their stead.")
    playerService.sendMessage(playerId, message)
  }

  private def award(
      typ: ShopItem.Type,
      typName: String,
      rarity: ItemRarity,
      player: Player
  ): Boolean =
    shopService.itemsByRarity(typ).get(rarity) match {
      case Some(items) =>
        val item = items(Random.nextInt(items.size))
        val profile = shopProfileService.profile(player.id)
        if (profile.unlocked.contains(item.id)) {
          val reimbursement = (item.price * 0.33).toInt
          currencyService.addCurrency(Fragments, reimbursement, player.id)
          notifyDuplicate(player.id, item.name, typName)
          notifyReimbursement(player.id, reimbursement)
        } else {
          shopProfileService.unlock(item.id, player.id)
          notifyUnlocked(player.id, item.name, typName)
        }
        true
      case None =>
        logger.severe(
          s"Failed to award $typName to ${player.id}, none exist for $rarity rarity!"
        )
        false
    }

  private def awardCombatClass(player: Player, rarity: ItemRarity): Boolean =
    award(ShopItem.CombatClass, "Combat Class", rarity, player)

  private def awardConsumable(playerId: UUID, rarity: ItemRarity): Boolean = {
    val typ: ItemTypeOrVariant = rarity match {
      case ItemRarity.Common => ItemType.CARROT
      case ItemRarity.Rare   => ItemType.GOLDEN_CARROT
      case _                 => ItemType.GOLDEN_APPLE
    }

    var item = itemService.create(typ)
      .withHideAttributes(true)
      .withTooltip("Consumable")
    if (rarity == ItemRarity.Legendary)
      item = item.asInstanceOf[GoldenApple].copy(enchanted = true)

    playerService.giveItem(playerId, item)
    notifyReceived(playerId, item.name)
    true
  }

  private def awardDecoration(player: Player, rarity: ItemRarity): Boolean =
    award(ShopItem.Decoration, "Decoration", rarity, player)

  private def awardDecorationPack(player: Player, rarity: ItemRarity): Boolean =
    shopService.itemsByRarity(ShopItem.DecorationPack).get(rarity) match {
      case Some(items) =>
        val item = items(Random.nextInt(items.size)).asInstanceOf[DecorationPack]
        val profile = shopProfileService.profile(player.id)
        lazy val duplicates = item.decorations
          .count { it => profile.unlocked.contains(it.id) }
        if (
          profile.unlocked.contains(item.id) ||
          duplicates == item.decorations.size
        ) {
          val reimbursement = (item.price * 0.33).toInt
          currencyService.addCurrency(Fragments, reimbursement, player.id)
          notifyDuplicate(player.id, item.name, "Decoration Pack")
          notifyReimbursement(player.id, reimbursement)
        } else {
          shopProfileService.unlock(item.id, player.id)
          notifyUnlocked(player.id, item.name, "Decoration Pack")

          if (duplicates > 0) {
            val reimbursement = ((item.price * 0.33).toInt *
              (duplicates.toFloat / item.decorations.size.toFloat)).toInt
            currencyService.addCurrency(Fragments, reimbursement, player.id)
            notifyDuplicatePack(player.id, item.name)
            notifyReimbursement(player.id, reimbursement)
          }
        }
        true
      case None =>
        logger.severe(
          s"Failed to award Decoration Pack to ${player.id}, none exist for $rarity rarity!"
        )
        false
    }

  private def awardEquipment(player: Player, rarity: ItemRarity): Boolean =
    (player.combatClass, player.team) match {
      case (Some(combatClass), Some(team)) =>
        val item = equipmentGenerator.generate(combatClass, team, rarity)
        itemService.giveTo(player.id, item)
        notifyReceived(player.id, item.name)
        true
      case _ =>
        playerService.sendMessage(player.id, missingCombatClassOrTeam)
        false
    }

  private def awardEquipUpgrade(player: Player, rarity: ItemRarity): Boolean =
    player.combatClass match {
      case Some(combatClass) =>
        val upgrade = equipUpgradeGenerator.generate(combatClass.archetype, rarity)
        itemService.giveTo(player.id, upgrade)
        notifyReceived(player.id, upgrade.name)
        true
      case None =>
        playerService.sendMessage(player.id, missingCombatClassOrTeam)
        false
    }

  private def awardGold(playerId: UUID, rarity: ItemRarity): Boolean = {
    val amount = rarity match {
      case Common => 25
      case Rare   => 100
      case Epic   => 300
      case _      => 600
    }
    currencyService.addCurrency(Gold, amount, playerId)
    notifyReceived(playerId, s"$amount Gold")
    true
  }

  private def awardMiniPet(player: Player, rarity: ItemRarity): Boolean =
    award(ShopItem.MiniPet, "Mini Pet", rarity, player)

  def openCrate(player: Player, crate: LootCrate): Unit =
    if (player.inventory.hasSpace(itemsPerCrate)) {
      if (itemService.takeFrom(player.id, crate)) {
        var i = 0
        while (i < 3) {
          val dropTableRoll = 100 * Random.nextFloat
          val dropTable = crate.dropTablesMap.minAfter(dropTableRoll).get._2

          val rarityRoll = 100 * Random.nextFloat
          val rarity = dropTable.distributionMap.minAfter(rarityRoll).get._2

          val success = dropTable.typ match {
            case LootCrate.CombatClass      => awardCombatClass(player, rarity)
            case LootCrate.Consumable       => awardConsumable(player.id, rarity)
            case LootCrate.Decoration       => awardDecoration(player, rarity)
            case LootCrate.DecorationPack   => awardDecorationPack(player, rarity)
            case LootCrate.Equipment        => awardEquipment(player, rarity)
            case LootCrate.EquipmentUpgrade => awardEquipUpgrade(player, rarity)
            case LootCrate.Gold             => awardGold(player.id, rarity)
            case LootCrate.MiniPet          => awardMiniPet(player, rarity)
          }
          if (success) i += 1
        }

        eventService << LootCrateOpenedEvent(player, crate)
        // TODO add opened items to event
      } else logger.severe(
        s"Failed to take $crate from ${player.id} after opening crate!"
      )
    } else playerService.sendMessage(player.id, noInventorySpace)
}
