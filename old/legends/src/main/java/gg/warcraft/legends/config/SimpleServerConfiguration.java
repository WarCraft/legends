package gg.warcraft.legends.config;

import gg.warcraft.legends.entity.LegendsRace;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class SimpleServerConfiguration implements ServerConfiguration {
    private static final float DEFAULT_SPAWN_PITCH = 15.0f;

    private final Location humanSpawnLocation;
    private final Location orcSpawnLocation;
    private final Location elfSpawnLocation;
    private final Location dwarfSpawnLocation;
    private final Location defaultSpawnLocation;

    public SimpleServerConfiguration(World world) {
        humanSpawnLocation = new Location(world, -1554.5, 54.0, 357.5, -90.0f, DEFAULT_SPAWN_PITCH);
        humanSpawnLocation.setDirection(new Vector(1, 0, 0));
        // NOTE outpost: humanSpawnLocation = new Location(world, -656.5, 32.0, -167.5, 0f, DEFAULT_SPAWN_PITCH);
        orcSpawnLocation = new Location(world, 444.5, 45.0, 414.5, 0f, DEFAULT_SPAWN_PITCH);
        orcSpawnLocation.setDirection(new Vector(-1, 0, 0));
        // NOTE outpost: orcSpawnLocation = new Location(world, -406.5, 36.0, -158.5, 0f, DEFAULT_SPAWN_PITCH);
        elfSpawnLocation = new Location(world, 385.5, 62.0, -1517.5, 45.0f, DEFAULT_SPAWN_PITCH);
        dwarfSpawnLocation = new Location(world, -1497.5, 65.0, -1487.5, 45.0f, DEFAULT_SPAWN_PITCH);
        defaultSpawnLocation = new Location(world, -587.5, 90.0, -634.5, 90.0f, DEFAULT_SPAWN_PITCH);
    }

    public Location getSpawnLocationFor(LegendsRace race) {
        if (race == null) {
            return defaultSpawnLocation;
        }
        switch (race) {
            case HUMAN:
                return humanSpawnLocation;
            case ORC:
                return orcSpawnLocation;
            case ELF:
                return elfSpawnLocation;
            case DWARF:
                return dwarfSpawnLocation;
            default:
                return defaultSpawnLocation;
        }
    }
}
