/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.equipment

import gg.warcraft.combat.api.combatclass.{CombatClass, CombatClassService}
import gg.warcraft.legends.api.combat.LegendsCombatArchetype
import gg.warcraft.legends.api.entity.LegendsTeam
import gg.warcraft.legends.api.entity.attribute.LegendsAttribute
import gg.warcraft.legends.api.item.ItemRarity
import gg.warcraft.legends.api.item.equipment.config.EquipmentConfig.AttributeDistributionConfig
import gg.warcraft.legends.api.item.equipment.config.{
  AttributeRollConfig, EquipmentConfigRepository
}
import gg.warcraft.legends.api.util.combatclass.CombatClassOps
import gg.warcraft.monolith.api.entity.team.Team
import gg.warcraft.monolith.api.item.{Item, ItemService}

import scala.util.Random

class EquipmentGenerator(implicit
    itemService: ItemService,
    equipmentConfigRepository: EquipmentConfigRepository,
    combatClassService: CombatClassService
) {
  private final val attributeDistributions = AttributeDistribution.values()

  private lazy val config = equipmentConfigRepository.config

  def generate(
      combatClass: CombatClass,
      team: Team
  ): Item = {
    val typ = randomEquipmentType(combatClass.archetype)
    generate(combatClass, team, typ)
  }

  def generate(
      combatClass: CombatClass,
      team: Team,
      typ: EquipmentType
  ): Item = {
    val rarityRoll = Random.nextFloat * 100
    val rarity = config.navigableDropRates.floorEntry(rarityRoll).getValue
    generate(combatClass, team, typ, rarity)
  }

  def generate(
      combatClass: CombatClass,
      team: Team,
      rarity: ItemRarity
  ): Item = {
    val typ = randomEquipmentType(combatClass.archetype)
    generate(combatClass, team, typ, rarity)
  }

  def generate(
      combatClass: CombatClass,
      team: Team,
      typ: EquipmentType,
      rarity: ItemRarity
  ): Item = {
    if (team.id == LegendsTeam.Wayfarers) throw new IllegalArgumentException

    val archetype = combatClass.archetype
    val attrDistro = randomAttributeDistribution
    val itemType =
      if (typ.isArmor) config.armorTypes(combatClass.archetype)(team)(typ)
      else config.weaponTypes(typ)(rarity)

    val tooltips = config.tooltips(archetype)(team)(attrDistro)(typ)
    val tooltip = rarity match {
      case ItemRarity.Legendary =>
        val legendaryTooltips = tooltips.special(rarity)
        if (legendaryTooltips.isEmpty) tooltips.default
        else legendaryTooltips(Random.nextInt(legendaryTooltips.size))
      case _ => tooltips.default
    }

    val builder = new EquipmentBuilder(itemType, rarity, tooltip.name)
    tooltip.flavorText match {
      case Some(text) if text.nonEmpty => builder.withFlavorText(text)
      case _                           =>
    }

    val attributeRolls =
      if (config.offhandTypes.contains(typ))
        offhandAttributeDistributions(rarity)(archetype)(attrDistro)
      else attributeDistributions(rarity)(archetype)(attrDistro)
    val attributeRoll = attributeRolls(Random.nextInt(attributeRolls.size))

    val randomRoll = randomRollFor(attributeRoll, rarity)
    val randomAttributeRoll = randomAttributes(attributeRoll, randomRoll)
    builder.withAttributes(randomAttributeRoll)

    builder.withAvailableUpgrades(Random.nextInt(4))

    val customModelData =
      try {
        if (typ != EquipmentType.HEAD) config.customModelData(typ)(rarity)(team)
        else config.helmetModelData(combatClass)(rarity)(team)
      } catch { case _: NoSuchElementException => 0 }
    builder.withCustomModelData(customModelData)

    builder.build()
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
