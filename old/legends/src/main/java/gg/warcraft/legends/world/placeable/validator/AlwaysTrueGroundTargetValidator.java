package gg.warcraft.legends.world.placeable.validator;

import org.bukkit.Location;

public class AlwaysTrueGroundTargetValidator implements GroundTargetValidator {

    @Override
    public boolean isValidLocation(Location location) {
        return true;
    }
}
