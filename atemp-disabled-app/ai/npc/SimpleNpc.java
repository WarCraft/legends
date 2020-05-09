package gg.warcraft.legends.app.ai.npc;

import gg.warcraft.legends.api.ai.npc.Npc;
import gg.warcraft.legends.api.ai.npc.NpcType;

import java.util.UUID;

public class SimpleNpc implements Npc {
    private final UUID id;
    private final NpcType type;
    private final String name;

    public SimpleNpc(UUID id, NpcType type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public NpcType getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }
}
