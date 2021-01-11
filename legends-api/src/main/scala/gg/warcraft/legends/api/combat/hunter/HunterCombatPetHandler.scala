/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat.hunter

import gg.warcraft.combat.api.ability.{AbilityCastEvent, AbilityService}
import gg.warcraft.combat.api.combatclass.{
  CombatClass, PlayerCombatClassChangedEvent
}
import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.combat.api.util.entity._
import gg.warcraft.legends.api.ai.CombatPetFactory
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.core.{GameMode, ServerShutdownEvent}
import gg.warcraft.monolith.api.entity.{
  EntityDeathEvent, EntityService, EntityTeamChangedEvent
}
import gg.warcraft.monolith.api.player.{
  Player, PlayerConnectEvent, PlayerDisconnectEvent, PlayerGameModeChangedEvent
}

import java.util.UUID

class HunterCombatPetHandler(implicit
    combatPetFactory: CombatPetFactory,
    combatDataService: CombatDataService,
    entityService: EntityService,
    abilityService: AbilityService
) extends Event.Handler {
  private var ownersToPets: Map[UUID, UUID] = Map.empty
  private var petsToOwners: Map[UUID, UUID] = Map.empty

  override def handle(event: Event): Unit = event match {
    case PlayerConnectEvent(player) if player.mode == GameMode.SURVIVAL =>
      spawnPet(player, player.combatClass)

    case PlayerCombatClassChangedEvent(player, _, current) =>
      removePet(player.id)
      spawnPet(player, current)

    case PlayerGameModeChangedEvent(player, current) => current match {
        case GameMode.SURVIVAL => spawnPet(player.id, player.combatClass)
        case _                 => removePet(player.id)
      }

    case EntityTeamChangedEvent(player: Player, _, _) =>
      if (removePet(player.id)) spawnPet(player, player.combatClass)

    case EntityDeathEvent(pet, _) if petsToOwners.contains(pet.id) =>
      val ownerId = petsToOwners(pet.id)
      ownersToPets -= ownerId
      petsToOwners -= pet.id
      abilityService.castAbility("Pet Respawns", ownerId, None)

    case AbilityCastEvent("Pet Respawns (Respawn)", caster, _) =>
      removePet(caster.id)
      spawnPet(caster, caster.combatClass)

    case PlayerDisconnectEvent(player) =>
      removePet(player.id)

    case ServerShutdownEvent() =>
      ownersToPets.keys.foreach(removePet)

    case _ =>
  }

  private def spawnPet(
      playerId: UUID,
      combatClass: Option[CombatClass]
  ): Unit = combatClass match {
    case Some(it) if it.name == "Marksman" || it.name == "Ranger" =>
      val petId = combatPetFactory.create(playerId)
      ownersToPets += (playerId -> petId)
      petsToOwners += (petId -> playerId)
    case _ =>
  }

  private def removePet(playerId: UUID): Boolean = {
    var hasPet = false
    ownersToPets = ownersToPets.updatedWith(playerId) {
      case Some(petId) =>
        entityService.removeEntity(petId)
        petsToOwners -= petId
        hasPet = true
        None
      case None => None
    }
    hasPet
  }
}
