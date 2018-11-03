package gg.warcraft.legends.app.item.crate.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.item.crate.LootCrateReader;
import gg.warcraft.legends.api.item.crate.LootCrateReaderFactory;
import gg.warcraft.legends.api.item.crate.LootCrateType;
import gg.warcraft.legends.api.item.crate.service.LootCrateCommandService;
import gg.warcraft.monolith.api.core.PluginLogger;
import gg.warcraft.monolith.api.entity.event.EntityPreInteractEvent;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.item.Inventory;
import gg.warcraft.monolith.api.item.Item;
import gg.warcraft.monolith.api.world.block.event.BlockPreInteractEvent;

import java.util.UUID;
import java.util.logging.Logger;

public class LootCrateUseHandler {
    private static final String LOOT_CRATE_TYPE = "Loot Crate";
    private static final int ITEMS_PER_CRATE = 3;

    private final LootCrateCommandService lootCrateCommandService;
    private final PlayerCommandService playerCommandService;
    private final LegendsPlayerQueryService playerQueryService;
    private final LootCrateReaderFactory lootCrateReaderFactory;
    private final Logger pluginLogger;

    @Inject
    public LootCrateUseHandler(LootCrateCommandService lootCrateCommandService,
                               PlayerCommandService playerCommandService,
                               LegendsPlayerQueryService playerQueryService,
                               LootCrateReaderFactory lootCrateReaderFactory,
                               @PluginLogger Logger pluginLogger) {
        this.lootCrateCommandService = lootCrateCommandService;
        this.playerCommandService = playerCommandService;
        this.playerQueryService = playerQueryService;
        this.lootCrateReaderFactory = lootCrateReaderFactory;
        this.pluginLogger = pluginLogger;
    }

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
            pluginLogger.warning("Failed to open loot crate with name " + itemInHand.getName() + " for player " +
                    playerId + ", no crate type exists for this item.");
            return true;
        }

        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        Inventory inventory = player.getInventory();
        if (inventory.getSpace() >= ITEMS_PER_CRATE && inventory.remove(itemInHand.withStackSize(1))) {
            for (int i = 0; i < ITEMS_PER_CRATE; i += 1) {
                lootCrateCommandService.openCrate(playerId, crateType);
            }
        } else {
            playerCommandService.sendNotification(playerId, "Please make sure you have at least " + ITEMS_PER_CRATE +
                    " empty slots in your inventory before opening a Loot Crate.");
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
