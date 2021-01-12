/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat.passive

import gg.warcraft.combat.api.ability.AbilityPreCastEvent
import gg.warcraft.combat.api.ability.config.AbilityAttribute
import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.combat.api.util.entity.EntityOps
import gg.warcraft.legends.api.combat.LegendsCombatArchetype._
import gg.warcraft.legends.api.entity.attribute.LegendsAttribute._
import gg.warcraft.legends.api.util.combatclass.CombatClassOps
import gg.warcraft.monolith.api.combat.{CombatSource, CombatValue}
import gg.warcraft.monolith.api.core.Duration
import gg.warcraft.monolith.api.core.event.PreEvent
import gg.warcraft.monolith.api.entity.status.Status
import gg.warcraft.monolith.api.player.PlayerService

object AttributesPassive {
  class StatusEffect(
      override val source: CombatSource
  )(implicit
      playerService: PlayerService,
      combatDataService: CombatDataService
  ) extends Status.Effect {
    require(source.entityId.isDefined)

    override val name: String = "Attributes"
    override val group: String = "legends.passive"
    override val duration: Option[Duration] = None

    override def reduce[T <: PreEvent](event: T): T = event match {
      case event @ AbilityPreCastEvent(_, caster, modifiers, _, _) =>
        playerService.getPlayerOption(caster.id) match {
          case Some(player) =>
            val modifierValue = player.combatClass.map { _.archetype } match {
              case Some(WARRIOR) => 0.1f * player.attributes.get(Strength)
              case Some(HUNTER)  => 0.1f * player.attributes.get(Dexterity)
              case Some(MAGE)    => 0.1f * player.attributes.get(Intellect)
              case _             => 0
            }
            if (modifierValue != 0) {
              val outdated = modifiers.getOrElse(AbilityAttribute.DAMAGE, Nil)
              val modifier = CombatValue.Modifier.flat(source, modifierValue)
              val updated =
                modifiers + (AbilityAttribute.DAMAGE -> (modifier :: outdated))
              event.copy(modifiers = updated).asInstanceOf[T]
            } else event.asInstanceOf[T]
          case None => event.asInstanceOf[T]
        }
      case _ => event
    }
  }
}
