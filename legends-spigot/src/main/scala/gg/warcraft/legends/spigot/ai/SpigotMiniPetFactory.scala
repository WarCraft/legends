/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.ai

import gg.warcraft.legends.api.ai.MiniPetFactory
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.player.PlayerService
import gg.warcraft.monolith.api.util.chaining.ChainingOps
import me.libraryaddict.disguise.DisguiseAPI
import me.libraryaddict.disguise.disguisetypes.{DisguiseType, MobDisguise}
import org.bukkit.Server
import org.bukkit.entity.{Player, Wolf}

import java.util.UUID
import scala.jdk.CollectionConverters.CollectionHasAsScala

class SpigotMiniPetFactory(implicit
    server: Server,
    playerService: PlayerService,
    entityService: EntityService
) extends MiniPetFactory {
  private final val goalsToRemove = List(
    // "random_stroll_land",
    "owner_hurt_target",
    "beg",
    "leap_at_target",
    "random_target_non_tamed",
    "nearest_attackable_target",
    "breed",
    // "look_at_player",
    // "float",
    // "wolf_avoid_entity",
    "hurt_by_target",
    // "follow_owner",
    // "random_lookaround",
    "melee_attack",
    "owner_hurt_by_target",
    "sit"
  )

  private var ownersToPets: Map[UUID, UUID] = Map.empty

  override def create(typ: Entity.Type, ownerId: UUID): UUID = {
    remove(ownerId)

    val owner = playerService.getPlayer(ownerId)
    val wolfId =
      entityService.spawnEntity(Entity.Type.WOLF, owner.location, owner.team)

    val spigotOwner = server.getEntity(ownerId).asInstanceOf[Player]
    val spigotWolf = server.getEntity(wolfId).asInstanceOf[Wolf]
    spigotWolf.setTamed(true)
    spigotWolf.setOwner(spigotOwner)
    spigotWolf.setBaby()
    spigotWolf.setAgeLock(true)
    spigotWolf.setInvulnerable(true)
    spigotWolf.setCustomNameVisible(false)

    val goals = server.getMobGoals.getAllGoals(spigotWolf)
    goals.asScala
      .filter { _.getKey.getNamespacedKey.getKey |> goalsToRemove.contains }
      .foreach { server.getMobGoals.removeGoal(spigotWolf, _) }

    val disguise = new MobDisguise(DisguiseType.valueOf(typ.toString))
    DisguiseAPI.disguiseToAll(spigotWolf, disguise)

    spigotWolf.getUniqueId
  }

  override def remove(ownerId: UUID): Unit =
    ownersToPets = ownersToPets.updatedWith(ownerId) {
      case Some(petId) =>
        entityService.removeEntity(petId)
        None
      case None => None
    }

  override def removeAll(): Unit =
    ownersToPets.keys.foreach(remove)
}
