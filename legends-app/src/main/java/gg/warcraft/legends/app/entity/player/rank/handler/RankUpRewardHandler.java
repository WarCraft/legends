package gg.warcraft.legends.app.entity.player.rank.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.LegendsPlayerData;
import gg.warcraft.legends.api.entity.player.rank.event.PlayerRankChangedEvent;
import gg.warcraft.legends.api.entity.player.rank.service.RankRepository;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.item.crate.LootCrateBuilderFactory;
import gg.warcraft.legends.api.item.crate.LootCrateType;
import gg.warcraft.monolith.api.command.Command;
import gg.warcraft.monolith.api.command.CommandHandler;
import gg.warcraft.monolith.api.command.CommandSender;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.item.Item;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RankUpRewardHandler implements CommandHandler {
    private final LegendsPlayerQueryService playerQueryService;
    private final PlayerCommandService playerCommandService;
    private final LootCrateBuilderFactory lootCrateBuilderFactory;
    private final Map<Integer, Map<LootCrateType, Integer>> rankUpRewards;

    @Inject
    public RankUpRewardHandler(LegendsPlayerQueryService playerQueryService,
                               PlayerCommandService playerCommandService,
                               LootCrateBuilderFactory lootCrateBuilderFactory,
                               RankRepository rankRepository) {
        this.playerQueryService = playerQueryService;
        this.playerCommandService = playerCommandService;
        this.lootCrateBuilderFactory = lootCrateBuilderFactory;
        this.rankUpRewards = rankRepository.getRankUpRewards();
    }

    private void processRewards(UUID playerId, int playerLevel) {
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        if (player == null) {
            return;
        }

        int rankUpRewardsClaimed = player.getRankUpRewardsClaimed();
        while (rankUpRewardsClaimed < playerLevel) {
            int newRankUpRewardsClaimed = rankUpRewardsClaimed + 1;
            Map<LootCrateType, Integer> rewards = rankUpRewards.get(newRankUpRewardsClaimed);
            if (rewards.isEmpty()) {
                continue;
            }

            if (player.getInventory().getSpace() < 3) {
                playerCommandService.sendNotification(playerId,
                        "You have rewards for ranking up waiting for you, but you need at least 3 free spaces in your inventory. " +
                                " When you're ready you can claim them in the first button in /menu.");
                break;
            }

            rewards.forEach((crate, amount) -> {
                Item reward = lootCrateBuilderFactory.createLootCrateBuilder(crate).build().withStackSize(amount);
                playerCommandService.giveItem(playerId, reward, false);
                String pluralAdendum = amount > 1 ? "s" : "";
                playerCommandService.sendNotification(playerId, "You received " + amount + " " + reward.getName() +
                        pluralAdendum + " for ranking up!");
            });
            rankUpRewardsClaimed = newRankUpRewardsClaimed;
        }
        playerCommandService.setData(playerId, LegendsPlayerData.RANK_UP_REWARDS_CLAIMED.getName(),
                "" + rankUpRewardsClaimed);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, List<String> arguments) {
        if (!sender.isPlayer()) {
            return false;
        }

        UUID playerId = sender.getPlayerId();
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        if (player == null) {
            return false;
        }

        processRewards(playerId, player.getRank().getLevel());
        return true;
    }

    @Subscribe
    public void onPlayerRankChangedEvent(PlayerRankChangedEvent event) {
        processRewards(event.getPlayerId(), event.getNewRank().getLevel());
    }
}
