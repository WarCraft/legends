package gg.warcraft.legends.app.core.handler;

import com.google.inject.Inject;
import gg.warcraft.legends.api.core.MetricsService;

public class TotalPointsMetricHandler {
    private final MetricsService metricsService;

    @Inject
    public TotalPointsMetricHandler(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    /* TODO event still needs implementing
    @Subscribe
    public void onPointsTalliedEvent(PointsTalliedEvent event) {
        metricsService.recordTotalPoints(LegendsTeam.HUMAN, event.getHuman());
        metricsService.recordTotalPoints(LegendsTeam.ORC, event.getOrc());
    }
    */
}
