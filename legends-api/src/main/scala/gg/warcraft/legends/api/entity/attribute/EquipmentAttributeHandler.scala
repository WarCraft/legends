/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.entity.attribute

import gg.warcraft.legends.api.entity.attribute.LegendsAttribute._
import gg.warcraft.legends.api.item.equipment.EquipmentReader
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.entity.attribute.Attribute.Modifier._
import gg.warcraft.monolith.api.entity.attribute.{Attribute, AttributeService}
import gg.warcraft.monolith.api.player.{
  Player, PlayerConnectEvent, PlayerEquipmentChangedEvent, PlayerRespawnEvent
}

import java.util.UUID

class EquipmentAttributeHandler(implicit
    attributeService: AttributeService
) extends Event.Handler {
  private var equipmentModifiers: Map[UUID, List[Attribute.Modifier]] = Map.empty

  override def handle(event: Event): Unit = event match {
    case PlayerConnectEvent(player)          => updateModifiers(player, ignoreGeneric = true)
    case PlayerEquipmentChangedEvent(player) => updateModifiers(player)
    case PlayerRespawnEvent(player, _)       => updateModifiers(player)
    case _                                   =>
  }

  private def updateModifiers(
      player: Player,
      ignoreGeneric: Boolean = false
  ): Unit = {
    val currentModifiers = equipmentModifiers.getOrElse(player.id, Nil)
    attributeService.removeModifier(player.id, currentModifiers: _*)

    val (strength, dexterity, intellect, vitality, crit) =
      player.equipment.items.foldLeft((0, 0, 0, 0, 0)) { (stats, item) =>
        val reader = new EquipmentReader(item)
        if (reader.isEquipment)
          (
            stats._1 + reader.strength,
            stats._2 + reader.dexterity,
            stats._3 + reader.intellect,
            stats._4 + reader.vitality,
            stats._5 + reader.crit
          )
        else stats
      }

    val monolithModifiers = (
      Attribute.Modifier(Strength, FLAT, strength)
        :: Attribute.Modifier(Dexterity, FLAT, dexterity)
        :: Attribute.Modifier(Intellect, FLAT, intellect)
        :: Attribute.Modifier(Vitality, FLAT, vitality)
        :: Attribute.Modifier(Crit, FLAT, crit)
        :: Nil
    )

    val genericModifiers =
      if (!ignoreGeneric) {
        List(Attribute.Modifier(Attribute.Generic.MAX_HEALTH, FLAT, vitality))
      } else Nil

    val newModifiers = monolithModifiers ++ genericModifiers

    attributeService.addModifier(player.id, newModifiers: _*)
    equipmentModifiers += (player.id -> newModifiers)
  }
}
