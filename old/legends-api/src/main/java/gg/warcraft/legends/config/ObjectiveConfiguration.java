package gg.warcraft.legends.config;

import org.bukkit.Location;

public interface ObjectiveConfiguration {

    String getId();

    String getType();

    String getName();

    Location getCaptureLocation();

    double getCaptureRadius();

    Location getOuterPortalEntryLocation();

    Location getOuterPortalExitLocation();

    Location getInnerPortalEntryLocation();

    Location getInnerPortalExitLocation();
}
