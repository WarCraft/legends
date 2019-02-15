package gg.warcraft.legends.app.core.challenge.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.core.challenge.Challenge;
import gg.warcraft.legends.api.core.game.event.WarStartedEvent;
import gg.warcraft.legends.api.core.game.service.GameQueryService;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.LegendsPlayerData;
import gg.warcraft.legends.api.entity.player.LegendsStatistic;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.monolith.api.core.event.DailyTickEvent;
import gg.warcraft.monolith.api.entity.player.event.PlayerConnectEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public class ChallengeResetHandler {
    private final LegendsPlayerQueryService playerQueryService;
    private final LegendsPlayerCommandService playerCommandService;
    private final GameQueryService gameQueryService;

    @Inject
    public ChallengeResetHandler(LegendsPlayerQueryService playerQueryService,
                                 LegendsPlayerCommandService playerCommandService,
                                 GameQueryService gameQueryService) {
        this.playerQueryService = playerQueryService;
        this.playerCommandService = playerCommandService;
        this.gameQueryService = gameQueryService;
    }

    private int getCurrentNewYorkDay() {
        LocalDateTime currentUtcTime = LocalDateTime.now();
        LocalDateTime currentNewYorkTime = currentUtcTime.minusHours(5);
        return currentNewYorkTime.getDayOfMonth();
    }

    private void resetDailies(UUID playerId, int currentNewYorkDay) {
        LegendsStatistic.getDailies().forEach(daily ->
                playerCommandService.setData(playerId, daily.getKey(), "0"));
        Challenge.getDailies().forEach(daily ->
                playerCommandService.setData(playerId, daily.getPlayerData().getKey(), null));

        playerCommandService.setData(playerId,
                LegendsPlayerData.LAST_DAILY_RESET.getKey(), "" + currentNewYorkDay);
    }

    private void resetWarries(UUID playerId, int currentWarId) {
        LegendsStatistic.getWarries().forEach(warry ->
                playerCommandService.setData(playerId, warry.getKey(), "0"));
        Challenge.getWarries().forEach(warry ->
                playerCommandService.setData(playerId, warry.getPlayerData().getKey(), null));

        playerCommandService.setData(playerId,
                LegendsPlayerData.LAST_WARRY_RESET.getKey(), "" + currentWarId);
    }

    @Subscribe
    public void onPlayerConnectEvent(PlayerConnectEvent event) {
        UUID playerId = event.getPlayerId();
        LegendsPlayer player = playerQueryService.getPlayer(playerId);

        // dailies
        String lastPlayerDailyResetData = player.getData(LegendsPlayerData.LAST_DAILY_RESET.getKey());
        int lastPlayerDailyResetDay = lastPlayerDailyResetData != null
                ? Integer.parseInt(lastPlayerDailyResetData)
                : 0;

        int currentNewYorkDay = getCurrentNewYorkDay();
        if (currentNewYorkDay != lastPlayerDailyResetDay) {
            resetDailies(playerId, currentNewYorkDay);
        }

        // warries
        String lastPlayerWarryResetData = player.getData(LegendsPlayerData.LAST_WARRY_RESET.getKey());
        int lastPlayerWarryResetWar = lastPlayerWarryResetData != null
                ? Integer.parseInt(lastPlayerWarryResetData)
                : 0;

        int currentWarId = gameQueryService.getCurrentWar().getId();
        if (lastPlayerWarryResetWar != currentWarId) {
            resetWarries(playerId, currentWarId);
        }
    }

    @Subscribe
    public void onDailyTickEvent(DailyTickEvent event) {
        int currentNewYorkDay = getCurrentNewYorkDay();
        playerQueryService.getOnlinePlayers().forEach(player ->
                resetDailies(player.getId(), currentNewYorkDay));
    }

    @Subscribe
    public void onWarStartedEvent(WarStartedEvent event) {
        playerQueryService.getOnlinePlayers().forEach(player ->
                resetWarries(player.getId(), event.getGameId()));
    }
}
