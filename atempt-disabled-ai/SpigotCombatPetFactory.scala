/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.ai

import java.util.UUID

import gg.warcraft.legends.api.ai.CombatPetFactory
import gg.warcraft.monolith.api.util.chaining._
import org.bukkit.{ChatColor, Server}
import org.bukkit.attribute.{Attribute, AttributeModifier}
import org.bukkit.entity.{EntityType, LivingEntity, Player, Wolf}

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

  override def create(entityId: UUID, ownerId: UUID): Unit = {
    val entity = server.getEntity(entityId)
    val owner = server.getEntity(ownerId)

    (entity, owner) match {
      case (entity: LivingEntity, owner: Player) =>
        val nms = entity.asInstanceOf[CraftLivingEntity].getHandle
        nms match {
          case nms: EntityTameableAnimal =>
            val followModifier = new AttributeModifier(
              "monolith:follow_range",
              1.75,
              AttributeModifier.Operation.ADD_SCALAR
            )
            entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).addModifier(
              followModifier
            )

            nms.setTamed(true)
            nms.setOwnerUUID(ownerId)
            val name = ChatColor.LIGHT_PURPLE + owner.getName + "'s pet"
            nms.setCustomName(new ChatComponentText(name))

            setPathfinding(nms)
          case _ =>
            throw new IllegalArgumentException(
              s"Failed to turn non EntityTamableAnimal ${entity.getType} into a combat pet!"
            )
        }
      case _ => throw new IllegalArgumentException
    }
  }

  private def setPathfinding(nms: EntityTameableAnimal): Unit = {
    SpigotPathfinderUtils.resetSelectors(nms)

    // TODO double check these magic values
    val goal = nms.goalSelector
    // combat
    goal.a(0, new PathfinderGoalFloat(nms))
    goal.a(1, new PathfinderGoalLeapAtTarget(nms, 0.4f))
    goal.a(2, new PathfinderGoalMeleeAttack(nms, 1.3, false))
    // movement
    goal.a(3, new PathfinderGoalFollowOwner(nms, 1.3, 3.0f, 2.0f, false))
    goal.a(4, new PathfinderGoalLookAtPlayer(nms, classOf[EntityHuman], 8.0f))
    goal.a(4, new PathfinderGoalRandomLookaround(nms))

    val target = nms.targetSelector
    target.a(1, new PathfinderGoalOwnerHurtTarget(nms))
    target.a(2, new PathfinderGoalOwnerHurtByTarget(nms))
    target.a(3, new PathfinderGoalHurtByTarget(nms, classOf[LivingEntity]))
  }
}
