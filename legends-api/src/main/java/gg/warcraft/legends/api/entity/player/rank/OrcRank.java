package gg.warcraft.legends.api.entity.player.rank;

public enum OrcRank implements RankSuffix {
    TUSKLING("Tuskling"),
    GRUNT("Grunt"),
    RAIDER("Raider"),
    MARAUDER("Marauder"),
    OVERSEER("Overseer"),
    WARLORD("Warlord"),
    LEGEND("Legend");

    private final String name;

    OrcRank(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
