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
import gg.warcraft.monolith.api.item.Item;
import gg.warcraft.monolith.api.item.ItemBuilderFactory;
import gg.warcraft.monolith.api.item.ItemType;

import java.util.UUID;

public class ArrowDistributionHandler {
    private final PlayerQueryService playerQueryService;
    private final PlayerCommandService playerCommandService;
    private final ItemBuilderFactory itemBuilderFactory;
    private final TaskService taskService;

    @Inject
    public ArrowDistributionHandler(PlayerQueryService playerQueryService, PlayerCommandService playerCommandService,
                                    ItemBuilderFactory itemBuilderFactory, TaskService taskService) {
        this.playerQueryService = playerQueryService;
        this.playerCommandService = playerCommandService;
        this.itemBuilderFactory = itemBuilderFactory;
        this.taskService = taskService;
    }

    @Subscribe
    public void onProjectileLaunchEvent(ProjectileLaunchEvent event) {
        if (event.getProjectileType() == ProjectileType.ARROW) {
            UUID playerId = event.getShooterId();
            if (playerId != null) {
                Player player = playerQueryService.getPlayer(playerId);
                if (player != null) {
                    if (player.getInventory().getSpace() > 0
                            || player.getInventory().contains(ItemType.ARROW)) {
                        Item arrow = itemBuilderFactory.createItemBuilder(ItemType.ARROW, "Arrow").build();
                        taskService.runNextTick(() -> playerCommandService.giveItem(playerId, arrow, false));
                    }
                }
            }
        }
    }

    @Subscribe
    public void onPlayerSpecializationChangedEvent(PlayerSpecializationChangedEvent event) {
        switch (event.getNewSpecialization().getPermission()) {
            case "warcraft.marksman":
            case "warcraft.hunter":
                UUID playerId = event.getPlayerId();
                Player player = playerQueryService.getPlayer(playerId);
                if (!player.getInventory().contains(ItemType.ARROW)) {
                    Item arrow = itemBuilderFactory.createItemBuilder(ItemType.ARROW, "Arrow").build();
                    taskService.runNextTick(() -> playerCommandService.giveItem(playerId, arrow, false));
                }
        }
    }
}
