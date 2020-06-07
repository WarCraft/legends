/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.equipment

import gg.warcraft.legends.api.combat.LegendsCombatArchetype
import gg.warcraft.legends.api.entity.{LegendsAttribute, LegendsTeam}
import gg.warcraft.legends.api.item.ItemRarity
import gg.warcraft.legends.api.item.equipment.config.{
  AttributeRollConfig, EquipmentConfigRepository
}
import gg.warcraft.legends.api.item.equipment.config.EquipmentConfig.AttributeDistributionConfig
import gg.warcraft.monolith.api.item.{Item, ItemService}

import scala.util.Random

class EquipmentGenerator(implicit
    itemService: ItemService,
    equipmentConfigRepository: EquipmentConfigRepository
) {
  private final val attributeDistributions = AttributeDistribution.values()

  private lazy val config = equipmentConfigRepository.config

  def generate(archetype: LegendsCombatArchetype, team: LegendsTeam): Item = {
    val typ = randomEquipmentType(archetype)
    generate(archetype, team, typ)
  }

  def generate(
      archetype: LegendsCombatArchetype,
      team: LegendsTeam,
      typ: EquipmentType
  ): Item = {
    val rarityRoll = Random.nextFloat * 100
    val rarity = config.itemRarities.floorEntry(rarityRoll).getValue
    generate(archetype, team, typ, rarity)
  }

  def generate(
      archetype: LegendsCombatArchetype,
      team: LegendsTeam,
      rarity: ItemRarity
  ): Item = {
    val typ = randomEquipmentType(archetype)
    generate(archetype, team, typ, rarity)
  }

  def generate(
      archetype: LegendsCombatArchetype,
      team: LegendsTeam,
      typ: EquipmentType,
      rarity: ItemRarity
  ): Item = {
    ???

    /*
            if (team == null || team == LegendsTeam.WAYFARER) {
            // TODO do we want to return for a random team here?
            return null;
        }

        // TODO remove when Ancient is ready
        if (rarity == ItemRarity.ANCIENT) {
            rarity = ItemRarity.LEGENDARY;
        }

        AttributeDistribution attributeDistribution = getRandomAttributeDistribution();
        String itemTypeString = equipmentConfiguration.getItemTypes().get(type).get(rarity);
        ItemTypeOrVariant itemType = itemService.parseData(itemTypeString);

        List<EquipmentLoreConfiguration> loreConfigurations = equipmentConfiguration.getItemLores().get(specialization)
                .get(team).get(attributeDistribution).get(type).get(rarity);
        EquipmentLoreConfiguration loreConfiguration =
                loreConfigurations.get(random.nextInt(loreConfigurations.size()));

        String itemName = loreConfiguration.getName();
        List<String> itemLore = loreConfiguration.getFlavorText();
        EquipmentBuilder builder = equipmentBuilderFactory.createEquipmentBuilder(itemType, rarity, itemName);
        if (itemLore != null && !itemLore.isEmpty()) {
            builder.withFlavorText(itemLore);
        }

        List<List<AttributeRollConfiguration>> attributeRolls;
        if (equipmentConfiguration.getOffhandTypes().contains(type)) {
            attributeRolls = getOffhandAttributeConfigurationsFor(rarity).get(specialization).get(attributeDistribution);
        } else {
            attributeRolls = getAttributeConfigurationsFor(rarity).get(specialization).get(attributeDistribution);
        }
        List<AttributeRollConfiguration> attributeRoll = attributeRolls.get(random.nextInt(attributeRolls.size()));
        int randomRoll = getRandomRollFor(attributeRoll, rarity);
        Map<LegendsAttribute, Integer> randomAttributeRoll = getRandomAttributesFor(attributeRoll, randomRoll);
        builder.withAttributes(randomAttributeRoll);

        builder.withAvailableUpgrades(random.nextInt(4));
        return builder.build();
     */
  }

  private def randomAttributeDistribution: AttributeDistribution =
    attributeDistributions(Random.nextInt(attributeDistributions.size))

  private def randomEquipmentType(
      archetype: LegendsCombatArchetype
  ): EquipmentType = {
    val validTypes = config.validTypes(archetype)
    validTypes(Random.nextInt(validTypes.size))
  }

  private def attributeDistributions(
      rarity: ItemRarity
  ): Map[LegendsCombatArchetype, AttributeDistributionConfig] = rarity match {
    case ItemRarity.Legendary => config.legendaryRolls
    case _                    => config.attributeRolls
  }

  private def offhandAttributeDistributions(
      rarity: ItemRarity
  ): Map[LegendsCombatArchetype, AttributeDistributionConfig] = rarity match {
    case ItemRarity.Legendary => config.offhandLegendaryRolls
    case _                    => config.offhandAttributeRolls
  }

  private def randomRollFor(
      rolls: List[AttributeRollConfig],
      rarity: ItemRarity
  ): Int = {
    var totalLowRoll = 0
    var totalHighRoll = 0
    rolls.foreach { roll =>
      totalLowRoll += roll.low
      totalHighRoll += roll.high
    }

    val deltaRoll = totalHighRoll - totalLowRoll
    val halfDeltaRoll = deltaRoll / 2
    rarity match {
      case ItemRarity.Common => totalLowRoll + Random.nextInt(halfDeltaRoll + 1)
      case ItemRarity.Rare =>
        val randomHalfDeltaRoll =
          if (halfDeltaRoll > 1) Random.nextInt(halfDeltaRoll - 1)
          else 0
        totalHighRoll - 1 - randomHalfDeltaRoll
      case _ => totalHighRoll
    }
  }

  private def randomAttributes(
      rolls: List[AttributeRollConfig],
      roll: Int
  ): Map[LegendsAttribute, Int] = {
    var randomAttributeRolls: Map[LegendsAttribute, Int] = Map.empty
    var highRolls: Map[LegendsAttribute, Int] = Map.empty
    var remainingRoll = roll
    rolls.foreach { roll =>
      randomAttributeRolls += (roll.attribute -> roll.low)
      highRolls += (roll.attribute -> roll.high)
      remainingRoll -= roll.low
    }
    while (remainingRoll > 0) {
      val randomAttribute = rolls(Random.nextInt(rolls.size)).attribute
      val currentRoll = randomAttributeRolls(randomAttribute)
      val maxRoll = highRolls(randomAttribute)
      if (currentRoll < maxRoll) {
        val newRoll = currentRoll + 1
        randomAttributeRolls += (randomAttribute -> newRoll)
        remainingRoll -= 1
      }
    }
    randomAttributeRolls
  }
}
