package gg.warcraft.legends.app.ai.npc.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.ai.npc.service.NpcQueryService;
import gg.warcraft.legends.api.ai.npc.service.NpcRepository;

import java.util.UUID;

public class DefaultNpcQueryService implements NpcQueryService {
    private final NpcRepository npcRepository;

    @Inject
    public DefaultNpcQueryService(NpcRepository npcRepository) {
        this.npcRepository = npcRepository;
    }

    @Override
    public boolean isNpc(UUID entityId) {
        return npcRepository.getNpcIds().contains(entityId);
    }
}
