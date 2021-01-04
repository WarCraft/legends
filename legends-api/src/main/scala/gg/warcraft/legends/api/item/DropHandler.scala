/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item

import gg.warcraft.combat.api.combatclass.CombatClassService
import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.combat.api.util.entity._
import gg.warcraft.legends.api.combat.LegendsCombatClass
import gg.warcraft.legends.api.entity.LegendsTeam
import gg.warcraft.legends.api.item.equipment.EquipmentGenerator
import gg.warcraft.legends.api.item.equipment.config.EquipmentConfigRepository
import gg.warcraft.monolith.api.core.Duration.DurationOps
import gg.warcraft.monolith.api.core.auth.Principal
import gg.warcraft.monolith.api.core.command.Command
import gg.warcraft.monolith.api.core.event.{Event, PreEvent}
import gg.warcraft.monolith.api.entity.EntityService
import gg.warcraft.monolith.api.entity.team.TeamService
import gg.warcraft.monolith.api.item.{ItemPrePickupEvent, ItemService}
import gg.warcraft.monolith.api.player.statistic.StatisticService
import gg.warcraft.monolith.api.player.{Player, PlayerService}
import gg.warcraft.monolith.api.world.Location

import java.util.UUID
import java.util.logging.Logger

class DropHandler(implicit
    logger: Logger,
    itemService: ItemService,
    equipmentConfigRepository: EquipmentConfigRepository,
    combatClassService: CombatClassService,
    playerService: PlayerService,
    combatDataService: CombatDataService,
    statisticService: StatisticService,
    entityService: EntityService,
    teamService: TeamService
) extends Command.Handler
    with Event.Handler {
  private final val RESERVE_DURATION = 60.seconds

  private val generator = new EquipmentGenerator()

  private var reservedFor: Map[UUID, UUID] = Map.empty
  private var reservedUntil: Map[UUID, Long] = Map.empty

  override def handle(
      executor: Principal,
      command: Command,
      args: String*
  ): Command.Result = executor match {
    case _: Player           => Command.consoleOnly
    case _ if args.size != 6 => Command.invalid
    case _ =>
      try {
        val mobType = args(0)
        val mobId = UUID.fromString(args(1))
        val mob = entityService.getEntity(mobId)
        val world = args(2)
        val x = args(3).toFloat
        val y = args(4).toFloat
        val z = args(5).toFloat

        mob.combatData.attackedBy.foreach { attackerId =>
          playerService.getPlayerOption(attackerId).foreach { player =>
            val combatClass = player.combatClass match {
              case Some(it) => it
              case None =>
                logger.warning(
                  s"${player.name} has no combat class, dropping random item!"
                )
                LegendsCombatClass.random
            }

            val team = player.team match {
              case Some(it) => it
              case None =>
                logger.warning(s"${player.name} has no team, dropping random item!")
                LegendsTeam.random
            }

            val drop = generator.generate(combatClass, team)
            if (player.location.world.name != world)
              throw new IllegalStateException(s"$attackerId has left $world")
            val location = new Location(player.location.world, x, y, z)
            val dropped = itemService.dropItems(location, drop)
            val droppedId = dropped(0)

            reservedFor += (droppedId -> attackerId)
            reservedUntil += (droppedId -> (System.currentTimeMillis + RESERVE_DURATION.millis))

          /*
            UUID secondDroppedItemId = null;
            if (mobType.equals("Rider") || mobType.equals("Elemental")) {
                if (random.nextDouble() >= 0.5) {
                    Item secondDrop = equipmentGenerator.generate(specializationType, team);
                    UUID[] secondDroppedItemIds = itemService.dropItems(dropLocation, secondDrop);
                    secondDroppedItemId = secondDroppedItemIds[0];
                }
                playerCommandService.addFavor(damagerId, 2);
                playerCommandService.sendNotification(damagerId, "You gained 2 favor.");
            } else {
                playerCommandService.addFavor(damagerId, 1);
                playerCommandService.sendNotification(damagerId, "You gained 1 favor.");
            }

            int goldDropped = getGoldAmount(mobType);
            if (goldDropped > 0) {
                playerCommandService.addGold(damagerId, goldDropped);
                playerCommandService.sendNotification(damagerId, "You looted " + goldDropped + " gold.");
            }

            if (secondDroppedItemId != null) {
                reservedFor.put(secondDroppedItemId, damagerId);
                reservedUntil.put(secondDroppedItemId, System.currentTimeMillis() + RESERVE_DURATION);
            }

           */
          }
        }
        Command.success
      } catch {
        case ex: Exception =>
          println(s"Failed to drop items for $args: ${ex.getMessage}")
          Command.invalid
      }
  }

  /*
    private int getGoldAmount(String mobType) {
        switch (mobType) {
            case "Dunewalker":
            case "Charred":
                return random.nextInt(2);
            case "Elemental":
            case "Rider":
                return random.nextInt(3) + 3;
            default:
                return 0;
        }
    }
   */

  override def reduce[T <: PreEvent](event: T): T = event match {
    case event @ ItemPrePickupEvent(_, itemId, entityId, _, _, _) =>
      val isReserved = reservedUntil.getOrElse(itemId, 0L) > System.currentTimeMillis
      if (isReserved) {
        if (reservedFor(itemId) != entityId) {
          event.copy(cancelled = true).asInstanceOf[T]
        } else event.asInstanceOf[T]
      } else {
        reservedFor -= itemId
        reservedUntil -= itemId
        event.asInstanceOf[T]
      }
    case _ => event
  }
}
