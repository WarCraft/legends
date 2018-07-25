package gg.warcraft.legends.spigot;

import com.google.inject.Injector;
import gg.warcraft.monolith.api.Monolith;
import org.bukkit.plugin.java.JavaPlugin;

public class LegendsPlugin extends JavaPlugin {
    private Injector injector;

    @Override
    public void onLoad() {
        Monolith.registerModule(new SpigotLegendsModule());
    }

    @Override
    public void onEnable() {
        this.injector = Monolith.getInstance().getInjector();
    }
}
