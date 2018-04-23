package gg.warcraft.legends.config;

import gg.warcraft.monolith.util.BoundingBlockBox;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GatheringConfiguration {

    public final Set<Material> materials;
    private final List<BoundingBlockBox> areas;

    public GatheringConfiguration(List<ConfigurationSection> configuration) {
        areas = new ArrayList<>();
        materials = new HashSet<>();
        configuration.forEach(section -> {
            double north = section.getDouble("boundaries.north");
            double east = section.getDouble("boundaries.east");
            double south = section.getDouble("boundaries.south");
            double west = section.getDouble("boundaries.west");
            double upper = section.getDouble("boundaries.upper");
            double lower = section.getDouble("boundaries.lower");

            section.getStringList("materials").forEach(material -> materials.add(Material.valueOf(material)));
        });
    }

    public List<BoundingBlockBox> getAreas() {
        return areas;
    }

    public Set<Material> getMaterials() {
        return materials;
    }
}
