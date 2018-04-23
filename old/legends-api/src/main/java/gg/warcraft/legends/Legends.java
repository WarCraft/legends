package gg.warcraft.legends;

import org.bukkit.Bukkit;

public interface Legends {

    static Legends getInstance() {
        return (Legends) Bukkit.getPluginManager().getPlugin("Monolith");
    }
}
