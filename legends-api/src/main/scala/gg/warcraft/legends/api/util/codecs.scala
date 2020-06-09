/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.util

import gg.warcraft.legends.api.combat.LegendsCombatArchetype
import gg.warcraft.legends.api.entity.{LegendsAttribute, LegendsTeam}
import gg.warcraft.legends.api.item.ItemRarity
import gg.warcraft.legends.api.item.equipment.{AttributeDistribution, EquipmentType}
import gg.warcraft.monolith.api.core.Codecs.Circe._
import io.circe.{Decoder, Encoder}

import scala.util.Try

object circe {
  implicit lazy val attributeDecoder: Decoder[LegendsAttribute] =
    Decoder.decodeString.emapTry { it => Try { LegendsAttribute.parseName(it) } }
  implicit lazy val attributeEncoder: Encoder[LegendsAttribute] =
    Encoder.encodeString.contramap { _.name }

  implicit lazy val attributeDistributionDecoder: Decoder[AttributeDistribution] =
    enumDecoder(AttributeDistribution.valueOf)
  implicit lazy val attributeDistributionEncoder: Encoder[AttributeDistribution] =
    enumEncoder[AttributeDistribution]

  implicit lazy val combatArchetypeDecoder: Decoder[LegendsCombatArchetype] =
    enumDecoder(LegendsCombatArchetype.valueOf)
  implicit lazy val combatArchetypeEncoder: Encoder[LegendsCombatArchetype] =
    enumEncoder[LegendsCombatArchetype]

  implicit lazy val equipmentTypeDecoder: Decoder[EquipmentType] =
    enumDecoder(EquipmentType.valueOf)
  implicit lazy val equipmentTypeEncoder: Encoder[EquipmentType] =
    enumEncoder[EquipmentType]

  implicit lazy val itemRarityDecoder: Decoder[ItemRarity] =
    Decoder.decodeString.emapTry { it => Try { ItemRarity.parseName(it) } }
  implicit lazy val itemRarityEncoder: Encoder[ItemRarity] =
    Encoder.encodeString.contramap { _.name }

  implicit lazy val teamDecoder: Decoder[LegendsTeam] =
    Decoder.decodeString.emapTry { it => Try { LegendsTeam.parseName(it) } }
  implicit lazy val teamEncoder: Encoder[LegendsTeam] =
    Encoder.encodeString.contramap { _.name }
}

object quill {}
