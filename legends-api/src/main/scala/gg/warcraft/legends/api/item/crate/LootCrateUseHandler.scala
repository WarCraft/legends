package gg.warcraft.legends.api.item.crate

import gg.warcraft.monolith.api.block.BlockPreInteractEvent
import gg.warcraft.monolith.api.core.event.{ Event, PreEvent }
import gg.warcraft.monolith.api.entity.EntityPreInteractEvent

class LootCrateUseHandler extends Event.Handler {
  def reduce[T <: PreEvent](event: T): T = event match {
    case event @ BlockPreInteractEvent(_, _, playerId, _, mainHand, _, _, _) =>
      if(onInteract(playerId, mainHand)) event.copy(cancelled = true).asInstanceOf[T]
      else event.asInstanceOf[T]
    case event @ EntityPreInteractEvent(_, _, playerId, _, mainHand, _, _, _, _) =>
      if(onInteract(playerId, mainHand)) event.copy(cancelled = true).asInstanceOf[T]
      else event.asInstanceOf[T]
  }
}


/*
 lootCrateService.crates.find {
    case (_, crate) => item == crate
  }.map { _._2 }
 */







/*
public class LootCrateUseHandler {
    private static final String LOOT_CRATE_TYPE = "Loot Crate";
    private static final int ITEMS_PER_CRATE = 3;


    private boolean onInteract(UUID playerId, Item itemInHand) {
        if (itemInHand == null) {
            return false;
        }

        LootCrateReader reader = lootCrateReaderFactory.createLootCrateReader(itemInHand);
        if (!LOOT_CRATE_TYPE.equals(reader.getType())) {
            return false;
        }

        LootCrateType crateType = reader.getCrateType();
        if (crateType == null) {
            pluginLogger.warning("Failed to open loot crate with name " + itemInHand.name() + " for player " +
                    playerId + ", no crate type exists for this item.");
            return true;
        }

        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        Inventory inventory = player.getInventory();
        if (inventory.hasSpace(ITEMS_PER_CRATE)) {
            if(itemService.takeFrom(playerId, ((StackableItem) itemInHand).withCount(1))) {
                for (int i = 0; i < ITEMS_PER_CRATE; i += 1) {
                    lootCrateCommandService.openCrate(playerId, crateType);
                }
                LootCrateOpenedEvent event = new SimpleLootCrateOpenedEvent(playerId);
                eventService.publish(event);
            } else {
                System.out.println("Failed to take item from " + playerId + " inventory for loot crate " + itemInHand);
            }
        } else {
            playerCommandService.sendNotification(playerId, "Please make sure you have at least " + ITEMS_PER_CRATE +
                    " empty slots in your inventory before opening a Loot Crate.");
        }
        return true;
    }

}

 */