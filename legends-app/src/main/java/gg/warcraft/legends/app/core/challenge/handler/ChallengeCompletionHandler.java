package gg.warcraft.legends.app.core.challenge.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.core.challenge.Challenge;
import gg.warcraft.legends.api.entity.player.LegendsCurrency;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.LegendsStatistic;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.monolith.api.entity.player.Statistic;
import gg.warcraft.monolith.api.entity.player.event.PlayerStatisticChangedEvent;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ChallengeCompletionHandler {
    private static final Map<Statistic, Set<Challenge>> challenges;

    static {
        challenges = new HashMap<>();
        Arrays.stream(Challenge.values()).forEach(challenge -> {
            Statistic statistic = challenge.getStatistic();
            Set<Challenge> newStatisticChallenges =
                    challenges.computeIfAbsent(statistic, key -> new HashSet<>());
            newStatisticChallenges.add(challenge);
            challenges.put(statistic, newStatisticChallenges);
        });
    }

    private final LegendsPlayerQueryService playerQueryService;
    private final PlayerCommandService playerCommandService;

    @Inject
    public ChallengeCompletionHandler(LegendsPlayerQueryService playerQueryService,
                                      PlayerCommandService playerCommandService) {
        this.playerQueryService = playerQueryService;
        this.playerCommandService = playerCommandService;
    }

    @Subscribe
    public void onPlayerStatisticChangedEvent(PlayerStatisticChangedEvent event) {
        UUID playerId = event.getPlayerId();
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        LegendsStatistic statistic = (LegendsStatistic) event.getStatistic();
        Set<Challenge> statisticChallenges = challenges.getOrDefault(statistic, Collections.emptySet());
        statisticChallenges.forEach(challenge -> {
            if (event.getNewCurrent() >= challenge.getRequiredStatisticValue()) {
                String playerDataKey = challenge.getPlayerData().getKey();
                if (!player.getDataBool(playerDataKey)) {
                    playerCommandService.setData(playerId, playerDataKey, "true");
                    playerCommandService.addCurrency(playerId, LegendsCurrency.FAVOR, challenge.getFavorReward());
                    playerCommandService.sendNotification(playerId, "You have completed the " +
                            challenge.getName() + " challenge!");
                    playerCommandService.sendNotification(playerId, "You gained " +
                            challenge.getFavorReward() + " favor.");
                }
            }
        });
    }
}
