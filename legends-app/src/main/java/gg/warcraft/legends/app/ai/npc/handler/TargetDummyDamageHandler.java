package gg.warcraft.legends.app.ai.npc.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.ai.npc.Npc;
import gg.warcraft.legends.api.ai.npc.service.NpcQueryService;
import gg.warcraft.monolith.api.entity.event.EntityPreDamageEvent;

public class TargetDummyDamageHandler {
    private final NpcQueryService npcQueryService;

    @Inject
    public TargetDummyDamageHandler(NpcQueryService npcQueryService) {
        this.npcQueryService = npcQueryService;
    }

    @Subscribe
    public void onEntityPreDamageEvent(EntityPreDamageEvent event) {
        Npc npc = npcQueryService.getNpc(event.getEntityId());
        if (npc != null && "TARGET_DUMMY".equals(npc.getType())) {
            event.cancel();
        }
    }
}
