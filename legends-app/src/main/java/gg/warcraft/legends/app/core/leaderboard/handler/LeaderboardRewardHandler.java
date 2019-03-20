package gg.warcraft.legends.app.core.leaderboard.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.core.game.event.WarFinishedEvent;
import gg.warcraft.legends.api.core.leaderboard.service.LeaderboardQueryService;
import gg.warcraft.legends.api.entity.player.LegendsStatistic;
import gg.warcraft.legends.api.item.crate.LootCrateBuilderFactory;
import gg.warcraft.legends.api.item.crate.LootCrateType;
import gg.warcraft.monolith.api.core.event.DailyTickEvent;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;
import gg.warcraft.monolith.api.item.Item;

import java.util.List;
import java.util.UUID;

public class LeaderboardRewardHandler {
    private final LeaderboardQueryService leaderboardQueryService;
    private final PlayerCommandService playerCommandService;
    private final PlayerQueryService playerQueryService;
    private final Item warCrate;
    private final Item legendCrate;
    private final Item titanCrate;

    @Inject
    public LeaderboardRewardHandler(LeaderboardQueryService leaderboardQueryService,
                                    PlayerCommandService playerCommandService,
                                    PlayerQueryService playerQueryService,
                                    LootCrateBuilderFactory lootCrateBuilderFactory) {
        this.leaderboardQueryService = leaderboardQueryService;
        this.playerCommandService = playerCommandService;
        this.playerQueryService = playerQueryService;
        this.warCrate = lootCrateBuilderFactory.createLootCrateBuilder(LootCrateType.WAR_CRATE).build();
        this.legendCrate = lootCrateBuilderFactory.createLootCrateBuilder(LootCrateType.LEGEND_CRATE).build();
        this.titanCrate = lootCrateBuilderFactory.createLootCrateBuilder(LootCrateType.TITAN_CRATE).build();
    }

    @Subscribe
    public void onDailyTickEvent(DailyTickEvent event) {
        LegendsStatistic.getDailies().forEach(daily -> {
            List<UUID> leaders = leaderboardQueryService.getLeaders(daily, 3);
            for (int i = 1; i <= 3; i += 1) {
                UUID playerId = leaders.get(i - 1);
                Item crates;
                switch (i) {
                    case 1:
                        crates = legendCrate.withStackSize(1);
                        break;
                    case 2:
                        crates = warCrate.withStackSize(2);
                        break;
                    case 3:
                        crates = warCrate.withStackSize(1);
                        break;
                    default:
                        crates = warCrate.withStackSize(0);
                        break;
                }
                playerCommandService.giveItem(playerId, crates, false);
            }
        });
    }

    @Subscribe
    public void onWarFinishedEvent(WarFinishedEvent event) {
        LegendsStatistic.getWarries().forEach(warry -> {
            List<UUID> leaders = leaderboardQueryService.getLeaders(warry, 9);
            for (int i = 1; i <= 9; i += 1) {
                UUID playerId = leaders.get(i - 1);
                Item crates;
                switch (i) {
                    case 1:
                        crates = titanCrate.withStackSize(3);
                        break;
                    case 2:
                        crates = titanCrate.withStackSize(2);
                        break;
                    case 3:
                        crates = titanCrate.withStackSize(1);
                        break;
                    case 4:
                        crates = legendCrate.withStackSize(3);
                        break;
                    case 5:
                        crates = legendCrate.withStackSize(2);
                        break;
                    case 6:
                        crates = legendCrate.withStackSize(1);
                        break;
                    case 7:
                        crates = warCrate.withStackSize(3);
                        break;
                    case 8:
                        crates = warCrate.withStackSize(2);
                        break;
                    case 9:
                        crates = warCrate.withStackSize(1);
                        break;
                    default:
                        crates = warCrate.withStackSize(0);
                        break;
                }
                playerCommandService.giveItem(playerId, crates, false);
            }
        });
    }
}
