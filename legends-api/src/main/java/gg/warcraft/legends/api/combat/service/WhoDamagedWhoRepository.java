package gg.warcraft.legends.api.combat.service;

import java.util.Set;
import java.util.UUID;

public interface WhoDamagedWhoRepository {

    Set<UUID> getDamagers(UUID damagedId);

    void addDamager(UUID damagerId, UUID damagedId);

    void resetDamagers(UUID damagedId);
}
