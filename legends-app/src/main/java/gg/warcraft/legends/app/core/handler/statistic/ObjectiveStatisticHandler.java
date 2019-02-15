package gg.warcraft.legends.app.core.handler.statistic;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.LegendsStatistic;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.siege.api.objective.event.ObjectiveCapturedEvent;
import gg.warcraft.siege.api.objective.event.ObjectiveDefendedEvent;
import gg.warcraft.siege.api.objective.event.ObjectiveNeutralizedEvent;

public class ObjectiveStatisticHandler {
    private final LegendsPlayerCommandService playerCommandService;

    @Inject
    public ObjectiveStatisticHandler(LegendsPlayerCommandService playerCommandService) {
        this.playerCommandService = playerCommandService;
    }

    @Subscribe
    public void onObjectiveCapturedEvent(ObjectiveCapturedEvent event) {
        String objectiveType = event.getObjective().getType();
        LegendsStatistic[] statistics;
        switch (objectiveType) {
            case "RESOURCE":
                statistics = new LegendsStatistic[]{
                        LegendsStatistic.DAILY_CAMPS_CAPTURED,
                        LegendsStatistic.WARRY_CAMPS_CAPTURED,
                        LegendsStatistic.ALLTIME_CAMPS_CAPTURED};
                break;
            case "OUTPOST":
                statistics = new LegendsStatistic[]{
                        LegendsStatistic.DAILY_OUTPOSTS_CAPTURED,
                        LegendsStatistic.WARRY_OUTPOSTS_CAPTURED,
                        LegendsStatistic.ALLTIME_OUTPOSTS_CAPTURED};
                break;
            case "KEEP":
                statistics = new LegendsStatistic[]{
                        LegendsStatistic.DAILY_KEEPS_CAPTURED,
                        LegendsStatistic.WARRY_KEEPS_CAPTURED,
                        LegendsStatistic.ALLTIME_KEEPS_CAPTURED};
                break;
            default:
                statistics = new LegendsStatistic[]{};
                break;
        }

        event.getAttackerIds().forEach(attackerId ->
                playerCommandService.increaseStatistics(attackerId, 1, statistics));
    }

    @Subscribe
    public void onObjectiveNeutralizedEvent(ObjectiveNeutralizedEvent event) {
        String objectiveType = event.getObjective().getType();
        LegendsStatistic[] statistics;
        switch (objectiveType) {
            case "RESOURCE":
                statistics = new LegendsStatistic[]{
                        LegendsStatistic.DAILY_CAMPS_NEUTRALIZED,
                        LegendsStatistic.WARRY_CAMPS_NEUTRALIZED,
                        LegendsStatistic.ALLTIME_CAMPS_NEUTRALIZED};
                break;
            case "OUTPOST":
                statistics = new LegendsStatistic[]{
                        LegendsStatistic.DAILY_OUTPOSTS_NEUTRALIZED,
                        LegendsStatistic.WARRY_OUTPOSTS_NEUTRALIZED,
                        LegendsStatistic.ALLTIME_OUTPOSTS_NEUTRALIZED};
                break;
            case "KEEP":
                statistics = new LegendsStatistic[]{
                        LegendsStatistic.DAILY_KEEPS_NEUTRALIZED,
                        LegendsStatistic.WARRY_KEEPS_NEUTRALIZED,
                        LegendsStatistic.ALLTIME_KEEPS_NEUTRALIZED};
                break;
            default:
                statistics = new LegendsStatistic[]{};
                break;
        }

        event.getAttackerIds().forEach(attackerId ->
                playerCommandService.increaseStatistics(attackerId, 1, statistics));
    }

    @Subscribe
    public void onObjectiveDefendedEvent(ObjectiveDefendedEvent event) {
        String objectiveType = event.getObjective().getType();
        LegendsStatistic[] statistics;
        switch (objectiveType) {
            case "RESOURCE":
                statistics = new LegendsStatistic[]{
                        LegendsStatistic.DAILY_CAMPS_DEFENDED,
                        LegendsStatistic.WARRY_CAMPS_DEFENDED,
                        LegendsStatistic.ALLTIME_CAMPS_DEFENDED};
                break;
            case "OUTPOST":
                statistics = new LegendsStatistic[]{
                        LegendsStatistic.DAILY_OUTPOSTS_DEFENDED,
                        LegendsStatistic.WARRY_OUTPOSTS_DEFENDED,
                        LegendsStatistic.ALLTIME_OUTPOSTS_DEFENDED};
                break;
            case "KEEP":
                statistics = new LegendsStatistic[]{
                        LegendsStatistic.DAILY_KEEPS_DEFENDED,
                        LegendsStatistic.WARRY_KEEPS_DEFENDED,
                        LegendsStatistic.ALLTIME_KEEPS_DEFENDED};
                break;
            default:
                statistics = new LegendsStatistic[]{};
                break;
        }

        event.getAttackerIds().forEach(attackerId ->
                playerCommandService.increaseStatistics(attackerId, 1, statistics));
    }
}
