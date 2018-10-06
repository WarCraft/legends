package gg.warcraft.legends.spigot.core.handler;

import com.google.inject.Inject;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.util.TimeUtils;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
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

public class ProfanityHandler implements Listener {
    private static final String WATCH_YO_PROFAMITY = "Watch yo profamity!";
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

    private final TaskService taskService;

    @Inject
    public ProfanityHandler(TaskService taskService) {
        this.taskService = taskService;
    }

    private long getBanDuration(UUID playerId) {
        int hasBeenBannedTimes = timesBanned.getOrDefault(playerId, 0);
        if (hasBeenBannedTimes >= banTimes.size()) {
            hasBeenBannedTimes = banTimes.size() - 1;
        }
        long banTime = banTimes.get(hasBeenBannedTimes);
        timesBanned.put(playerId, hasBeenBannedTimes + 1);
        return banTime;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncChatEvent(AsyncPlayerChatEvent event) {
        String message = event.getMessage().toLowerCase();
        boolean profane = flagged.stream().anyMatch(message::contains);
        if (profane) {
            UUID playerId = event.getPlayer().getUniqueId();
            taskService.runNextTick(() -> {
                Bukkit.getBanList(BanList.Type.NAME).addBan(
                        event.getPlayer().getName(),
                        WATCH_YO_PROFAMITY,
                        new Timestamp(System.currentTimeMillis() + getBanDuration(playerId)),
                        null
                );
                event.getPlayer().kickPlayer(WATCH_YO_PROFAMITY);
            });
            event.setCancelled(true);
        }
    }
}
