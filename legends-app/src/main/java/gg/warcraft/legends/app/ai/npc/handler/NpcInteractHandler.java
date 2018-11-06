package gg.warcraft.legends.app.ai.npc.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.ai.npc.service.NpcCommandService;
import gg.warcraft.legends.api.ai.npc.service.NpcQueryService;
import gg.warcraft.monolith.api.entity.event.EntityPreInteractEvent;

import java.util.UUID;

public class NpcInteractHandler {
    private final NpcQueryService npcQueryService;
    private final NpcCommandService npcCommandService;

    @Inject
    public NpcInteractHandler(NpcQueryService npcQueryService, NpcCommandService npcCommandService) {
        this.npcQueryService = npcQueryService;
        this.npcCommandService = npcCommandService;
    }

    @Subscribe
    public void onEntityPreInteractEvent(EntityPreInteractEvent event) {
        UUID entityId = event.getEntityId();
        if (npcQueryService.isNpc(entityId)) {
            npcCommandService.openDialog(entityId, event.getPlayerId());
            event.cancel();
        }
    }
}
