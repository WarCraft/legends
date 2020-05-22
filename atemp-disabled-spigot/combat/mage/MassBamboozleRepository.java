package gg.warcraft.legends.spigot.OLD.combat.mage;

import com.google.inject.Singleton;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Singleton
public class MassBamboozleRepository {
    private final Set<UUID> bamboozled;

    public MassBamboozleRepository() {
        this.bamboozled = new HashSet<>();
    }

    public boolean isBamboozled(UUID entityId) {
        return bamboozled.contains(entityId);
    }

    public void save(UUID entityId) {
        bamboozled.add(entityId);
    }

    public void delete(UUID entityId) {
        bamboozled.remove(entityId);
    }
}
