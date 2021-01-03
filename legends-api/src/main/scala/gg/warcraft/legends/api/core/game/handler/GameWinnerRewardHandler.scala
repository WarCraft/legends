/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.game.handler

class GameWinnerRewardHandler {

}
/*
Team winner = event.getWinner();
            Item warCrate = lootCrateBuilderFactory.createLootCrateBuilder(LootCrateType.WAR_CRATE).build();
            playerQueryService.getOnlinePlayers().forEach(player -> {
                int rewardCount = player.getTeam() == winner ? 5 : 2;
                Item stackedItem = ((StackableItem) warCrate).withCount(rewardCount);
                playerCommandService.giveItem(player.getId(), stackedItem, false);
            });
 */