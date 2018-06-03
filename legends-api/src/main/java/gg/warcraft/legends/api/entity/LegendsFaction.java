package gg.warcraft.legends.api.entity;

import gg.warcraft.monolith.api.entity.Alliance;
import gg.warcraft.monolith.api.entity.Faction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum LegendsFaction implements Faction {
    ORC("Vorkulth", LegendsAlliance.ORC),
    HUMAN("Humenum", LegendsAlliance.HUMAN),
    DWARF("DWARF_PH", LegendsAlliance.DWARF),
    ELF("ELF_PH", LegendsAlliance.ELF),
    WAYFARER("Wayfarer", LegendsAlliance.WAYFARER);

    private static final Map<String, Faction> byName;

    static {
        byName = new HashMap<>();
        Arrays.stream(LegendsFaction.values()).forEach(faction -> byName.put(faction.getName(), faction));
    }

    public static Faction ofName(String name) {
        return byName.get(name);
    }

    private final String name;
    private final Alliance alliance;

    LegendsFaction(String name, Alliance alliance) {
        this.name = name;
        this.alliance = alliance;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Alliance getAlliance() {
        return alliance;
    }
}
