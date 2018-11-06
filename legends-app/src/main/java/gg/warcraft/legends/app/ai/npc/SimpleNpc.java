package gg.warcraft.legends.app.ai.npc;

import gg.warcraft.legends.api.ai.npc.Npc;
import gg.warcraft.legends.api.ai.npc.NpcType;

import java.util.UUID;

public class SimpleNpc implements Npc {
    private final UUID id;
    private final NpcType type;

    public SimpleNpc(UUID id, NpcType type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public NpcType getType() {
        return type;
    }
}
