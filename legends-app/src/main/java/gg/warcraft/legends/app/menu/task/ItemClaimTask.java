package gg.warcraft.legends.app.menu.task;

import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.item.Item;
import gg.warcraft.monolith.api.item.service.ItemStorageCommandService;
import gg.warcraft.monolith.api.item.service.ItemStorageQueryService;

import java.util.List;
import java.util.UUID;

public class ItemClaimTask implements Runnable {
    private final ItemStorageQueryService itemStorageQueryService;
    private final ItemStorageCommandService itemStorageCommandService;
    private final LegendsPlayerQueryService playerQueryService;
    private final LegendsPlayerCommandService playerCommandService;
    private final TaskService taskService;
    private final UUID playerId;

    public ItemClaimTask(ItemStorageQueryService itemStorageQueryService,
                         ItemStorageCommandService itemStorageCommandService,
                         LegendsPlayerQueryService playerQueryService,
                         LegendsPlayerCommandService playerCommandService,
                         TaskService taskService,
                         UUID playerId) {
        this.itemStorageQueryService = itemStorageQueryService;
        this.itemStorageCommandService = itemStorageCommandService;
        this.playerQueryService = playerQueryService;
        this.playerCommandService = playerCommandService;
        this.taskService = taskService;
        this.playerId = playerId;
    }

    @Override
    public void run() {
        List<Item> storedItems = itemStorageQueryService.getStoredItems(playerId);
        if (storedItems.isEmpty()) {
            return;
        }

        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        if (player.getInventory().getSpace() < 1) {
            playerCommandService.sendNotification(playerId,
                    "You don't have enough inventory space to claim all of your items. Please free up some space and try again.");
            return;
        }

        Item itemToClaim = storedItems.get(0);
        itemStorageCommandService.claimItem(itemToClaim, playerId);
        playerCommandService.giveItem(playerId, itemToClaim, false);
        playerCommandService.sendNotification(playerId, "You received " + itemToClaim.getStackSize() +
                " " + itemToClaim.getName() + "!");

        taskService.runNextTick(this);
    }
}
