package gg.warcraft.legends.objective;

import gg.warcraft.monolith.objective.event.ObjectiveCapturedEvent;
import gg.warcraft.monolith.objective.event.ObjectiveNeutralizedEvent;
import gg.warcraft.monolith.util.MonolithTask;
import gg.warcraft.monolith.util.RunnableUtils;
import gg.warcraft.monolith.util.TimeUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class SkirmishObjectiveCapturedListener implements Listener {
    private static final long POINTS_INTERVAL = 5 * TimeUtils.TICKS_PER_MINUTE;

    private final SimpleScoreboard scoreboard;
    private final HashMap<String, MonolithTask> recurringPoints;

    public SkirmishObjectiveCapturedListener(SimpleScoreboard scoreboard) {
        this.scoreboard = scoreboard;
        this.recurringPoints = new HashMap<>();
    }

    private int getPointsForObjectiveType(String type) {
        switch (type) {
            case "KEEP":
                return 25;
            case "OUTPOST":
                return 10;
            case "RESOURCE":
                return 5;
            default:
                return 0;
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onObjectiveCapturedEvent(ObjectiveCapturedEvent event) {
        AbstractCapturableObjective objective = (AbstractCapturableObjective) event.getObjective();
        int points = getPointsForObjectiveType(objective.getType());
        scoreboard.addScore(objective.getOccupiers(), points);

        if (!recurringPoints.containsKey(objective.getName())) {
            MonolithTask recurringPointsTask = RunnableUtils.runTask(
                    () -> scoreboard.addScore(objective.getOccupiers(), points),
                    POINTS_INTERVAL, POINTS_INTERVAL);
            recurringPoints.put(objective.getName(), recurringPointsTask);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onObjectiveNeutralizedEvent(ObjectiveNeutralizedEvent event) {
        MonolithTask recurringPointsTask = recurringPoints.remove(event.getObjective().getName());
        if (recurringPointsTask != null) {
            recurringPointsTask.cancel();
        }
    }
}
