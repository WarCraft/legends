package gg.warcraft.legends.app.core.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.core.service.ServerRepository;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.item.crate.LootCrateBuilderFactory;
import gg.warcraft.legends.api.item.crate.LootCrateType;
import gg.warcraft.monolith.api.entity.event.EntityFatalDamageEvent;
import gg.warcraft.monolith.api.item.Item;
import gg.warcraft.siege.api.objective.event.ObjectiveCapturedEvent;

import java.util.UUID;

public class CaptureAndKillRewardHandler {
    private final LegendsPlayerQueryService playerQueryService;
    private final LegendsPlayerCommandService playerCommandService;
    private final ServerRepository serverRepository;
    private final Item warCrate;

    @Inject
    public CaptureAndKillRewardHandler(LegendsPlayerQueryService playerQueryService,
                                       LegendsPlayerCommandService playerCommandService,
                                       ServerRepository serverRepository,
                                       LootCrateBuilderFactory lootCrateBuilderFactory) {
        this.playerQueryService = playerQueryService;
        this.playerCommandService = playerCommandService;
        this.serverRepository = serverRepository;
        this.warCrate = lootCrateBuilderFactory.createLootCrateBuilder(LootCrateType.WAR_CRATE).build();
    }

    @Subscribe
    public void onObjectiveCapturedEvent(ObjectiveCapturedEvent event) {
        int favorForObjective = serverRepository.getObjectivePoints(event.getObjective().getType());
        if (favorForObjective > 0) {
            event.getAttackerIds().forEach(playerId -> {
                playerCommandService.addFavor(playerId, favorForObjective);
                playerCommandService.sendNotification(playerId, "You gained " + favorForObjective + " favor.");
            });
        }

        int cratesForObjective = serverRepository.getObjectiveCrates(event.getObjective().getType());
        if (cratesForObjective > 0) {
            Item warCrateStack = warCrate.withStackSize(cratesForObjective);
            event.getAttackerIds().forEach(playerId -> {
                playerCommandService.giveItem(playerId, warCrateStack, false);
                playerCommandService.sendNotification(playerId, "You gained " + cratesForObjective + " crates.");
            });
        }
    }

    @Subscribe
    public void onEntityDeathEvent(EntityFatalDamageEvent event) {
        UUID killedId = event.getEntityId();
        LegendsPlayer killedPlayer = playerQueryService.getPlayer(killedId);
        if (killedPlayer == null) {
            return;
        }

        UUID attackerId = event.getDamage().getSource().getEntityId();
        if (attackerId == null) {
            return;
        }

        LegendsPlayer attackingPlayer = playerQueryService.getPlayer(attackerId);
        if (attackingPlayer == null) {
            return;
        }

        int favorPlayerKill = serverRepository.getFavorPlayerKill();
        playerCommandService.addFavor(attackerId, favorPlayerKill);
        playerCommandService.sendNotification(attackerId, "You gained " + favorPlayerKill + " favor.");
    }
}
