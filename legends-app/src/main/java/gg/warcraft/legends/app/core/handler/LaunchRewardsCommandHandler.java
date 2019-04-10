package gg.warcraft.legends.app.core.handler;

import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.LegendsPlayerData;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.item.crate.LootCrateBuilderFactory;
import gg.warcraft.legends.api.item.crate.LootCrateType;
import gg.warcraft.legends.api.shop.PromotionId;
import gg.warcraft.legends.api.shop.profile.service.ShopProfileCommandService;
import gg.warcraft.monolith.api.command.Command;
import gg.warcraft.monolith.api.command.CommandHandler;
import gg.warcraft.monolith.api.command.CommandSender;
import gg.warcraft.monolith.api.item.Item;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

public class LaunchRewardsCommandHandler implements CommandHandler {
    private final LegendsPlayerQueryService playerQueryService;
    private final LegendsPlayerCommandService playerCommandService;
    private final ShopProfileCommandService shopProfileCommandService;
    private final Item threeWarCrates;
    private final Item twoLegendCrates;
    private final Item titanCrate;

    @Inject
    public LaunchRewardsCommandHandler(LegendsPlayerQueryService playerQueryService,
                                       LegendsPlayerCommandService playerCommandService,
                                       ShopProfileCommandService shopProfileCommandService,
                                       LootCrateBuilderFactory lootCrateBuilderFactory) {
        this.playerQueryService = playerQueryService;
        this.playerCommandService = playerCommandService;
        this.shopProfileCommandService = shopProfileCommandService;
        this.threeWarCrates = lootCrateBuilderFactory
                .createLootCrateBuilder(LootCrateType.WAR_CRATE)
                .build()
                .withStackSize(3);
        this.twoLegendCrates = lootCrateBuilderFactory
                .createLootCrateBuilder(LootCrateType.LEGEND_CRATE)
                .build()
                .withStackSize(2);
        this.titanCrate = lootCrateBuilderFactory
                .createLootCrateBuilder(LootCrateType.TITAN_CRATE)
                .build();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, List<String> arguments) {
        if (!sender.isPlayer()) {
            return false;
        }

        UUID playerId = sender.getPlayerId();
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        boolean hasExecutedLaunchCommand = player.getDataBool(LegendsPlayerData.LAUNCH_REWARDS_COMMAND.getKey());
        if (hasExecutedLaunchCommand) {
            playerCommandService.sendNotification(playerId, "You can only receive the launch rewards once!");
            return true;
        }

        playerCommandService.setData(playerId, LegendsPlayerData.LAUNCH_REWARDS_COMMAND.getKey(), "true");

        playerCommandService.addFragments(playerId, 1500);
        playerCommandService.giveItem(playerId, titanCrate, false);
        playerCommandService.giveItem(playerId, twoLegendCrates, false);
        playerCommandService.giveItem(playerId, threeWarCrates, false);
        shopProfileCommandService.unlockPromotion(PromotionId.FIVE_YEAR_OLD_CAKE, playerId, false);

        playerCommandService.sendNotification(playerId, "You received 1500 Fragments.");
        playerCommandService.sendNotification(playerId, "You received 3 War Crates.");
        playerCommandService.sendNotification(playerId, "You received 2 Legend Crates.");
        playerCommandService.sendNotification(playerId, "You received a Titan Crate.");
        playerCommandService.sendNotification(playerId, "You unlocked the Five Year Old Cake. You can equip it from your /me > collectables > promotions menu.");
        return true;
    }
}
