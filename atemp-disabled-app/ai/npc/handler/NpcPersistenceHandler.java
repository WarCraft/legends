package gg.warcraft.legends.app.ai.npc.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.ai.npc.Npc;
import gg.warcraft.legends.api.ai.npc.service.NpcRepository;
import gg.warcraft.monolith.api.core.event.ServerShutdownEvent;
import gg.warcraft.monolith.api.entity.event.EntityDeathEvent;
import gg.warcraft.monolith.api.entity.event.EntityPreDespawnEvent;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;

public class NpcPersistenceHandler {
    private final NpcRepository npcRepository;
    private final EntityCommandService entityCommandService;

    @Inject
    public NpcPersistenceHandler(NpcRepository npcRepository, EntityCommandService entityCommandService) {
        this.npcRepository = npcRepository;
        this.entityCommandService = entityCommandService;
    }

    @Subscribe
    public void onEntityPreDespawnEvent(EntityPreDespawnEvent event) {
        if (npcRepository.getNpcIds().contains(event.getEntityId())) {
            event.cancel();
        }
    }

    @Subscribe
    public void onEntityDeathEvent(EntityDeathEvent event) {
        Npc npc = npcRepository.getNpc(event.getEntityId());
        if (npc != null) {
            npcRepository.delete(npc.getId());
        }
    }

    @Subscribe
    public void onServerShutDownEvent(ServerShutdownEvent event) {
        npcRepository.getNpcIds().forEach(entityCommandService::removeEntity);
    }
}
