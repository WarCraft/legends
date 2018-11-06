package gg.warcraft.legends.app.ai.npc.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gg.warcraft.legends.api.ai.npc.config.NpcConfiguration;
import gg.warcraft.legends.api.ai.npc.config.NpcsConfiguration;

import java.util.List;

public class JacksonNpcsConfiguration implements NpcsConfiguration {
    private final List<NpcConfiguration> npcs;

    @JsonCreator
    public JacksonNpcsConfiguration(@JsonProperty("npcs") List<NpcConfiguration> npcs) {
        this.npcs = npcs;
    }

    @Override
    public List<NpcConfiguration> getNpcs() {
        return npcs;
    }
}
