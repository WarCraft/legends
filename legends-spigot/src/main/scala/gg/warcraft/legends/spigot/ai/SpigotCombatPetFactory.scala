/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.ai

import gg.warcraft.legends.api.ai.CombatPetFactory
import gg.warcraft.legends.api.entity.LegendsTeam._
import gg.warcraft.monolith.api.entity.team.{Team, TeamService}
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.player.PlayerService
import gg.warcraft.monolith.api.util.chaining._
import me.libraryaddict.disguise.DisguiseAPI
import me.libraryaddict.disguise.disguisetypes.{DisguiseType, MobDisguise}
import org.bukkit.Server
import org.bukkit.entity.{Player, Wolf}

import java.util.UUID
import scala.jdk.CollectionConverters._

class SpigotCombatPetFactory(implicit
    server: Server,
    playerService: PlayerService,
    entityService: EntityService,
    teamService: TeamService
) extends CombatPetFactory {
  private final val goalsToRemove = List(
    "sit",
    "nearest_attackable_target",
    // "random_lookaround",
    // "follow_owner",
    // "float",
    "random_target_non_tamed",
    "breed",
    // "hurt_by_target",
    // "owner_hurt_target",
    // "melee_attack",
    "beg"
    // "owner_hurt_by_target",
    // "wolf_avoid_entity",
    // "look_at_player",
    // "leap_at_target",
    // "random_stroll_land"
  )

  private lazy val combatPetSkins = Map[Option[Team], MobDisguise](
    Some(Dwarves.instance) -> new MobDisguise(DisguiseType.POLAR_BEAR),
    Some(Elves.instance) -> new MobDisguise(DisguiseType.OCELOT),
    Some(Orcs.instance) -> new MobDisguise(DisguiseType.PIG)
  )

  override def create(ownerId: UUID): UUID = {
    val owner = playerService.getPlayer(ownerId)
    val wolfId =
      entityService.spawnEntity(Entity.Type.WOLF, owner.location, owner.team)

    val spigotOwner = server.getEntity(ownerId).asInstanceOf[Player]
    val spigotWolf = server.getEntity(wolfId).asInstanceOf[Wolf]
    spigotWolf.setTamed(true)
    spigotWolf.setOwner(spigotOwner)

    val nameColor = owner.team.map { _.chatColor.toString }.getOrElse("")
    spigotWolf.setCustomName(s"$nameColor${spigotOwner.getName}'s Pet'")

    val goals = server.getMobGoals.getAllGoals(spigotWolf)
    goals.asScala
      .filter { _.getKey.getNamespacedKey.getKey |> goalsToRemove.contains }
      .foreach { server.getMobGoals.removeGoal(spigotWolf, _) }

    combatPetSkins
      .get(owner.team)
      .foreach { DisguiseAPI.disguiseToAll(spigotWolf, _) }

    spigotWolf.getUniqueId
  }
}
