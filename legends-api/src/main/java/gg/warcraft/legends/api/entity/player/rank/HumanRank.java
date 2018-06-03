package gg.warcraft.legends.api.entity.player.rank;

public enum HumanRank {
    SQUIRE("Squire"),
    FOOTMAN("Footman"),
    KNIGHT("Knight"),
    CAPTAIN("Captain"),
    CHAMPION("Chamption"),
    HERO("Hero"),
    LEGEND("Legend");

    private final String name;

    HumanRank(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
