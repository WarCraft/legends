package gg.warcraft.legends;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import gg.warcraft.monolith.player.MonolithPlayer;
import gg.warcraft.monolith.player.PlayerService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class VoteListener implements Listener {
    private static final int GOLD_PER_VOTE = 15;

    private final PlayerService playerService;

    public VoteListener(PlayerService playerService) {
        this.playerService = playerService;
    }

    @EventHandler
    public void onVotifierEvent(VotifierEvent event) {
        Vote vote = event.getVote();
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(vote.getUsername());
        if (offlinePlayer == null) {
            return;
        }
        UUID playerId = offlinePlayer.getUniqueId();
        if (playerId == null) {
            return;
        }

        MonolithPlayer player = playerService.getMonolithPlayer(playerId);
        player.addInGameCurrency(GOLD_PER_VOTE);
        boolean gaveCarrot = player.giveItem(new ItemStack(Material.GOLDEN_CARROT), false);
        player.addVote();

        if (player.isOnline()) {
            player.sendMessage(ChatColor.YELLOW + "You've been given " + GOLD_PER_VOTE + " gold and a golden carrot. Thank you for voting!");
            if (!gaveCarrot) {
                player.sendMessage(ChatColor.YELLOW + "Whoops! We couldn't give you the carrot, your inventory was full.");
            }
        }
    }
}
