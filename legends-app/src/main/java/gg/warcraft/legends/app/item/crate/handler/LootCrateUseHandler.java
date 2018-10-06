package gg.warcraft.legends.app.item.crate.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.item.crate.LootCrateType;
import gg.warcraft.legends.api.item.crate.service.LootCrateCommandService;
import gg.warcraft.monolith.api.entity.event.EntityPreInteractEvent;
import gg.warcraft.monolith.api.item.Inventory;
import gg.warcraft.monolith.api.item.Item;
import gg.warcraft.monolith.api.item.ItemReader;
import gg.warcraft.monolith.api.item.ItemReaderFactory;
import gg.warcraft.monolith.api.world.block.event.BlockPreInteractEvent;

import java.util.UUID;

public class LootCrateUseHandler {
    private static final String LOOT_CRATE_TYPE = "Loot Crate";

    private final LootCrateCommandService lootCrateCommandService;
    private final LegendsPlayerQueryService playerQueryService;
    private final ItemReaderFactory itemReaderFactory;

    @Inject
    public LootCrateUseHandler(LootCrateCommandService lootCrateCommandService,
                               LegendsPlayerQueryService playerQueryService,
                               ItemReaderFactory itemReaderFactory) {
        this.lootCrateCommandService = lootCrateCommandService;
        this.playerQueryService = playerQueryService;
        this.itemReaderFactory = itemReaderFactory;
    }

    private boolean isLootCrate(Item item) {
        ItemReader reader = itemReaderFactory.createItemReader(item);
        return LOOT_CRATE_TYPE.equals(reader.getType());
    }

    private boolean onInteract(UUID playerId, Item itemInHand) {
        if (!isLootCrate(itemInHand)) {
            return false;
        }

        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        Inventory inventory = player.getInventory();
        if (inventory.getSpace() > 0) {
            LootCrateType crateType = null; // TODO find out type
            lootCrateCommandService.openCrate(playerId, crateType);
            // TODO reduce itemInHand's stackSize by 1
        }
        return true;
    }

    @Subscribe
    public void onBlockPreInteractEvent(BlockPreInteractEvent event) {
        UUID playerId = event.getPlayerId();
        Item itemInHand = event.getItemInHand();
        switch (event.getInteraction()) {
            case RIGHT_CLICK_AIR:
            case RIGHT_CLICK_BLOCK:
                if (onInteract(playerId, itemInHand)) {
                    event.cancel();
                }
                break;
            default:
                if (isLootCrate(itemInHand)) {
                    event.cancel();
                }
                break;
        }
    }

    @Subscribe
    public void onEntityPreInteractEvent(EntityPreInteractEvent event) {
        UUID playerId = event.getPlayerId();
        Item itemInHand = event.getItemInHand();
        if (onInteract(playerId, itemInHand)) {
            event.cancel();
        }
    }
}
