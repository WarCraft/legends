package gg.warcraft.legends.app.core.handler;

import com.google.inject.Inject;
import gg.warcraft.legends.api.core.service.SpawnCommandService;
import gg.warcraft.legends.api.core.service.SpawnQueryService;
import gg.warcraft.monolith.api.command.Command;
import gg.warcraft.monolith.api.command.CommandHandler;
import gg.warcraft.monolith.api.command.CommandSender;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.util.TimeUtils;

import java.util.List;
import java.util.UUID;

public class SpawnCommandHandler implements CommandHandler {
    private final SpawnQueryService spawnQueryService;
    private final SpawnCommandService spawnCommandService;
    private final PlayerCommandService playerCommandService;
    private final TimeUtils timeUtils;

    @Inject
    public SpawnCommandHandler(SpawnQueryService spawnQueryService,
                               SpawnCommandService spawnCommandService,
                               PlayerCommandService playerCommandService,
                               TimeUtils timeUtils) {
        this.spawnQueryService = spawnQueryService;
        this.spawnCommandService = spawnCommandService;
        this.playerCommandService = playerCommandService;
        this.timeUtils = timeUtils;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, List<String> arguments) {
        if (!sender.isPlayer()) {
            return false;
        }

        UUID playerId = sender.getPlayerId();
        long cooldown = spawnQueryService.getCooldown(playerId);
        long now = System.currentTimeMillis();
        if (cooldown >= now) {
            String timeToGo = timeUtils.getTimeToGoUntil(cooldown);
            playerCommandService.sendNotification(playerId,
                    "You can return to spawn in " + timeToGo + ".");
        } else {
            spawnCommandService.warp(playerId);
        }
        return true;
    }
}
