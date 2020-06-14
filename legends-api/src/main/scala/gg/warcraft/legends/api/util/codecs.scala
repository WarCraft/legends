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
import io.circe.{Decoder, Encoder, KeyDecoder}

import scala.util.Try

object circe {
  implicit val archetypeDecoder: Decoder[LegendsCombatArchetype] =
    enumDecoder(LegendsCombatArchetype.valueOf)
  implicit val archetypeKeyDecoder: KeyDecoder[LegendsCombatArchetype] =
    KeyDecoder.instance(it => Some(LegendsCombatArchetype.valueOf(it)))
  implicit val archetypeEncoder: Encoder[LegendsCombatArchetype] =
    enumEncoder[LegendsCombatArchetype]

  implicit val attributeDecoder: Decoder[LegendsAttribute] =
    Decoder.decodeString.emapTry { it => Try { LegendsAttribute.parseName(it) } }
  implicit val attributeKeyDecoder: KeyDecoder[LegendsAttribute] =
    KeyDecoder.instance(it => Some(LegendsAttribute.parseName(it)))
  implicit val attributeEncoder: Encoder[LegendsAttribute] =
    Encoder.encodeString.contramap { _.name }

  implicit val attributeDistroDecoder: Decoder[AttributeDistribution] =
    enumDecoder(AttributeDistribution.valueOf)
  implicit val attributeDistroKeyDecoder: KeyDecoder[AttributeDistribution] =
    KeyDecoder.instance(it => Some(AttributeDistribution.valueOf(it)))
  implicit val attributeDistroEncoder: Encoder[AttributeDistribution] =
    enumEncoder[AttributeDistribution]

  implicit val equipmentTypeDecoder: Decoder[EquipmentType] =
    enumDecoder(EquipmentType.valueOf)
  implicit val equipmentTypeKeyDecoder: KeyDecoder[EquipmentType] =
    KeyDecoder.instance(it => Some(EquipmentType.valueOf(it)))
  implicit val equipmentTypeEncoder: Encoder[EquipmentType] =
    enumEncoder[EquipmentType]

  implicit val floatKeyDecoder: KeyDecoder[Float] =
    KeyDecoder.instance(it => Some(it.toFloat))

  implicit val itemRarityDecoder: Decoder[ItemRarity] =
    Decoder.decodeString.emapTry { it => Try { ItemRarity.parseName(it) } }
  implicit val itemRarityKeyDecoder: KeyDecoder[ItemRarity] =
    KeyDecoder.instance(it => Some(ItemRarity.parseName(it)))
  implicit val itemRarityEncoder: Encoder[ItemRarity] =
    Encoder.encodeString.contramap { _.name }

  implicit val teamDecoder: Decoder[LegendsTeam] =
    Decoder.decodeString.emapTry { it => Try { LegendsTeam.parseName(it) } }
  implicit val teamKeyDecoder: KeyDecoder[LegendsTeam] =
    KeyDecoder.instance(it => Some(LegendsTeam.parseName(it)))
  implicit val teamEncoder: Encoder[LegendsTeam] =
    Encoder.encodeString.contramap { _.name }
}

object quill {}
