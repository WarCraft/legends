package gg.warcraft.legends.app.ai.npc.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Singleton;
import gg.warcraft.legends.api.ai.npc.Npc;
import gg.warcraft.legends.api.ai.npc.service.NpcRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class DefaultNpcRepository implements NpcRepository {
    private final Map<UUID, Npc> internalNpcs;

    private ImmutableMap<UUID, Npc> npcs;

    public DefaultNpcRepository() {
        this.internalNpcs = new HashMap<>();
        this.npcs = ImmutableMap.of();
    }

    @Override
    public Npc getNpc(UUID npcId) {
        return npcs.get(npcId);
    }

    @Override
    public ImmutableSet<UUID> getNpcIds() {
        return npcs.keySet();
    }

    @Override
    public void save(Npc npc) {
        internalNpcs.put(npc.getId(), npc);
        npcs = ImmutableMap.copyOf(internalNpcs);
    }

    @Override
    public void delete(UUID npcId) {
        internalNpcs.remove(npcId);
        npcs = ImmutableMap.copyOf(internalNpcs);
    }
}
