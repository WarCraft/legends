/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item

import gg.warcraft.combat.api.combatclass.CombatClassService
import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.combat.api.util.combatplayer._
import gg.warcraft.legends.api.combat.LegendsCombatArchetype
import gg.warcraft.legends.api.entity.LegendsTeam
import gg.warcraft.legends.api.item.equipment.EquipmentGenerator
import gg.warcraft.legends.api.item.equipment.config.EquipmentConfigRepository
import gg.warcraft.legends.api.player.statistic.LegendsStatistic
import gg.warcraft.monolith.api.core.Duration.DurationOps
import gg.warcraft.monolith.api.core.auth.Principal
import gg.warcraft.monolith.api.core.command.Command
import gg.warcraft.monolith.api.core.event.{Event, PreEvent}
import gg.warcraft.monolith.api.item.{ItemPrePickupEvent, ItemService}
import gg.warcraft.monolith.api.player.statistic.StatisticService
import gg.warcraft.monolith.api.player.{Player, PlayerService}
import gg.warcraft.monolith.api.world.Location

import java.util.{Random, UUID}

class ItemDropHandler(implicit
    itemService: ItemService,
    equipmentConfigRepository: EquipmentConfigRepository,
    combatClassService: CombatClassService,
    playerService: PlayerService,
    combatDataService: CombatDataService,
    statisticService: StatisticService
) extends Command.Handler
    with Event.Handler {
  private val RESERVE_DURATION = 60.seconds

  private val generator = new EquipmentGenerator()
  private val random = new Random(System.currentTimeMillis)

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
        val world = args(2)
        val x = args(3).toFloat
        val y = args(4).toFloat
        val z = args(5).toFloat

        val damagers: Set[UUID] = whoDamagedWhoRepository.damagers(mobId)
        damagers.foreach { damagerId =>
          playerService.getPlayerOption(damagerId).foreach { player =>
            val combatClass = player.combatClass match {
              case Some(it) => it
              case None =>
                throw new IllegalStateException(s"$damagerId has no combat class")
            }
            val archetype = combatClass.name match {
              case "Berserker"    => LegendsCombatArchetype.WARRIOR
              case "Guardian"     => LegendsCombatArchetype.WARRIOR
              case "Marksman"     => LegendsCombatArchetype.HUNTER
              case "Ranger"       => LegendsCombatArchetype.HUNTER
              case "Elementalist" => LegendsCombatArchetype.MAGE
              case "Wizard"       => LegendsCombatArchetype.MAGE
            }

            val drop = generator.generate(
              archetype,
              player.team.get.asInstanceOf[LegendsTeam]
            )
            if (player.location.world.name != world)
              throw new IllegalStateException(s"$damagerId has left $world")
            val location = new Location(player.location.world, x, y, z)
            val dropped = itemService.dropItems(location, drop)
            val droppedId = dropped(0)

            reservedFor += (droppedId -> damagerId)
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

            statisticService.increaseStatistic(
              damagerId,
              1,
              LegendsStatistic.DailyMobs.id,
              LegendsStatistic.MonthlyMobs.id,
              LegendsStatistic.AlltimeMobs.id
            )
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
      val isReserved = reservedUntil.getOrElse(itemId, 0) > System.currentTimeMillis
      if (isReserved) {
        if (reservedFor(itemId) != entityId) {
          event.copy(cancelled = true).asInstanceOf[T]
        } else event
      } else {
        reservedFor -= itemId
        reservedUntil -= itemId
        event
      }
    case _ => event
  }
}
