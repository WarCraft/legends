package gg.warcraft.legends.app.core.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.core.game.service.GameQueryService;
import gg.warcraft.legends.api.entity.player.event.PlayerFavorGainedEvent;
import gg.warcraft.legends.api.entity.player.event.PlayerGoldGainedEvent;
import gg.warcraft.legends.api.entity.player.event.PlayerGoldLostEvent;
import gg.warcraft.monolith.api.core.PersistenceService;

public class CurrenciesLeaderboardHandler {
    private static final String FAVOR_LEADERBOARD_KEY_PREFIX = "leaderboard:favor:";
    private static final String GOLD_LEADERBOARD_KEY_PREFIX = "leaderboard:gold:";

    private final GameQueryService gameQueryService;
    private final PersistenceService persistenceService;

    @Inject
    public CurrenciesLeaderboardHandler(GameQueryService gameQueryService, PersistenceService persistenceService) {
        this.gameQueryService = gameQueryService;
        this.persistenceService = persistenceService;
    }

    private String createLeaderboardKey(String keyPrefix) {
        int activeGameId = gameQueryService.getActiveGame().getId();
        return keyPrefix + activeGameId;
    }

    private String createAllTimeLeaderboardKey(String keyPrefix) {
        return keyPrefix + "alltime";
    }

    @Subscribe
    public void onPlayerFavorGainedEvent(PlayerFavorGainedEvent event) {
        String playerId = event.getPlayerId().toString();

        String favorKey = createLeaderboardKey(FAVOR_LEADERBOARD_KEY_PREFIX);
        persistenceService.incrSortedSet(favorKey, playerId, event.getAmount());

        String allTimeFavorKey = createAllTimeLeaderboardKey(FAVOR_LEADERBOARD_KEY_PREFIX);
        persistenceService.setSortedSet(allTimeFavorKey, playerId, event.getNewLifetime());
    }

    @Subscribe
    public void onPlayerGoldGainedEvent(PlayerGoldGainedEvent event) {
        String playerId = event.getPlayerId().toString();

        String goldKey = createLeaderboardKey(GOLD_LEADERBOARD_KEY_PREFIX);
        persistenceService.incrSortedSet(goldKey, playerId, event.getAmount());

        String allTimeGoldKey = createAllTimeLeaderboardKey(GOLD_LEADERBOARD_KEY_PREFIX);
        persistenceService.setSortedSet(allTimeGoldKey, playerId, event.getNewCurrent());
    }

    @Subscribe
    public void onPlayerGoldLostEvent(PlayerGoldLostEvent event) {
        String playerId = event.getPlayerId().toString();

        String goldKey = createLeaderboardKey(GOLD_LEADERBOARD_KEY_PREFIX);
        persistenceService.decrSortedSet(goldKey, playerId, event.getAmount());

        String allTimeGoldKey = createAllTimeLeaderboardKey(GOLD_LEADERBOARD_KEY_PREFIX);
        persistenceService.setSortedSet(allTimeGoldKey, playerId, event.getNewCurrent());
    }
}
