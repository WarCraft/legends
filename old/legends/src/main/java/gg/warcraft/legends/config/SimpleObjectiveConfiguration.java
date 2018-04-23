package gg.warcraft.legends.config;

import gg.warcraft.monolith.Monolith;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class SimpleObjectiveConfiguration implements ObjectiveConfiguration {
    private static final Monolith monolith = Monolith.getInstance();

    private final String id;
    private final String type;
    private final String name;
    private final Location captureLocation;
    private final double captureRadius;
    private final Location outerPortalEntryLocation;
    private final Location outerPortalExitLocation;
    private final Location innerPortalEntryLocation;
    private final Location innerPortalExitLocation;

    public SimpleObjectiveConfiguration(ConfigurationSection configuration) {
        id = configuration.getString("id");
        type = configuration.getString("type");
        name = configuration.getString("name");
        captureLocation = new Location(
                monolith.getWorld(),
                configuration.getDouble("captureLocation.x"),
                configuration.getDouble("captureLocation.y"),
                configuration.getDouble("captureLocation.z")
        );
        captureRadius = configuration.getDouble("captureRadius");
        outerPortalEntryLocation = new Location(
                monolith.getWorld(),
                configuration.getDouble("outerPortal.entryLocation.x"),
                configuration.getDouble("outerPortal.entryLocation.y"),
                configuration.getDouble("outerPortal.entryLocation.z")
        );
        outerPortalExitLocation = new Location(
                monolith.getWorld(),
                configuration.getDouble("outerPortal.exitLocation.x"),
                configuration.getDouble("outerPortal.exitLocation.y"),
                configuration.getDouble("outerPortal.exitLocation.z")
        );
        innerPortalEntryLocation = new Location(
                monolith.getWorld(),
                configuration.getDouble("innerPortal.entryLocation.x"),
                configuration.getDouble("innerPortal.entryLocation.y"),
                configuration.getDouble("innerPortal.entryLocation.z")
        );
        innerPortalExitLocation = new Location(
                monolith.getWorld(),
                configuration.getDouble("innerPortal.exitLocation.x"),
                configuration.getDouble("innerPortal.exitLocation.y"),
                configuration.getDouble("innerPortal.exitLocation.z")
        );
    }

    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Location getCaptureLocation() {
        return captureLocation;
    }

    @Override
    public double getCaptureRadius() {
        return captureRadius;
    }

    @Override
    public Location getOuterPortalEntryLocation() {
        return outerPortalEntryLocation;
    }

    @Override
    public Location getOuterPortalExitLocation() {
        return outerPortalExitLocation;
    }

    @Override
    public Location getInnerPortalEntryLocation() {
        return innerPortalEntryLocation;
    }

    @Override
    public Location getInnerPortalExitLocation() {
        return innerPortalExitLocation;
    }
}
