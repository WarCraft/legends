package gg.warcraft.legends.spigot.core.handler;

import com.google.inject.Inject;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import gg.warcraft.legends.api.item.crate.LootCrateBuilderFactory;
import gg.warcraft.legends.api.item.crate.LootCrateType;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.item.Item;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class VoteHandler implements Listener {
    private final PlayerCommandService playerCommandService;
    private final Server server;
    private final Item voteCrate;

    @Inject
    public VoteHandler(PlayerCommandService playerCommandService,
                       LootCrateBuilderFactory lootCrateBuilderFactory,
                       Server server) {
        this.playerCommandService = playerCommandService;
        this.server = server;
        this.voteCrate = lootCrateBuilderFactory.createLootCrateBuilder(LootCrateType.VOTE_CRATE).build();
    }

    @EventHandler
    public void onVoteEvent(VotifierEvent event) {
        Vote vote = event.getVote();
        Player player = server.getPlayer(vote.getUsername());
        if (player == null) {
            return;
        }

        UUID playerId = player.getUniqueId();
        playerCommandService.giveItem(playerId, voteCrate, false);
        playerCommandService.sendNotification(playerId, "You received a vote crate.");
    }
}
