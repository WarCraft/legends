/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.ai

import java.util.UUID

import gg.warcraft.legends.api.ai.CombatPetFactory
import gg.warcraft.monolith.api.util.chaining._
import org.bukkit.Server
import org.bukkit.entity.{EntityType, Player, Wolf}

import scala.jdk.CollectionConverters._

class SpigotCombatPetFactory(implicit server: Server) extends CombatPetFactory {
  private final val goalsToRemove =
    "beg" :: "sit" :: "breed" :: "random_target_non_tamed" :: "nearest_attackable_target" :: Nil

  override def create(ownerId: UUID): UUID = {
    val owner = server.getEntity(ownerId).asInstanceOf[Player]
    val wolf = owner.getLocation.getWorld
      .spawnEntity(owner.getLocation, EntityType.WOLF)
      .asInstanceOf[Wolf]
    wolf.setTamed(true)
    wolf.setOwner(owner)
    wolf.setCustomName(s"${owner.getName}'s Pet'")

    val goals = server.getMobGoals.getAllGoals(wolf)
    goals.asScala
      .filter { _.getKey.getNamespacedKey.getKey |> goalsToRemove.contains }
      .foreach { server.getMobGoals.removeGoal(wolf, _) }

    wolf.getUniqueId
  }
}
