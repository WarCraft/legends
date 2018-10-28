package gg.warcraft.legends.app.item.crate.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.item.crate.LootCrateReader;
import gg.warcraft.legends.api.item.crate.LootCrateReaderFactory;
import gg.warcraft.legends.api.item.crate.LootCrateType;
import gg.warcraft.legends.api.item.crate.service.LootCrateCommandService;
import gg.warcraft.monolith.api.entity.event.EntityPreInteractEvent;
import gg.warcraft.monolith.api.item.Inventory;
import gg.warcraft.monolith.api.item.Item;
import gg.warcraft.monolith.api.world.block.event.BlockPreInteractEvent;

import java.util.UUID;

public class LootCrateUseHandler {
    private static final String LOOT_CRATE_TYPE = "Loot Crate";
    private static final int ITEMS_PER_CRATE = 3;

    private final LootCrateCommandService lootCrateCommandService;
    private final LegendsPlayerQueryService playerQueryService;
    private final LootCrateReaderFactory lootCrateReaderFactory;

    @Inject
    public LootCrateUseHandler(LootCrateCommandService lootCrateCommandService,
                               LegendsPlayerQueryService playerQueryService,
                               LootCrateReaderFactory lootCrateReaderFactory) {
        this.lootCrateCommandService = lootCrateCommandService;
        this.playerQueryService = playerQueryService;
        this.lootCrateReaderFactory = lootCrateReaderFactory;
    }

    private boolean onInteract(UUID playerId, Item itemInHand) {
        System.out.println("DEBUG checking LootCrate interact");
        LootCrateReader reader = lootCrateReaderFactory.createLootCrateReader(itemInHand);
        if (!LOOT_CRATE_TYPE.equals(reader.getType())) {
            System.out.println("DEBUG not a LootCrate: " + reader.getType());
            return false;
        }

        LootCrateType crateType = reader.getCrateType();
        if (crateType == null) {
            System.out.println("DEBUG LootCrate type missing");
            return false;
        }

        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        Inventory inventory = player.getInventory();
        if (inventory.getSpace() >= ITEMS_PER_CRATE) {
            for (int i = 0; i < ITEMS_PER_CRATE; i += 1) {
                if (lootCrateCommandService.openCrate(playerId, crateType)) {
                    inventory.remove(itemInHand.withStackSize(1));
                }
            }
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
