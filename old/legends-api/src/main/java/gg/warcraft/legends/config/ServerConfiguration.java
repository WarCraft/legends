package gg.warcraft.legends.config;

import gg.warcraft.legends.entity.LegendsRace;
import org.bukkit.Location;

public interface ServerConfiguration {

    Location getSpawnLocationFor(LegendsRace race);
}
