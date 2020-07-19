/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.ai

import java.util.UUID

import gg.warcraft.legends.api.ai.CombatPetFactory
import gg.warcraft.legends.api.entity.LegendsTeam._
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.entity.team.Team
import gg.warcraft.monolith.api.player.PlayerService
import gg.warcraft.monolith.api.util.chaining._
import me.libraryaddict.disguise.DisguiseAPI
import me.libraryaddict.disguise.disguisetypes.{DisguiseType, MobDisguise}
import org.bukkit.Server
import org.bukkit.entity.{Player, Wolf}

import scala.jdk.CollectionConverters._

class SpigotCombatPetFactory(implicit
    server: Server,
    playerService: PlayerService,
    entityService: EntityService
) extends CombatPetFactory {
  private final val goalsToRemove =
    "beg" :: "sit" :: "breed" :: "random_target_non_tamed" :: "nearest_attackable_target" :: Nil

  private val combatPetSkins = Map[Option[Team], MobDisguise](
    Some(Dwarves) -> new MobDisguise(DisguiseType.POLAR_BEAR),
    Some(Elves) -> new MobDisguise(DisguiseType.OCELOT),
    Some(Orcs) -> new MobDisguise(DisguiseType.PIG)
  )

  override def create(ownerId: UUID): UUID = {
    val owner = playerService.getPlayer(ownerId)
    val wolfId = entityService.spawnEntity(Entity.Type.WOLF, owner.location, owner.team)

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
