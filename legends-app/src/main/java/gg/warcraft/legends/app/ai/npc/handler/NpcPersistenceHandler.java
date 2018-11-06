package gg.warcraft.legends.app.ai.npc.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.ai.npc.Npc;
import gg.warcraft.legends.api.ai.npc.service.NpcRepository;
import gg.warcraft.monolith.api.entity.event.EntityDeathEvent;

public class NpcPersistenceHandler {
    private final NpcRepository npcRepository;

    @Inject
    public NpcPersistenceHandler(NpcRepository npcRepository) {
        this.npcRepository = npcRepository;
    }

    @Subscribe
    public void onEntityDeathEvent(EntityDeathEvent event) {
        Npc npc = npcRepository.getNpc(event.getEntityId());
        if (npc != null) {
            npcRepository.delete(npc.getId());
        }
    }
}
