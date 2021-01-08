/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.util

import gg.warcraft.legends.api.combat.LegendsCombatArchetype
import gg.warcraft.legends.api.entity.attribute.LegendsAttribute
import gg.warcraft.legends.api.item.ItemRarity
import gg.warcraft.legends.api.item.equipment.{AttributeDistribution, EquipmentType}
import gg.warcraft.legends.api.player.challenge.{Challenge, ChallengeService}
import gg.warcraft.legends.api.shop.{ShopItem, ShopService}

object codecs {
  object legends {
    implicit val archetypeDecoder: String => LegendsCombatArchetype =
      LegendsCombatArchetype.valueOf
    implicit val archetypeEncoder: LegendsCombatArchetype => String =
      _.name

    implicit val attributeDecoder: String => LegendsAttribute =
      LegendsAttribute.valueOf
    implicit val attributeEncoder: LegendsAttribute => String =
      _.name

    implicit val attributeDistroDecoder: String => AttributeDistribution =
      AttributeDistribution.valueOf
    implicit val attributeDistroEncoder: AttributeDistribution => String =
      _.name

    implicit def challengeDecoder(implicit
        service: ChallengeService
    ): String => Challenge =
      service.challengesById
    implicit val challengeEncoder: Challenge => String =
      _.id

    implicit val equipmentTypeDecoder: String => EquipmentType =
      EquipmentType.valueOf
    implicit val equipmentTypeEncoder: EquipmentType => String =
      _.name

    implicit val itemRarityDecoder: String => ItemRarity =
      ItemRarity.valueOf
    implicit val itemRarityEncoder: ItemRarity => String =
      _.name

    implicit def shopItemDecoder(implicit
        service: ShopService
    ): String => ShopItem =
      service.itemsById
    implicit val shopItemEncoder: ShopItem => String =
      _.id
  }
}
