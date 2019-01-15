package gg.warcraft.legends.app.ai.npc.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.ai.npc.Npc;
import gg.warcraft.legends.api.ai.npc.service.NpcQueryService;
import gg.warcraft.legends.api.ai.npc.service.NpcRepository;

import java.util.Set;
import java.util.UUID;

public class DefaultNpcQueryService implements NpcQueryService {
    private final NpcRepository npcRepository;

    @Inject
    public DefaultNpcQueryService(NpcRepository npcRepository) {
        this.npcRepository = npcRepository;
    }

    @Override
    public Set<UUID> getNpcIds() {
        return npcRepository.getNpcIds();
    }

    @Override
    public Npc getNpc(UUID entityId) {
        return npcRepository.getNpc(entityId);
    }
}
