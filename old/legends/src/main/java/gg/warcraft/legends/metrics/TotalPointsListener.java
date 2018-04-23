package gg.warcraft.legends.metrics;

import gg.warcraft.legends.objective.event.PointsTalliedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TotalPointsListener implements Listener {

    @EventHandler
    public void onPointsTalliedEvent(PointsTalliedEvent event) {
        MetricsService.getInstance().publishTotalPointsMetric(
                event.getHumanScore(),
                event.getOrcScore()
        );
    }
}
