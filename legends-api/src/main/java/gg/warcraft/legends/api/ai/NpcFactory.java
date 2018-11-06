package gg.warcraft.legends.api.ai;

import java.util.UUID;

public interface NpcFactory {

    UUID create(UUID entityId, String name);
}
