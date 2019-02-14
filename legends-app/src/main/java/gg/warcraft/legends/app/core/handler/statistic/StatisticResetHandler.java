package gg.warcraft.legends.app.core.handler.statistic;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
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

public class StatisticResetHandler {
    private final LegendsPlayerQueryService playerQueryService;
    private final LegendsPlayerCommandService playerCommandService;
    private final GameQueryService gameQueryService;

    @Inject
    public StatisticResetHandler(LegendsPlayerQueryService playerQueryService,
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

    private void resetDailies(UUID playerId) {
        LegendsStatistic.getDailies().forEach(daily ->
                playerCommandService.setData(playerId, daily.getKey(), "0"));

        int currentNewYorkDay = getCurrentNewYorkDay();
        playerCommandService.setData(playerId,
                LegendsPlayerData.LAST_DAILY_RESET.getKey(), "" + currentNewYorkDay);
    }

    private void resetWarries(UUID playerId) {
        LegendsStatistic.getWarries().forEach(warry ->
                playerCommandService.setData(playerId, warry.getKey(), "0"));

        int currentWar = gameQueryService.getCurrentWar().getId();
        playerCommandService.setData(playerId,
                LegendsPlayerData.LAST_WARRY_RESET.getKey(), "" + currentWar);
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
            resetDailies(playerId);
        }

        // warries
        String lastPlayerWarryResetData = player.getData(LegendsPlayerData.LAST_WARRY_RESET.getKey());
        int lastPlayerWarryResetWar = lastPlayerWarryResetData != null
                ? Integer.parseInt(lastPlayerWarryResetData)
                : 0;

        int currentWar = gameQueryService.getCurrentWar().getId();
        if (lastPlayerWarryResetWar != currentWar) {
            resetWarries(playerId);
        }
    }

    @Subscribe
    public void onDailyTickEvent(DailyTickEvent event) {
        playerQueryService.getOnlinePlayers().forEach(player ->
                resetDailies(player.getId()));
    }

    @Subscribe
    public void onWarStartedEvent(WarStartedEvent event) {
        playerQueryService.getOnlinePlayers().forEach(player ->
                resetWarries(player.getId()));
    }
}
