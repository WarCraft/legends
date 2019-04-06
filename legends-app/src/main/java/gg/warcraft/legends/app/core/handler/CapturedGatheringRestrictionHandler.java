package gg.warcraft.legends.app.core.handler;

import com.google.common.eventbus.Subscribe;
import gg.warcraft.gathering.api.gatherable.event.BlockPreGatheredEvent;
import gg.warcraft.gathering.api.gatherable.event.EntityPreGatheredEvent;
import gg.warcraft.legends.api.core.service.GatheringSpotOccupierRepository;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.entity.player.Player;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;
import gg.warcraft.siege.api.objective.event.ObjectiveCapturedEvent;
import gg.warcraft.siege.api.objective.event.ObjectiveNeutralizedEvent;
import gg.warcraft.siege.api.objective.service.ObjectiveQueryService;

import javax.inject.Inject;

public class CapturedGatheringRestrictionHandler {
    private final PlayerQueryService playerQueryService;
    private final GatheringSpotOccupierRepository gatheringSpotOccupierRepository;
    private final PlayerCommandService playerCommandService;

    private final static String CANNOT_GATHER_PLAYER_MESSAGE = "You need to capture this objective before you can gather it!";

    @Inject
    public CapturedGatheringRestrictionHandler(PlayerQueryService playerQueryService,
                                               GatheringSpotOccupierRepository gatheringSpotOccupierRepository,
                                               PlayerCommandService playerCommandService,
                                               ObjectiveQueryService objectiveQueryService,
                                               TaskService taskService) {
        this.playerQueryService = playerQueryService;
        this.gatheringSpotOccupierRepository = gatheringSpotOccupierRepository;
        this.playerCommandService = playerCommandService;
        taskService.runNextTick(() ->
            objectiveQueryService.getObjectives().forEach(objective ->
                gatheringSpotOccupierRepository.setTeamFor(objective.getId(), (LegendsTeam) objective.getOccupiers()))
        );
    }

    @Subscribe
    public void onBlockGatheringPreEvent(BlockPreGatheredEvent event) {
        final Player player = playerQueryService.getPlayer(event.getPlayerId());
        final LegendsTeam team = gatheringSpotOccupierRepository.getTeamFor(event.getGatheringSpotId());

        if (player.getTeam() != team) {
            playerCommandService.sendNotification(event.getPlayerId(), CANNOT_GATHER_PLAYER_MESSAGE);
            event.cancel();
        }
    }

    @Subscribe
    public void onEntityGatheringPreEvent(EntityPreGatheredEvent event) {
        final Player player = playerQueryService.getPlayer(event.getPlayerId());
        final LegendsTeam team = gatheringSpotOccupierRepository.getTeamFor(event.getGatheringSpotId());

        if (player.getTeam() != team) {
            playerCommandService.sendNotification(event.getPlayerId(), CANNOT_GATHER_PLAYER_MESSAGE);
            event.cancel();
        }
    }

    @Subscribe
    public void onObjectiveCapturedEvent(ObjectiveCapturedEvent event) {
        final LegendsTeam team = (LegendsTeam) event.getAttackers();
        gatheringSpotOccupierRepository.setTeamFor(event.getObjective().getId(), team);
    }

    @Subscribe
    public void onObjectiveNeutralizedEvent(ObjectiveNeutralizedEvent event) {
        gatheringSpotOccupierRepository.setTeamFor(event.getObjective().getId(), null);
    }
}
