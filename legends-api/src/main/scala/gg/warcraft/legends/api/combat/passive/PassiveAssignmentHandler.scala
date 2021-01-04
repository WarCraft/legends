/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat.passive

import gg.warcraft.combat.api.ability.AbilityService
import gg.warcraft.combat.api.combatclass.PlayerCombatClassChangedEvent
import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.combat.api.util.entity.EntityOps
import gg.warcraft.legends.api.combat.LegendsCombatArchetype._
import gg.warcraft.legends.api.combat.mage.racial.{ArcaneBarrier, UnstableBunker}
import gg.warcraft.legends.api.combat.warrior.racial
import gg.warcraft.legends.api.combat.warrior.racial.{
  BladeDance, Bloodlust, Sturdiness
}
import gg.warcraft.legends.api.entity.LegendsTeam._
import gg.warcraft.legends.api.util.combatclass.CombatClassOps
import gg.warcraft.monolith.api.combat.CombatSource
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.entity.status.StatusService
import gg.warcraft.monolith.api.entity.{EntityService, EntityTeamChangedEvent}
import gg.warcraft.monolith.api.player.{Player, PlayerConnectEvent, PlayerService}
import gg.warcraft.monolith.api.util.chaining._

import scala.util.chaining._

class PassiveAssignmentHandler(implicit
    statusService: StatusService,
    playerService: PlayerService,
    combatDataService: CombatDataService,
    abilityService: AbilityService,
    entityService: EntityService
) extends Event.Handler {
  private final val genericGroup = "legends.passive"
  private final val racialGroup = "legends.racial"

  private def cleanStatus(player: Player, groups: String*): Unit =
    groups.foreach { group =>
      player.status.effects
        .filter { _.group == group }
        .pipe { effects => statusService.removeEffect(player.id, effects.toSeq: _*) }
    }

  private def assignRacialPassive(player: Player): Unit = {
    lazy val combatSource = CombatSource(player.name, Some(player.id))
    val racialPassive = player.team match {
      case Some(team) if team.id == Dwarves =>
        player.combatClass.map { _.archetype } match {
          case Some(WARRIOR) =>
            new Sturdiness.StatusEffect(combatSource) |> Some.apply
          case Some(MAGE) =>
            new UnstableBunker.StatusEffect(combatSource) |> Some.apply
          case _ => None
        }

      case Some(team) if team.id == Elves =>
        player.combatClass.map { _.archetype } match {
          case Some(WARRIOR) =>
            new BladeDance.StatusEffect(combatSource) |> Some.apply
          case Some(MAGE) =>
            None // TODO new Regrowth.StatusEffect(combatSource) |> Some.apply
          case _ => None
        }

      case Some(team) if team.id == Humans =>
        player.combatClass.map { _.archetype } match {
          case Some(WARRIOR) =>
            new racial.LastStand.PassiveStatusEffect(combatSource) |> Some.apply
          case Some(MAGE) =>
            new ArcaneBarrier.StatusEffect(combatSource) |> Some.apply
          case _ => None
        }

      case Some(team) if team.id == Orcs =>
        player.combatClass.map { _.archetype } match {
          case Some(WARRIOR) =>
            new Bloodlust.StatusEffect(combatSource) |> Some.apply
          case Some(MAGE) =>
            None // TODO new FlameTotem.StatusEffect(combatSource) |> Some.apply
          case _ => None
        }
      case _ => None
    }

    racialPassive match {
      case Some(racialPassive) => statusService.addEffect(player.id, racialPassive)
      case None                =>
    }
  }

  override def handle(event: Event): Unit = event match {
    case PlayerConnectEvent(player) =>
      cleanStatus(player, genericGroup, racialGroup)
      val combatSource = CombatSource(player.name, Some(player.id))
      val attributesPassive = new AttributesPassive.StatusEffect(combatSource)
      val criticalPassive = new CriticalPassive.StatusEffect(combatSource)
      statusService.addEffect(player.id, attributesPassive, criticalPassive)
      assignRacialPassive(player)

    case EntityTeamChangedEvent(player: Player, _, _) =>
      cleanStatus(player, racialGroup)
      assignRacialPassive(player)

    case PlayerCombatClassChangedEvent(player, _, _) =>
      cleanStatus(player, racialGroup)
      assignRacialPassive(player)

    case _ =>
  }
}
