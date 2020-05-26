/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.challenge.handler

class ChallengeCompletionHandler {

}

/* TODO
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
    private final ChallengeRepository challengeRepository;

    @Inject
    public ChallengeCompletionHandler(LegendsPlayerQueryService playerQueryService,
                                      PlayerCommandService playerCommandService,
                                      ChallengeRepository challengeRepository) {
        this.playerQueryService = playerQueryService;
        this.playerCommandService = playerCommandService;
        this.challengeRepository = challengeRepository;
    }

    @Subscribe
    public void onPlayerStatisticChangedEvent(PlayerStatisticChangedEvent event) {
        if (!(event.getStatistic() instanceof LegendsStatistic)) {
            return;
        }

        UUID playerId = event.getPlayerId();
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        LegendsStatistic statistic = (LegendsStatistic) event.getStatistic();
        Set<Challenge> statisticChallenges = challenges.getOrDefault(statistic, Collections.emptySet());
        statisticChallenges.forEach(challenge -> {
            if (challenge.isDaily() && !challengeRepository.getDailies().contains(challenge)) {
                return;
            }

            if (challenge.isWarry() && !challengeRepository.getWarries().contains(challenge)) {
                return;
            }

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

 */