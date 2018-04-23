package gg.warcraft.legends;

import gg.warcraft.monolith.util.RunnableUtils;
import gg.warcraft.monolith.util.TimeUtils;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WatchYoProfamity implements Listener {
    private static final List<String> flagged;
    private static final List<Long> banTimes;
    private static final Map<UUID, Integer> timesBanned;

    static {
        flagged = new ArrayList<>();
        flagged.add("nigger");

        banTimes = new ArrayList<>();
        banTimes.add(5L * TimeUtils.MILLIS_PER_SECOND);
        banTimes.add(5L * TimeUtils.MILLIS_PER_MINUTE);
        banTimes.add(5L * TimeUtils.MILLIS_PER_HOUR);
        banTimes.add(5L * TimeUtils.MILLIS_PER_DAY);

        timesBanned = new HashMap<>();
    }

    private long getBanDuration(Player player) {
        int hasBeenBannedTimes = timesBanned.getOrDefault(player.getUniqueId(), 0);
        if (hasBeenBannedTimes >= banTimes.size()) {
            hasBeenBannedTimes = banTimes.size() - 1;
        }
        return banTimes.get(hasBeenBannedTimes);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncChatEvent(AsyncPlayerChatEvent event) {
        if (flagged.stream().anyMatch(flag -> event.getMessage().toLowerCase().contains(flag))) {
            UUID playerId = event.getPlayer().getUniqueId();
            RunnableUtils.runOnNextTick(() -> {
                Bukkit.getBanList(BanList.Type.NAME).addBan(
                        event.getPlayer().getName(),
                        "Watch yo profamity!",
                        new Timestamp(System.currentTimeMillis() + getBanDuration(event.getPlayer())),
                        null
                );
                event.getPlayer().kickPlayer("Watch yo profamity!");
                timesBanned.put(playerId, timesBanned.getOrDefault(playerId, 0) + 1);
            });
            event.setCancelled(true);
        }
    }
}
