/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat.passive

import gg.warcraft.combat.api.ability.AbilityPreCastEvent
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
import gg.warcraft.monolith.api.util.typing._

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
      case event @ AbilityPreCastEvent(_, caster, damageModifiers, _, _) =>
        playerService.getPlayerOption(caster.id) match {
          case Some(player) =>
            val modifierValue = player.combatClass.map { _.archetype } match {
              case Some(WARRIOR) => 0.1f * player.attributes.get(Strength)
              case Some(HUNTER)  => 0.1f * player.attributes.get(Dexterity)
              case Some(MAGE)    => 0.1f * player.attributes.get(Intellect)
              case _             => 0
            }
            if (modifierValue != 0) {
              val damageModifier = CombatValue.Modifier.flat(source, modifierValue)
              event.copy(damageModifiers = damageModifier :: damageModifiers).as[T]
            } else event.as[T]
          case None => event.as[T]
        }
      case _ => event
    }
  }
}
