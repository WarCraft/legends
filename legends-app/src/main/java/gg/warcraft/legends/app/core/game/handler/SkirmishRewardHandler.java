package gg.warcraft.legends.app.core.game.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.core.game.event.SkirmishFinishedEvent;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.item.crate.LootCrateBuilderFactory;
import gg.warcraft.legends.api.item.crate.LootCrateType;
import gg.warcraft.monolith.api.entity.team.Team;
import gg.warcraft.monolith.api.world.item.Item;
import gg.warcraft.monolith.api.world.item.StackableItem;

public class SkirmishRewardHandler {
    private final LegendsPlayerQueryService playerQueryService;
    private final LegendsPlayerCommandService playerCommandService;
    private final LootCrateBuilderFactory lootCrateBuilderFactory;

    @Inject
    public SkirmishRewardHandler(LegendsPlayerQueryService playerQueryService,
                                 LegendsPlayerCommandService playerCommandService,
                                 LootCrateBuilderFactory lootCrateBuilderFactory) {
        this.playerQueryService = playerQueryService;
        this.playerCommandService = playerCommandService;
        this.lootCrateBuilderFactory = lootCrateBuilderFactory;
    }

    @Subscribe
    public void onSkirmishFinishedEvent(SkirmishFinishedEvent event) {
        Team winner = event.getWinner();
        Item warCrate = lootCrateBuilderFactory.createLootCrateBuilder(LootCrateType.WAR_CRATE).build();
        playerQueryService.getOnlinePlayers().forEach(player -> {
            int rewardCount = player.getTeam() == winner ? 5 : 2;
            Item stackedItem = ((StackableItem) warCrate).withCount(rewardCount);
            playerCommandService.giveItem(player.getId(), stackedItem, false);
        });
    }
}
