package gg.warcraft.legends.app.combat.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.combat.api.specialization.event.PlayerSpecializationChangedEvent;
import gg.warcraft.monolith.api.combat.ProjectileType;
import gg.warcraft.monolith.api.combat.event.ProjectileLaunchEvent;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.entity.player.Player;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;
import gg.warcraft.monolith.api.world.item.Item;
import gg.warcraft.monolith.api.world.item.ItemService;
import gg.warcraft.monolith.api.world.item.ItemType;

import java.util.UUID;

public class ArrowDistributionHandler {
    private final PlayerQueryService playerQueryService;
    private final PlayerCommandService playerCommandService;
    private final ItemService itemService;
    private final TaskService taskService;

    @Inject
    public ArrowDistributionHandler(PlayerQueryService playerQueryService, PlayerCommandService playerCommandService,
                                    ItemService itemService, TaskService taskService) {
        this.playerQueryService = playerQueryService;
        this.playerCommandService = playerCommandService;
        this.itemService = itemService;
        this.taskService = taskService;
    }

    @Subscribe
    public void onProjectileLaunchEvent(ProjectileLaunchEvent event) {
        if (event.getProjectileType() == ProjectileType.ARROW) {
            UUID playerId = event.getShooterId();
            if (playerId != null) {
                Player player = playerQueryService.getPlayer(playerId);
                if (player != null) {
                    if (player.getInventory().hasSpace(1)
                            || player.getInventory().contains(ItemType.ARROW)) {
                        Item arrow = itemService.create(ItemType.ARROW).withName("Arrow");
                        taskService.runNextTick(() -> playerCommandService.giveItem(playerId, arrow, false));
                    }
                }
            }
        }
    }

    @Subscribe
    public void onPlayerSpecializationChangedEvent(PlayerSpecializationChangedEvent event) {
        if(event.getNewSpecialization() == null) {
            System.out.println("New spec was null?");
            return;
        }
        switch (event.getNewSpecialization().getPermission()) {
            case "warcraft.marksman":
            case "warcraft.hunter":
                UUID playerId = event.getPlayerId();
                Player player = playerQueryService.getPlayer(playerId);
                if (!player.getInventory().contains(ItemType.ARROW)) {
                    Item arrow = itemService.create(ItemType.ARROW).withName("Arrow");
                    taskService.runNextTick(() -> playerCommandService.giveItem(playerId, arrow, false));
                }
        }
    }
}
