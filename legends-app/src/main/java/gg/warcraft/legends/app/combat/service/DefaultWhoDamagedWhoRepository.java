package gg.warcraft.legends.app.combat.service;

import gg.warcraft.legends.api.combat.service.WhoDamagedWhoRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class DefaultWhoDamagedWhoRepository implements WhoDamagedWhoRepository {
    private final Map<UUID, Set<UUID>> damagers;

    public DefaultWhoDamagedWhoRepository() {
        this.damagers = new HashMap<>();
    }

    @Override
    public Set<UUID> getDamagers(UUID damagedId) {
        return damagers.getOrDefault(damagedId, Collections.emptySet());
    }

    @Override
    public void addDamager(UUID damagerId, UUID damagedId) {
        Set<UUID> damagedDamagers = damagers.computeIfAbsent(damagedId, id -> new HashSet<>());
        damagedDamagers.add(damagerId);
    }

    @Override
    public void resetDamagers(UUID damagedId) {
        Set<UUID> damagedDamagers = damagers.computeIfAbsent(damagedId, id -> new HashSet<>());
        damagedDamagers.clear();
    }
}
