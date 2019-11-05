package gg.warcraft.legends.app.core.leaderboard.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.core.game.event.WarFinishedEvent;
import gg.warcraft.legends.api.core.leaderboard.LeaderboardEntry;
import gg.warcraft.legends.api.core.leaderboard.service.LeaderboardQueryService;
import gg.warcraft.legends.api.entity.player.LegendsStatistic;
import gg.warcraft.legends.api.item.crate.LootCrateBuilderFactory;
import gg.warcraft.legends.api.item.crate.LootCrateType;
import gg.warcraft.monolith.api.core.event.DailyTickEvent;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;
import gg.warcraft.monolith.api.world.item.StackableItem;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
