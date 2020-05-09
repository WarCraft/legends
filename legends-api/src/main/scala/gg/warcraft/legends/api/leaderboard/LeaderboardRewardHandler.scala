package gg.warcraft.legends.api.leaderboard

class LeaderboardRewardHandler {

}


/*
public class LeaderboardRewardHandler {
    private final LeaderboardQueryService leaderboardQueryService;
    private final PlayerCommandService playerCommandService;
    private final PlayerQueryService playerQueryService;
    private final StackableItem warCrate;
    private final StackableItem legendCrate;
    private final StackableItem titanCrate;

    @Inject
    public LeaderboardRewardHandler(LeaderboardQueryService leaderboardQueryService,
                                    PlayerCommandService playerCommandService,
                                    PlayerQueryService playerQueryService,
                                    LootCrateBuilderFactory lootCrateBuilderFactory) {
        this.leaderboardQueryService = leaderboardQueryService;
        this.playerCommandService = playerCommandService;
        this.playerQueryService = playerQueryService;
        this.warCrate = (StackableItem) lootCrateBuilderFactory.createLootCrateBuilder(LootCrateType.WAR_CRATE).build();
        this.legendCrate = (StackableItem) lootCrateBuilderFactory.createLootCrateBuilder(LootCrateType.LEGEND_CRATE).build();
        this.titanCrate = (StackableItem) lootCrateBuilderFactory.createLootCrateBuilder(LootCrateType.TITAN_CRATE).build();
    }

    @Subscribe
    public void onDailyTickEvent(DailyTickEvent event) {
        LegendsStatistic.getDailies().forEach(daily -> {
            List<UUID> leaders = leaderboardQueryService.getLeaders(daily, 3).stream()
                    .map(LeaderboardEntry::getPlayerId)
                    .collect(Collectors.toList());
            if (leaders.isEmpty()) {
                return;
            }

            for (int i = 1; i <= 3; i += 1) {
                UUID playerId = leaders.get(i - 1);
                StackableItem crates;
                String placing;
                switch (i) {
                    case 1:
                        crates = legendCrate.withCount(1);
                        placing = "first";
                        break;
                    case 2:
                        crates = warCrate.withCount(2);
                        placing = "second";
                        break;
                    case 3:
                        crates = warCrate.withCount(1);
                        placing = "third";
                        break;
                    default:
                        crates = null;
                        placing = null;
                        break;
                }
                playerCommandService.giveItem(playerId, crates, false);
                playerCommandService.sendNotification(playerId, "You received " + crates.count() + " " +
                        crates.name() + " for finishing " + placing + " on the " + daily.getName() + " Leaderboard!");
            }
        });
    }

    @Subscribe
    public void onWarFinishedEvent(WarFinishedEvent event) {
        LegendsStatistic.getWarries().forEach(warry -> {
            List<UUID> leaders = leaderboardQueryService.getLeaders(warry, 9).stream()
                    .map(LeaderboardEntry::getPlayerId)
                    .collect(Collectors.toList());
            for (int i = 1; i <= 9; i += 1) {
                UUID playerId = leaders.get(i - 1);
                StackableItem crates;
                String placing;
                switch (i) {
                    case 1:
                        crates = titanCrate.withCount(3);
                        placing = "first";
                        break;
                    case 2:
                        crates = titanCrate.withCount(2);
                        placing = "second";
                        break;
                    case 3:
                        crates = titanCrate.withCount(1);
                        placing = "third";
                        break;
                    case 4:
                        crates = legendCrate.withCount(3);
                        placing = "fourth";
                        break;
                    case 5:
                        crates = legendCrate.withCount(2);
                        placing = "fifth";
                        break;
                    case 6:
                        crates = legendCrate.withCount(1);
                        placing = "sixth";
                        break;
                    case 7:
                        crates = warCrate.withCount(3);
                        placing = "seventh";
                        break;
                    case 8:
                        crates = warCrate.withCount(2);
                        placing = "eighth";
                        break;
                    case 9:
                        crates = warCrate.withCount(1);
                        placing = "ninth";
                        break;
                    default:
                        crates = null;
                        placing = null;
                        break;
                }
                playerCommandService.giveItem(playerId, crates, false);
                playerCommandService.sendNotification(playerId, "You received " + crates.count() + " " +
                        crates.name() + " for finishing " + placing + " on the " + warry.getName() + " Leaderboard!");
            }
        });
    }
}

 */