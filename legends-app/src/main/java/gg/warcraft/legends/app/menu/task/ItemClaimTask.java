package gg.warcraft.legends.app.menu.task;

import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.world.item.Item;
import gg.warcraft.monolith.api.world.item.storage.ItemStorageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemClaimTask implements Runnable {
    private final ItemStorageService itemStorageService;
    private final LegendsPlayerQueryService playerQueryService;
    private final LegendsPlayerCommandService playerCommandService;
    private final TaskService taskService;
    private final UUID playerId;

    public ItemClaimTask(ItemStorageService itemStorageService,
                         LegendsPlayerQueryService playerQueryService,
                         LegendsPlayerCommandService playerCommandService,
                         TaskService taskService,
                         UUID playerId) {
        this.itemStorageService = itemStorageService;
        this.playerQueryService = playerQueryService;
        this.playerCommandService = playerCommandService;
        this.taskService = taskService;
        this.playerId = playerId;
    }

    @Override
    public void run() {
        // TODO List<Item> storedItems = itemStorageService.getItems(playerId);
        List<Item> storedItems = new ArrayList<>();
        if (storedItems.isEmpty()) {
            return;
        }

        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        if (!player.getInventory().hasSpace(1)) {
            playerCommandService.sendNotification(playerId,
                    "You don't have enough inventory space to claim all of your items. Please free up some space and try again.");
            return;
        }

        // TODO
//        Item itemToClaim = storedItems.get(0);
//        itemStorageService.claimItem(itemToClaim, playerId);
//        playerCommandService.giveItem(playerId, itemToClaim, false);
//        playerCommandService.sendNotification(playerId, "You received " + itemToClaim.getStackSize() +
//                " " + itemToClaim.getName() + "!");
//
//        taskService.runNextTick(this);
    }
}
